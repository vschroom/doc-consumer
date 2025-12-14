package com.chvs.documentconsumer.core.model.document;

import com.chvs.documentconsumer.DocumentInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static com.chvs.documentconsumer.sdk.AppUtils.mapToList;

@Component
public class DocumentMapper {

    public DocumentCreation toCreateReq(DocumentInfo docInfo) {
        return new DocumentCreation(
                docInfo.documentId(),
                docInfo.packetId(),
                docInfo.title(),
                docInfo.type(),
                docInfo.state(),
                docInfo.body()
        );
    }

    public DocumentUpdate toUpdateReq(DocumentInfo docInfo) {
        return new DocumentUpdate(
                docInfo.documentId(),
                docInfo.packetId(),
                docInfo.title(),
                docInfo.type(),
                docInfo.state(),
                docInfo.body()
        );
    }

    public List<DocumentCreation> toCreateReq(Collection<DocumentInfo> docsInfo) {
        return mapToList(docsInfo, this::toCreateReq);
    }
}
