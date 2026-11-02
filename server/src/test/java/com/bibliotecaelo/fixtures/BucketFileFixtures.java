package com.bibliotecaelo.fixtures;

import com.bibliotecaelo.domain.BucketFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

public class BucketFileFixtures {

    public static BucketFile bucketFileFixture() {
        BucketFile bucketFile = new BucketFile();
        bucketFile.setId(UUID.fromString("463d8301-f4aa-4719-883e-326bca01c3dd"));
        bucketFile.setNome("Arquivo teste");
        bucketFile.setFileId(UUID.fromString("8b469685-1b83-4e5a-ad20-f8f6b0d48fbe"));
        bucketFile.setSize(512);
        bucketFile.setContentType("image/jpeg");
        bucketFile.setInputStream(new ByteArrayInputStream("ByteArrayTest".getBytes()));
        return bucketFile;
    }
}
