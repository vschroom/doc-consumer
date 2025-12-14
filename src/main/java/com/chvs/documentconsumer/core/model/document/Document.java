package com.chvs.documentconsumer.core.model.document;

import com.chvs.documentproducerapi.DocumentInfo;

import java.util.UUID;

public interface Document {

    UUID getId();

    UUID getDocumentId();

    UUID getPacketId();

    String getTitle();

    String getType();

    String getState();

    String getBody();

    int getVersion();

    boolean isRemoved();

    UUID getRelatedDocId();

    void updateBy(DocumentInfo documentInfo);

    void updateVersion();
}
