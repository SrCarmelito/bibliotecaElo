package com.bibliotecaelo.resource;

import com.bibliotecaelo.converter.BucketFileDTOConverter;
import com.bibliotecaelo.converter.DTOConverter;
import com.bibliotecaelo.dto.BucketFileDTO;
import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.interfaces.EntidadeDTO;
import com.bibliotecaelo.interfaces.EntidadeWithBucketFile;
import com.bibliotecaelo.service.CrudBucketService;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public abstract class CrudBucketResource<
    E extends Entidade & EntidadeWithBucketFile,
    D extends EntidadeDTO>
    extends CrudResource<E, D> {

    @Autowired
    protected CrudBucketService<E> crudBucketService;

    @Autowired
    protected DTOConverter<E, D> dtoConverter;

    @Autowired
    protected BucketFileDTOConverter bucketFileDTOConverter;

    @PostMapping("/file/upload/{entityId}")
    public ResponseEntity<D> upload(
        @PathVariable UUID entityId,
        @RequestBody MultipartFile multipartFile) {

        if(Objects.isNull(multipartFile)) {
            throw new IllegalArgumentException("Arquivo não pode ser nulo.");
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {

            BucketFileDTO dto = new BucketFileDTO();
            dto.setNome(multipartFile.getOriginalFilename());
            dto.setSize(multipartFile.getSize());
            dto.setInputStream(inputStream);
            dto.setContentType(multipartFile.getContentType());

            E entity = crudBucketService.upload(entityId, bucketFileDTOConverter.from(dto));

            return ResponseEntity.ok(dtoConverter.to(entity));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/file/remove/{entityId}/{bucketFileId}")
    public ResponseEntity<Void> removeFile(
        @PathVariable("entityId") UUID entityId,
        @PathVariable("bucketFileId") UUID bucketFileId) throws MinioException, IOException {

        crudBucketService.removeBucketFile(entityId, bucketFileId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/file/update/{entityId}")
    public ResponseEntity<D> updateFile(
        @PathVariable UUID entityId,
        @RequestBody MultipartFile multipartFile) {

        if(Objects.isNull(multipartFile)) {
            throw new IllegalArgumentException("Arquivo não pode ser nulo.");
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {

            BucketFileDTO dto = new BucketFileDTO();
            dto.setNome(multipartFile.getOriginalFilename());
            dto.setSize(multipartFile.getSize());
            dto.setInputStream(inputStream);
            dto.setContentType(multipartFile.getContentType());

            E entity = crudBucketService.updateFile(entityId, bucketFileDTOConverter.from(dto));

            return ResponseEntity.ok(dtoConverter.to(entity));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/file/{entityId}")
    public ResponseEntity<Resource> getFile(@PathVariable("entityId") UUID entityId) throws MinioException {
        InputStream inputStream = crudBucketService.getFile(entityId);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new InputStreamResource(inputStream));
    }

}
