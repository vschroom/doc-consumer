package com.chvs.documentconsumer.core.model.document;

import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DocumentRowMapper implements RowMapper<Document> {

    @Override
    public Document mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return RowMapperUtils.getFrom(rs);
    }
}
