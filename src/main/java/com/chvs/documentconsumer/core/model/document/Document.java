package com.chvs.documentconsumer.core.model.document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("documents")

@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Accessors(chain = true)
public class Document {

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

    public static Document create(DocumentCreation req) {
        return new Document()
                .setId(UUID.randomUUID())
                .setDocumentId(req.documentId())
                .setPacketId(req.packetId())
                .setType(req.type())
                .setTitle(req.title())
                .setState(req.state())
                .setBody(req.body())
                .setVersion(1);
    }

    public void updateBy(DocumentUpdate req) {
        this.packetId = req.packetId();
        this.type = req.type();
        this.title = req.title();
        this.state = req.state();
        this.body = req.body();
    }
}
