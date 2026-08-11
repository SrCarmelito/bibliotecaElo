package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.BucketFile;
import com.bibliotecaelo.dto.BucketFileDTO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class BucketFileDTOConverter implements DTOConverter<BucketFile, BucketFileDTO> {

    @Override
    public BucketFile from(BucketFileDTO dto, BucketFile entity) {
        if (isNull(entity)) {
            entity = new BucketFile();
        }

        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setFileId(dto.getFileId());
        entity.setSize(dto.getSize());
        entity.setContentType(dto.getContentType());
        entity.setInputStream(dto.getInputStream());

        return entity;
    }

    @Override
    public BucketFileDTO to(BucketFile entity) {
        if (isNull(entity)) {
            return new BucketFileDTO();
        }

        BucketFileDTO dto = new BucketFileDTO();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setFileId(entity.getFileId());
        dto.setSize(entity.getSize());
        dto.setContentType(entity.getContentType());
        dto.setInputStream(entity.getInputStream());

        return dto;
    }

}
