package com.chvs.documentconsumer.core.model.document.impl.jdbc;

import com.chvs.documentconsumer.core.model.document.Document;
import com.chvs.documentproducerapi.DocumentInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("documents")

@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Accessors(chain = true)
public class DocumentEntity implements Document {

    @Id
    private UUID id;

    private UUID documentId;
    private UUID packetId;
    private String title;
    private String type;
    private String state;
    private String body;
    private int version;
    private boolean removed;
    private UUID relatedDocId;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getDocumentId() {
        return documentId;
    }

    @Override
    public UUID getPacketId() {
        return packetId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    @Override
    public UUID getRelatedDocId() {
        return relatedDocId;
    }

    public static DocumentEntity create(DocumentInfo docInfo) {
        return new DocumentEntity()
                .setId(UUID.randomUUID())
                .setDocumentId(docInfo.documentId())
                .setPacketId(docInfo.packetId())
                .setType(docInfo.type())
                .setTitle(docInfo.title())
                .setState(docInfo.state())
                .setBody(docInfo.body())
                .setVersion(1);
    }

    @Override
    public void updateBy(DocumentInfo documentInfo) {
        this.packetId = documentInfo.packetId();
        this.type = documentInfo.type();
        this.title = documentInfo.title();
        this.state = documentInfo.state();
        this.body = documentInfo.body();
        updateVersion();
    }

    @Override
    public void updateVersion() {
        this.version += 1;
    }
}
