package com.chvs.documentconsumer.core.model.document.impl.jdbc;

import com.chvs.documentconsumer.core.model.document.Document;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.chvs.documentconsumer.sdk.AppUtils.mapToList;

@Component
@RequiredArgsConstructor
class JdbcDocumentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final DocumentEntityRowMapper documentEntityRowMapper;

    List<Document> findAllByPacketId(@NonNull UUID packetId) {
        List<DocumentEntity> docs = jdbcTemplate.query(
                "SELECT * FROM documents d WHERE d.packet_id = ?",
                documentEntityRowMapper,
                packetId
        );

        return mapToList(docs, d -> d);
    }

    void saveAll(Collection<DocumentEntity> documents) {
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

    void updateAll(Collection<DocumentEntity> documents) {
        for (var document : documents) {
            jdbcTemplate.update(
                    """
                            UPDATE documents SET
                            title = ?,
                            type = ?,
                            state = ?,
                            body = ?,
                            version = ?
                            WHERE id = ?
                            """,
                    document.getTitle(),
                    document.getType(),
                    document.getState(),
                    document.getBody(),
                    document.getVersion(),
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

    List<DocumentEntity> findAllByDocumentIdIn(Collection<UUID> docIds) {
        var param = new MapSqlParameterSource("docIds", docIds);
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM documents d WHERE d.document_id IN (:docIds)",
                param,
                documentEntityRowMapper
        );
    }

    void updateVersion(Collection<UUID> ids) {
        var param = new MapSqlParameterSource("ids", ids);
        namedParameterJdbcTemplate.update(
                "UPDATE documents SET version = version + 1 WHERE id IN (:ids)",
                param
        );
    }

    List<DocumentEntity> findAllById(Collection<UUID> ids) {
        var param = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM documents d WHERE d.id IN (:ids)",
                param,
                documentEntityRowMapper
        );
    }
}
