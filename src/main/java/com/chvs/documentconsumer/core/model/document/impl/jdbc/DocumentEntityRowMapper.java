package com.chvs.documentconsumer.core.model.document.impl.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class DocumentEntityRowMapper implements RowMapper<DocumentEntity> {

    @Override
    public DocumentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DocumentEntity()
                .setId(rs.getObject("id", UUID.class))
                .setDocumentId(rs.getObject("document_id", UUID.class))
                .setPacketId(rs.getObject("packet_id", UUID.class))
                .setTitle(rs.getString("title"))
                .setType(rs.getString("type"))
                .setState(rs.getString("state"))
                .setBody(rs.getString("body"))
                .setVersion(rs.getInt("version"))
                .setRemoved(rs.getBoolean("removed"))
                .setRelatedDocId(rs.getObject("related_doc_id", UUID.class));
    }
}
