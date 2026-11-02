package com.bibliotecaelo.service;

import com.bibliotecaelo.configuration.MinioPropsConfiguration;
import com.bibliotecaelo.domain.BucketFile;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.fixtures.BucketFileFixtures;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.repository.BucketFileRepository;
import com.bibliotecaelo.repository.LivroRepository;
import io.minio.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrudBucketServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private BucketFileRepository bucketFileRepository;

    @Mock
    private MinioClient client;

    @Mock
    private MinioPropsConfiguration props;

    @Mock
    private LivroRepository livroRepository;

    @Test
    void getBucketFileFromDatabase() {
        BucketFile bucketFile = BucketFileFixtures.bucketFileFixture();

        when(bucketFileRepository.findById(bucketFile.getId()))
            .thenReturn(Optional.of(bucketFile));

        livroService.getBucketFileFromDatabase(bucketFile.getId());

        verify(bucketFileRepository)
            .findById(bucketFile.getId());

        verifyNoMoreInteractions(bucketFileRepository);
    }

    @Test
    void getBucketFileFromDatabaseArquivoNaoEncontrado() {
        UUID fileId = UUID.randomUUID();

        when(bucketFileRepository.findById(fileId))
            .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> livroService.getBucketFileFromDatabase(fileId)
        );

        assertEquals(
            "Arquivo não encontrado com o id " + fileId,
            exception.getMessage()
        );

        verify(bucketFileRepository).findById(fileId);
        verifyNoMoreInteractions(bucketFileRepository);
    }

    @Test
    @SneakyThrows
    void getFile() {
        BucketFile bucketFile = BucketFileFixtures.bucketFileFixture();
        GetObjectResponse response = mock(GetObjectResponse.class);

        when(props.getBucketName()).thenReturn("biblioteca");
        when(bucketFileRepository.findById(bucketFile.getId())).thenReturn(Optional.of(bucketFile));
        when(client.getObject(any(GetObjectArgs.class))).thenReturn(response);

        InputStream result = livroService.getFile(bucketFile.getId());

        assertThat(result).isEqualTo(response);
        verify(client).getObject(any());
        verify(bucketFileRepository).findById(bucketFile.getId());
        verifyNoMoreInteractions(client);
        verifyNoMoreInteractions(bucketFileRepository);
    }

    @Test
    void getFileArquivoNaoExiste() {
        UUID fileId = UUID.randomUUID();

        when(props.getBucketName()).thenReturn("biblioteca");
        when(bucketFileRepository.findById(fileId)).thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> livroService.getFile(fileId)
        );

        verifyNoInteractions(client);
    }

    @Test
    @SneakyThrows
    void upload() {
        BucketFile file = BucketFileFixtures.bucketFileFixture();
        Livro livro = LivroFixtures.LivroOProcesso();
        ObjectWriteResponse response = mock(ObjectWriteResponse .class);

        when(livroRepository.findById(livro.getId())).thenReturn(Optional.of(livro));
        when(props.getBucketName()).thenReturn("biblioteca");
        when(client.putObject(any(PutObjectArgs.class))).thenReturn(response);
        when(bucketFileRepository.saveAndFlush(file)).thenReturn(file);
        when(livroRepository.saveAndFlush(livro)).thenReturn(livro);

        Livro result = livroService.upload(livro.getId(), file);

        assertThat(result.getBucketFile()).isEqualTo(file);

        verify(livroRepository).findById(livro.getId());
        verify(props, times(2)).getBucketName();
        verify(client).putObject(any());
        verify(bucketFileRepository).saveAndFlush(file);
        verify(livroRepository).saveAndFlush(livro);

        verifyNoMoreInteractions(livroRepository);
        verifyNoMoreInteractions(props);
        verifyNoMoreInteractions(client);
        verifyNoMoreInteractions(bucketFileRepository);
        verifyNoMoreInteractions(livroRepository);
    }

    @Test
    void naoDeveFazerUploadQuandoEntidadeJaPossuiArquivo() {
        UUID entityId = UUID.randomUUID();

        Livro livro = LivroFixtures.LivroOProcesso();
        livro.setId(entityId);
        livro.setBucketFile(BucketFileFixtures.bucketFileFixture());

        BucketFile novoArquivo = BucketFileFixtures.bucketFileFixture();

        when(livroRepository.findById(entityId))
            .thenReturn(Optional.of(livro));

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> livroService.upload(entityId, novoArquivo)
        );

        assertEquals(
            "Entidade já possui arquivo informado.",
            exception.getMessage()
        );

        verify(bucketFileRepository, never()).saveAndFlush(any(BucketFile.class));
        verify(livroRepository, never()).saveAndFlush(any(Livro.class));
        verifyNoInteractions(client);
    }

    @Test
    @SneakyThrows
    void removeBucketFile() {
        BucketFile file = BucketFileFixtures.bucketFileFixture();
        Livro livro = LivroFixtures.LivroOProcesso();

        livro.setBucketFile(file);

        when(livroRepository.findById(livro.getId())).thenReturn(Optional.of(livro));
        when(bucketFileRepository.findById(file.getId())).thenReturn(Optional.of(file));
        when(props.getBucketName()).thenReturn("biblioteca");

        livroService.removeBucketFile(livro.getId(), file.getId());

        assertThat(livro.getBucketFile()).isEqualTo(null);

        verify(livroRepository).findById(livro.getId());
        verify(bucketFileRepository).findById(file.getId());
        verify(props, times(2)).getBucketName();
    }

    @Test
    @SneakyThrows
    void updateFile() {
        BucketFile file = BucketFileFixtures.bucketFileFixture();
        BucketFile fileUpdate = file;
        Livro livro = LivroFixtures.LivroOProcesso();

        livro.setBucketFile(file);

        fileUpdate.setNome("Arquivo atualizado");
        fileUpdate.setContentType("text/xml");
        fileUpdate.setInputStream(new ByteArrayInputStream("Arquivo atualizado".getBytes()));

        when(livroRepository.findById(livro.getId())).thenReturn(Optional.of(livro));
        when(bucketFileRepository.findById(file.getId())).thenReturn(Optional.of(file));
        when(client.getObject(any(GetObjectArgs.class))).thenReturn(mock(GetObjectResponse.class));
        when(props.getBucketName()).thenReturn("biblioteca");
        when(client.putObject(any(PutObjectArgs.class))).thenReturn(mock(ObjectWriteResponse.class));
        when(bucketFileRepository.saveAndFlush(fileUpdate)).thenReturn(fileUpdate);

        Livro livroUpdated = livroService.updateFile(livro.getId(), fileUpdate);

        assertThat(livroUpdated).isEqualTo(livro);
    }

}