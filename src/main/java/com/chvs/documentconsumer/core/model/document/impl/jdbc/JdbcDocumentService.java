package com.chvs.documentconsumer.core.model.document.impl.jdbc;

import com.chvs.documentconsumer.core.model.document.Document;
import com.chvs.documentconsumer.core.model.document.DocumentService;
import com.chvs.documentproducerapi.DocumentInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.chvs.documentconsumer.sdk.AppUtils.mapToList;
import static com.chvs.documentconsumer.sdk.AppUtils.mapToSet;
import static com.chvs.documentconsumer.sdk.AppUtils.toMap;

@Service
@RequiredArgsConstructor
class JdbcDocumentService implements DocumentService {

    private final JdbcDocumentRepository documentRepository;

    @Override
    public List<Document> findAllByPacketId(@NonNull UUID packetId) {
        return documentRepository.findAllByPacketId(packetId);
    }

    @Override
    public void createAll(Collection<DocumentInfo> documentsInfo) {
        var documents = mapToList(documentsInfo, DocumentEntity::create);
        documentRepository.saveAll(documents);
    }

    @Override
    public void updateAllAndUpVersion(Collection<DocumentInfo> documentsInfo) {
        var documentByDocId = toMap(documentsInfo, DocumentInfo::documentId);
        List<DocumentEntity> updatedDocs = new ArrayList<>();
        for (var document : documentRepository.findAllByDocumentIdIn(documentByDocId.keySet())) {
            var documentInfo = documentByDocId.get(document.getDocumentId());
            document.updateBy(documentInfo);
            updatedDocs.add(document);
        }
        documentRepository.updateAll(updatedDocs);
        updateRelatedDocVersions(updatedDocs);
    }

    public void removeSoftByIds(Collection<UUID> ids) {
        documentRepository.removeSoftBy(ids);
        removeRelatedDocs(documentRepository.findAllById(ids));
    }

    private void updateRelatedDocVersions(Collection<DocumentEntity> documents) {
        var relatedDocIds = getRelatedIds(documents);
        documentRepository.updateVersion(relatedDocIds);
    }

    private void removeRelatedDocs(Collection<DocumentEntity> documents) {
        var relatedDocIds = getRelatedIds(documents);
        documentRepository.removeSoftBy(relatedDocIds);
    }

    private List<UUID> getRelatedIds(Collection<DocumentEntity> documents) {
        var mainDocIds = mapToList(documents, Document::getId);
        var relatedDocIds = mapToSet(documents, Document::getRelatedDocId);
        relatedDocIds.removeIf(mainDocIds::contains);

        return mainDocIds;
    }
}
