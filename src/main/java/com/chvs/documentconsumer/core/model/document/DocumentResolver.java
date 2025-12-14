package com.chvs.documentconsumer.core.model.document;

import com.chvs.documentconsumer.DocumentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.chvs.documentconsumer.core.model.document.DocumentRelatedActionObserver.Action.VERSION_UP;
import static com.chvs.documentconsumer.sdk.AppUtils.acceptIfNotEmpty;
import static com.chvs.documentconsumer.sdk.AppUtils.acceptIfNotNull;
import static com.chvs.documentconsumer.sdk.AppUtils.groupToMap;
import static com.chvs.documentconsumer.sdk.AppUtils.toMap;

@Component
@RequiredArgsConstructor
public class DocumentResolver {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;
    private final DocumentRelatedActionObserver docRelatedActionObserver;

    @Transactional
    public void resolveDocuments(Collection<DocumentInfo> documentsInfo) {
        var incomeDocsByPacketId = groupToMap(documentsInfo, DocumentInfo::packetId);
        var existDocsByPacketId = documentService.findAllWithRelatedByPacketsIdIn(incomeDocsByPacketId.keySet());

        List<UUID> docIdsForRemove = new ArrayList<>();
        List<DocumentCreation> docForCreate = new ArrayList<>();
        List<DocumentUpdate> docForUpdate = new ArrayList<>();
        for (var documentsEntry : incomeDocsByPacketId.entrySet()) {
            var reqDocInfoByDocId = toMap(documentsEntry.getValue(), DocumentInfo::documentId);
            var existDocs = existDocsByPacketId.get(documentsEntry.getKey());
            for (var existDoc : existDocs) {
                var documentInfo = reqDocInfoByDocId.get(existDoc.getDocumentId());
                if (documentInfo == null) {
                    processForRemove(docIdsForRemove, existDoc);
                } else if (!docIdsForRemove.contains(existDoc.getId())) {
                    processForUpdate(docForUpdate, existDoc, documentInfo);
                    reqDocInfoByDocId.remove(documentInfo.documentId());
                }
            }

            var forCreate = documentMapper.toCreateReq(reqDocInfoByDocId.values());
            docForCreate.addAll(forCreate);
        }

        acceptIfNotEmpty(docForCreate, documentService::createAll);
        acceptIfNotEmpty(docIdsForRemove, documentService::removeSoftByIds);
        acceptIfNotEmpty(docForUpdate, documentService::updateAll);
        acceptIfNotEmpty(
                docRelatedActionObserver.getDocIdsByActionAndClear(VERSION_UP),
                documentService::updateVersionForAllBy
        );
    }

    private void processForRemove(List<UUID> docIdsForRemove, Document existDoc) {
        docIdsForRemove.add(existDoc.getId());
        acceptIfNotNull(existDoc.getRelatedDocId(), docIdsForRemove::add);

        addToVersionUpAction(existDoc);
    }

    private void processForUpdate(List<DocumentUpdate> docForUpdate, Document existDoc, DocumentInfo incomeDoc) {
        docForUpdate.add(documentMapper.toUpdateReq(incomeDoc));

        addToVersionUpAction(existDoc);
    }

    private void addToVersionUpAction(Document existDoc) {
        docRelatedActionObserver.addToAction(VERSION_UP, existDoc.getId());
        acceptIfNotNull(existDoc.getRelatedDocId(), id -> docRelatedActionObserver.addToAction(VERSION_UP, id));
    }
}
