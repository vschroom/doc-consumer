package com.chvs.documentconsumer.core.model.document;

import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@UtilityClass
public class RowMapperUtils {

    public Document getFrom(ResultSet rs) throws SQLException {
        return new Document()
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
