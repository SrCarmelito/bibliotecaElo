package com.bibliotecaelo.repository;

import com.bibliotecaelo.domain.BucketFile;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BucketFileRepository extends RsqlRepository<BucketFile, UUID> {}