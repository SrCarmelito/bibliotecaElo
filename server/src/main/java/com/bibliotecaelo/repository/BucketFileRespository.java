package com.bibliotecaelo.repository;

import com.bibliotecaelo.domain.BucketFile;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BucketFileRespository extends RsqlRepository<BucketFile, UUID> {}