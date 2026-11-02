package com.bibliotecaelo.service;

import com.bibliotecaelo.configuration.MinioPropsConfiguration;
import com.bibliotecaelo.domain.BucketFile;
import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.interfaces.EntidadeWithBucketFile;
import com.bibliotecaelo.repository.BucketFileRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
public abstract class CrudBucketService<E extends Entidade & EntidadeWithBucketFile> extends CrudService<E> {

    protected final MinioPropsConfiguration props;
    protected final MinioClient client;
    protected final BucketFileRepository bucketFileRepository;

    protected CrudBucketService(
        MinioPropsConfiguration props,
        MinioClient client,
        BucketFileRepository bucketFileRepository) {

        this.props = props;
        this.client = client;
        this.bucketFileRepository = bucketFileRepository;
    }

    private String getDadosBucketFile(BucketFile file) {
        return "Arquivo: " + file.getNome() + " Id: " + file.getFileId() + " Bucket: " + props.getBucketName();
    }

    private void putObjectOnBucket(BucketFile file, String dadosUpload) {
        log.info("Iniciando upload do arquivo {}.", dadosUpload);
        try {
            client.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(props.getBucketName())
                    .object(String.valueOf(file.getFileId()))
                    .stream(file.getInputStream(), file.getSize(), -1L)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (MinioException me) {
            throw new IllegalStateException(String.format("Erro ao incluir o arquivo %s no bucket %s", file.getNome(), props.getBucketName()), me);
        }
        log.info("Upload do arquivo finalizado {}.", dadosUpload);
    }

    private void removeObjectOnBucket(UUID fileID, String dadosRemove) {
        log.info("Iniciando remoção do arquivo {}.", dadosRemove);
        try {
            client.removeObject(
                RemoveObjectArgs
                    .builder()
                    .bucket(props.getBucketName())
                    .object(String.valueOf(fileID))
                    .build()
            );
        } catch (MinioException me) {
            throw new IllegalStateException(String.format("Erro ao remover o arquivo %s no bucket %s", fileID, props.getBucketName()), me);
        }
        log.info("Remoção do arquivo finalizada {}.", dadosRemove);
    }

    private void recoverDeletedFileOnBucket(boolean success, BucketFile file, String dadosRemove) {
        if (success) {
            log.error("Erro inesperado, reinserindo {} .", dadosRemove);
            putObjectOnBucket(file, dadosRemove);
        }
    }

    private void removeEagerlyInsertedFileOnBucket(boolean success, UUID fileId, String dadosUpload) {
        if(success) {
            log.error("Erro inesperado, removendo {} .", dadosUpload);
            removeObjectOnBucket(fileId, dadosUpload);
        }
    }

    public BucketFile getBucketFileFromDatabase(UUID fileId) {
        return bucketFileRepository.findById(fileId)
            .orElseThrow(() -> new EntityNotFoundException("Arquivo não encontrado com o id ".concat(String.valueOf(fileId)))
        );
    }

    public InputStream getFile(UUID bucketFileId) {
        log.info("Iniciando a busca do arquivo ".concat(bucketFileId.toString()));
        try {
            return client.getObject(
                GetObjectArgs
                    .builder()
                    .bucket(props.getBucketName())
                    .object(String.valueOf(getBucketFileFromDatabase(bucketFileId).getFileId()))
                    .build()
            );
        } catch (MinioException me) {
            throw new IllegalStateException(String.format("Erro ao buscar o arquivo %s no bucket %s", bucketFileId, props.getBucketName()), me.getCause());
        }
    }

    public E upload(UUID entityId, BucketFile file) {

        E entity = findById(entityId);

        if(Objects.nonNull(entity.getBucketFile())) {
            throw new IllegalStateException("Entidade já possui arquivo informado.");
        }

        entity.setBucketFile(uploadBucketFile(file));

        return getRepository().saveAndFlush(entity);
    }

    private BucketFile uploadBucketFile(BucketFile file) {
        if(Objects.nonNull(file) && Objects.isNull(file.getFileId())) {
            file.setFileId(UUID.randomUUID());
        }

        boolean successUpload = false;

        String dadosUpload = getDadosBucketFile(file);

        try {
            putObjectOnBucket(file, dadosUpload);

            successUpload = true;

            return bucketFileRepository.saveAndFlush(file);

        } catch (Exception ex) {
            removeEagerlyInsertedFileOnBucket(successUpload, file.getFileId(), dadosUpload);
            throw new IllegalStateException(String.format("Não foi possível realizar o Upload do Arquivo %s", file.getNome()), ex);
        }
    }

    public void removeBucketFile(UUID entityId, UUID fileId) {
        E entity = findById(entityId);

        BucketFile originalFile = getBucketFileFromDatabase(fileId);

        boolean successDelete = false;

        if (isNull(originalFile.getInputStream())) {
            originalFile.setInputStream(getFile(fileId));
        }

        String dadosRemove = getDadosBucketFile(originalFile);

        try {
            removeObjectOnBucket(originalFile.getFileId(), dadosRemove);

            successDelete = true;
            entity.setBucketFile(null);
            update(entity);

            bucketFileRepository.deleteById(originalFile.getId());

        } catch (Exception ex) {
            entity.setBucketFile(originalFile);
            update((E) originalFile);
            recoverDeletedFileOnBucket(successDelete, originalFile, dadosRemove);
            throw new IllegalStateException(String.format("Não foi possível realizar a remoção do Arquivo %s", originalFile.getNome()), ex);
        }
    }

    public E updateFile(UUID entityId, BucketFile file) throws IOException {

        E entity = findById(entityId);

        if(isNull(entity.getBucketFile())) {
            throw new IllegalStateException("Entidade não possui arquivo para atualizar");
        }

        file.setId(entity.getBucketFile().getId());
        updateBucketFile(file);

        return entity;
    }

    private BucketFile updateBucketFile(BucketFile file) throws IOException {

        BucketFile original = getBucketFileFromDatabase(file.getId());

        file.setFileId(UUID.randomUUID());

        String dadosRemove = getDadosBucketFile(original);
        String dadosUpload = getDadosBucketFile(file);

        boolean successDelete = false;
        boolean successUpload = false;

        byte[] originalFileContent;
        long originalSize = original.getSize();

        try (InputStream inputStream = getFile(original.getId())) {
            originalFileContent = inputStream.readAllBytes();
        }

        try  {
            putObjectOnBucket(file, dadosUpload);
            successUpload = true;

            removeObjectOnBucket(original.getFileId(), dadosRemove);
            successDelete = true;

            return bucketFileRepository.saveAndFlush(file);

        } catch (Exception ex) {
            // necessário setar aqui, pois durante a execução os valores são resetados indevidamente
            original = getBucketFileFromDatabase(file.getId());
            original.setInputStream(new ByteArrayInputStream(originalFileContent));
            original.setSize(originalSize);

            recoverDeletedFileOnBucket(successDelete, original, dadosRemove);
            removeEagerlyInsertedFileOnBucket(successUpload, file.getFileId(), dadosUpload);
            throw new IllegalStateException(String.format("Não foi possível atualizar o arquivo %s", original.getNome()), ex);
        }
    }
}
