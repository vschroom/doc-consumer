package com.chvs.documentconsumer.core.model.document;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class DocumentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final DocumentRowMapper documentRowMapper;

    List<Document> findAllByPacketIdsIn(Collection<UUID> packetIds) {
        var param = new MapSqlParameterSource("packetIds", packetIds);
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM documents d WHERE d.packet_id IN (:packetIds)",
                param,
                documentRowMapper
        );
    }

    void saveAll(Collection<Document> documents) {
        for (var document : documents) {
            jdbcTemplate.update(
                    """
                            INSERT INTO
                            documents (id, document_id, packet_id, title, type, state, body, version, removed, related_doc_id)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """,
                    document.getId(),
                    document.getDocumentId(),
                    document.getPacketId(),
                    document.getTitle(),
                    document.getType(),
                    document.getState(),
                    document.getBody(),
                    document.getVersion(),
                    document.isRemoved(),
                    document.getRelatedDocId()
            );
        }
    }

    void updateAll(Collection<Document> documents) {
        for (var document : documents) {
            jdbcTemplate.update(
                    """
                            UPDATE documents SET
                            title = ?,
                            type = ?,
                            state = ?,
                            body = ?
                            WHERE id = ?
                            """,
                    document.getTitle(),
                    document.getType(),
                    document.getState(),
                    document.getBody(),
                    document.getId()
            );
        }
    }

    void removeSoftBy(Collection<UUID> ids) {
        var params = new MapSqlParameterSource(
                Map.of(
                        "removed", true,
                        "ids", ids
                )
        );
        namedParameterJdbcTemplate.update("UPDATE documents SET removed = :removed WHERE id IN (:ids)", params);
    }

    List<Document> findAllByDocumentIdIn(Collection<UUID> docIds) {
        var param = new MapSqlParameterSource("docIds", docIds);
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM documents d WHERE d.document_id IN (:docIds)",
                param,
                documentRowMapper
        );
    }

    void updateVersionBy(Collection<UUID> ids) {
        var param = new MapSqlParameterSource("ids", ids);
        namedParameterJdbcTemplate.update(
                "UPDATE documents SET version = version + 1 WHERE id IN (:ids)",
                param
        );
    }
}
