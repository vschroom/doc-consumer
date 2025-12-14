package com.chvs.documentconsumer.core.model.document;

import com.chvs.documentproducerapi.DocumentInfo;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DocumentService {

    List<Document> findAllByPacketId(@NonNull UUID packetId);

    void createAll(Collection<DocumentInfo> documentsInfo);

    void updateAllAndUpVersion(Collection<DocumentInfo> documentsInfo);

    void removeSoftByIds(Collection<UUID> ids);
}
