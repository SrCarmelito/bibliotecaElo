package com.bibliotecaelo.audit;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RevisionTest {

    @Test
    void revisionTest() {
        Revision revision = new Revision();
        revision.setRevisionNumber(1L);
        revision.setRevisionDate(new Date(2022, 11, 11));
        revision.setLogin("junior");
        revision.setUserName("Junior");
        revision.setRemoteIpAddress("0.0.0.0.0.1");
        revision.setUserId(UUID.fromString("ca7415e2-ddf7-4cca-82c5-7b63e4426c5e"));
        assertEquals(1, revision.getRevisionNumber());
        assertTrue(revision.getRevisionDate().after(Date.from(Instant.now())));
        assertEquals("junior", revision.getLogin());
        assertEquals("Junior", revision.getUserName());
        assertEquals("0.0.0.0.0.1", revision.getRemoteIpAddress());
        assertEquals(UUID.fromString("ca7415e2-ddf7-4cca-82c5-7b63e4426c5e"), revision.getUserId());
    }
}
