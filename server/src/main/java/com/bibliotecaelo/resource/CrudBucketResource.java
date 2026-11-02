package com.bibliotecaelo.resource;

import com.bibliotecaelo.converter.BucketFileDTOConverter;
import com.bibliotecaelo.domain.BucketFile;
import com.bibliotecaelo.dto.BucketFileDTO;
import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.interfaces.EntidadeDTO;
import com.bibliotecaelo.interfaces.EntidadeWithBucketFile;
import com.bibliotecaelo.service.CrudBucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.InvalidMediaTypeException;
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
    protected BucketFileDTOConverter bucketFileDTOConverter;
    
    protected CrudBucketService<E> getCrudBucketService() {
        return (CrudBucketService<E>) this.crudService;
    };

    @PostMapping("/file/upload/{entityId}")
    public ResponseEntity<D> upload(
        @PathVariable("entityId") UUID entityId,
        @RequestParam("multipartFile") MultipartFile multipartFile) {

        isValidContentType(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {

            BucketFileDTO dto = new BucketFileDTO();
            dto.setNome(multipartFile.getOriginalFilename());
            dto.setSize(multipartFile.getSize());
            dto.setInputStream(inputStream);
            dto.setContentType(multipartFile.getContentType());

            E entity = getCrudBucketService().upload(entityId, bucketFileDTOConverter.from(dto));

            return ResponseEntity.ok(dtoConverter.to(entity));
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível realizar o upload do arquivo", ex);
        }
    }

    @DeleteMapping("/file/remove/{entityId}/{bucketFileId}")
    public ResponseEntity<Void> removeFile(
        @PathVariable("entityId") UUID entityId,
        @PathVariable("bucketFileId") UUID bucketFileId) {

        getCrudBucketService().removeBucketFile(entityId, bucketFileId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/file/update/{entityId}")
    public ResponseEntity<D> updateFile(
        @PathVariable("entityId") UUID entityId,
        @RequestParam("multipartFile") MultipartFile multipartFile) {

        isValidContentType(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {

            BucketFileDTO dto = new BucketFileDTO();
            dto.setNome(multipartFile.getOriginalFilename());
            dto.setSize(multipartFile.getSize());
            dto.setInputStream(inputStream);
            dto.setContentType(multipartFile.getContentType());

            E entity = getCrudBucketService().updateFile(entityId, bucketFileDTOConverter.from(dto));

            return ResponseEntity.ok(dtoConverter.to(entity));

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível atualizar o arquivo", e);
        }
    }

    @GetMapping("/file/{bucketFileId}")
    public ResponseEntity<Resource> getFile(@PathVariable("bucketFileId") UUID bucketFileId) {
        BucketFile bucketFile = getCrudBucketService().getBucketFileFromDatabase(bucketFileId);
        bucketFile.setInputStream(getCrudBucketService().getFile(bucketFile.getId()));

        return ResponseEntity.ok()
            .contentType(MediaType.valueOf(bucketFile.getContentType()))
            .body(new InputStreamResource(bucketFile.getInputStream()));
    }

    public void isValidContentType(MultipartFile multipartFile) {
        if(Objects.isNull(multipartFile)) {
            throw new IllegalArgumentException("Arquivo não pode ser nulo.");
        }

        try {
            MediaType.parseMediaType(Objects.requireNonNull(multipartFile.getContentType()));
        } catch (InvalidMediaTypeException e) {
            throw new IllegalArgumentException("Tipo de arquivo é inválido");
        }
    }

}
