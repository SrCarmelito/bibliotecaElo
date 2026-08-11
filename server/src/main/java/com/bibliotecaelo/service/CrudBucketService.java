package com.bibliotecaelo.service;

import com.bibliotecaelo.domain.BucketFile;
import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.interfaces.EntidadeWithBucketFile;
import com.bibliotecaelo.repository.BucketFileRespository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class CrudBucketService<E extends Entidade & EntidadeWithBucketFile> extends CrudService<E> {

    private static final String BIBLIOTECA_BUCKET_NAME = "biblioteca";

    @Autowired
    private MinioClient client;

    @Autowired
    private BucketFileRespository repository;

    private String getDadosBucketFile(BucketFile file) {
        return "Arquivo: " + file.getNome() + " Id: " + file.getFileId() + " Bucket: " + BIBLIOTECA_BUCKET_NAME;
    }

    private void putObjectOnBucket(BucketFile file, String dadosUpload) throws MinioException {
        log.info("Iniciando upload do arquivo {}.", dadosUpload);
        client.putObject(
            PutObjectArgs
                .builder()
                .bucket(BIBLIOTECA_BUCKET_NAME)
                .object(String.valueOf(file.getFileId()))
                .stream(file.getInputStream(), file.getSize(), -1L)
                .contentType(file.getContentType())
                .build()
        );
        log.info("Upload do arquivo finalizado {}.", dadosUpload);
    }

    private void removeObjectOnBucket(UUID fileID, String dadosRemove) throws MinioException {
        log.info("Iniciando remoção do arquivo {}.", dadosRemove);
        client.removeObject(
            RemoveObjectArgs
                .builder()
                .bucket(BIBLIOTECA_BUCKET_NAME)
                .object(String.valueOf(fileID))
                .build()
        );
        log.info("Remoção do arquivo finalizada {}.", dadosRemove);
    }

    private void recoverDeletedFileOnBucket(boolean success, BucketFile file, String dadosRemove) throws MinioException {
        if (success) {
            log.error("Erro inesperado, reinserindo {} .", dadosRemove);
            putObjectOnBucket(file, dadosRemove);
        }
    }

    private void removeEagerlyInsertedFileOnBucket(boolean success, UUID fileId, String dadosUpload) throws MinioException {
        if(success) {
            log.error("Erro inesperado, removendo {} .", dadosUpload);
            removeObjectOnBucket(fileId, dadosUpload);
        }
    }

    private BucketFile getBucketFileFromDatabase(UUID fileId) {
        return repository.findById(fileId)
            .orElseThrow(() -> new EntityNotFoundException("Arquivo não encontrado com o id ".concat(String.valueOf(fileId)))
        );
    }

    public InputStream getFile(UUID bucketFileId) throws MinioException {
        return client.getObject(
            GetObjectArgs
                .builder()
                .bucket(BIBLIOTECA_BUCKET_NAME)
                .object(String.valueOf(getBucketFileFromDatabase(bucketFileId).getFileId()))
                .build()
        );
    }

    public E upload(UUID entityId, BucketFile file) throws MinioException, IOException {

        E entity = findById(entityId);

        if(Objects.nonNull(entity.getBucketFile())) {
            throw new IllegalStateException("Entidade já possui arquivo informado.");
        }

        entity.setBucketFile(uploadBucketFile(file));

        return getRepository().saveAndFlush(entity);
    }

    public BucketFile uploadBucketFile(BucketFile file) throws MinioException, IOException {
        if(Objects.isNull(file.getFileId())) {
            file.setFileId(UUID.randomUUID());
        }

        boolean successUpload = false;

        String dadosUpload = getDadosBucketFile(file);

        try {
            putObjectOnBucket(file, dadosUpload);

            successUpload = true;

            return repository.saveAndFlush(file);

        } catch (Exception e) {
            removeEagerlyInsertedFileOnBucket(successUpload, file.getFileId(), dadosUpload);
            throw new RuntimeException(e);
        }
    }

    public void removeBucketFile(UUID entityId, UUID fileId) throws MinioException {
        E entity = findById(entityId);

        BucketFile file = getBucketFileFromDatabase(fileId);

        boolean successDelete = false;

        if (Objects.isNull(file.getInputStream())) {
            file.setInputStream(getFile(fileId));
        }

        String dadosRemove = getDadosBucketFile(file);

        try {
            removeObjectOnBucket(file.getFileId(), dadosRemove);

            successDelete = true;
            entity.setBucketFile(null);
            update(entity);

            repository.deleteById(file.getId());

        } catch (Exception e) {
            entity.setBucketFile(file);
            update(entity);
            recoverDeletedFileOnBucket(successDelete, file, dadosRemove);
            throw new RuntimeException(e);
        }
    }

    public BucketFile updateBucketFile(BucketFile file) throws MinioException {

        BucketFile bucketFileToUpdate = getBucketFileFromDatabase(file.getId());

        bucketFileToUpdate.setInputStream(getFile(bucketFileToUpdate.getId()));

        file.setFileId(UUID.randomUUID());

        String dadosRemove = getDadosBucketFile(bucketFileToUpdate);
        String dadosUpload = getDadosBucketFile(file);

        boolean successDelete = false;
        boolean successUpload = false;

        try {
            putObjectOnBucket(file, dadosUpload);
            successUpload = true;

            removeObjectOnBucket(bucketFileToUpdate.getFileId(), dadosRemove);
            successDelete = true;

            return repository.saveAndFlush(file);

        } catch (Exception e) {
            recoverDeletedFileOnBucket(successDelete, bucketFileToUpdate, dadosRemove);
            removeEagerlyInsertedFileOnBucket(successUpload, file.getFileId(), dadosUpload);
            throw new RuntimeException(e);
        }
    }

    public E updateFile(UUID entityId, BucketFile file) throws MinioException {

        E entity = findById(entityId);

        if(Objects.isNull(entity.getBucketFile())) {
            throw new IllegalStateException("Entidade não possui arquivo para atualizar");
        }

        file.setId(entity.getBucketFile().getId());
        updateBucketFile(file);

        return entity;
    }

}
