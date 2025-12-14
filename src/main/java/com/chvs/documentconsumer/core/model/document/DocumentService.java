package com.chvs.documentconsumer.core.model.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.chvs.documentconsumer.sdk.AppUtils.groupToMap;
import static com.chvs.documentconsumer.sdk.AppUtils.mapToList;
import static com.chvs.documentconsumer.sdk.AppUtils.toMap;

@Service
@RequiredArgsConstructor
class DocumentService {

    private final DocumentRepository documentRepository;

    public Map<UUID, List<Document>> findAllWithRelatedByPacketsIdIn(Collection<UUID> packetIds) {
        var docs = documentRepository.findAllByPacketIdsIn(packetIds);

        return groupToMap(docs, Document::getPacketId);
    }

    public void createAll(Collection<DocumentCreation> documentsInfo) {
        var documents = mapToList(documentsInfo, Document::create);
        documentRepository.saveAll(documents);
    }

    public void updateAll(Collection<DocumentUpdate> documentsInfo) {
        var documentByDocId = toMap(documentsInfo, DocumentUpdate::documentId);
        List<Document> updatedDocs = new ArrayList<>();
        List<Document> docs = documentRepository.findAllByDocumentIdIn(documentByDocId.keySet());
        for (var doc : docs) {
            var docInfo = documentByDocId.get(doc.getDocumentId());
            doc.updateBy(docInfo);
            updatedDocs.add(doc);
        }
        documentRepository.updateAll(updatedDocs);
    }

    public void removeSoftByIds(Collection<UUID> ids) {
        documentRepository.removeSoftBy(ids);
    }

    public void updateVersionForAllBy(Collection<UUID> ids) {
        documentRepository.updateVersionBy(ids);
    }
}
