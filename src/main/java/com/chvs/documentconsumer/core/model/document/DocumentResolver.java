package com.chvs.documentconsumer.core.model.document;

import com.chvs.documentproducerapi.DocumentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.chvs.documentconsumer.sdk.AppUtils.acceptIfNotEmpty;
import static com.chvs.documentconsumer.sdk.AppUtils.groupToMap;
import static com.chvs.documentconsumer.sdk.AppUtils.toMap;

@Component
@RequiredArgsConstructor
public class DocumentResolver {

    private final DocumentService documentService;

    public void resolveDocuments(Collection<DocumentInfo> documentsInfo) {
        var documentsByPacketId = groupToMap(documentsInfo, DocumentInfo::packetId);

        List<UUID> docIdsToRemove = new ArrayList<>();
        List<DocumentInfo> docInfoToCreate = new ArrayList<>();
        List<DocumentInfo> docInfoToUpdate = new ArrayList<>();
        for (var documentsEntry : documentsByPacketId.entrySet()) {
            var reqDocInfoByDocId = toMap(documentsEntry.getValue(), DocumentInfo::documentId);
            var documents = documentService.findAllByPacketId(documentsEntry.getKey());
            for (var doc : documents) {
                var documentInfo = reqDocInfoByDocId.get(doc.getDocumentId());
                if (documentInfo == null) {
                    docIdsToRemove.add(doc.getId());
                } else {
                    docInfoToUpdate.add(documentInfo);
                    reqDocInfoByDocId.remove(documentInfo.documentId());
                }
            }

            docInfoToCreate.addAll(reqDocInfoByDocId.values());
        }

        acceptIfNotEmpty(docInfoToCreate, documentService::createAll);
        acceptIfNotEmpty(docInfoToUpdate, documentService::updateAllAndUpVersion);
        acceptIfNotEmpty(docIdsToRemove, documentService::removeSoftByIds);
    }
}
