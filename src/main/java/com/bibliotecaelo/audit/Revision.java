package com.bibliotecaelo.audit;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Table(name = "revision", schema = "biblioteca")
@Data
@Entity
@RevisionEntity(EnversListener.class)
public class Revision {

    @Id
    @RevisionNumber
    @SequenceGenerator(name = "seq_revision", schema = "biblioteca", sequenceName = "seq_revision", allocationSize=1)
    @GeneratedValue(generator = "seq_revision")
    @Column(name = "revisionnumber")
    private Long revisionNumber;

    @RevisionTimestamp
    @Column(name = "revisiondate")
    private Date revisionDate;

    @Column(name = "username")
    private String userName;

    @Column(name = "remoteipaddress", length = 1000)
    private String remoteIpAddress;

    @Column(name = "userid")
    @NotNull
    private UUID userId;

    @Column(name = "login")
    private String login;

}

