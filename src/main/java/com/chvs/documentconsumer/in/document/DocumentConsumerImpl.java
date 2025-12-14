package com.chvs.documentconsumer.in.document;

import com.chvs.documentconsumer.core.model.document.DocumentResolver;
import com.chvs.documentproducerapi.DocumentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
class DocumentConsumerImpl implements DocumentConsumer {

    private final DocumentResolver documentResolver;

    @Override
    public void consume(Collection<DocumentInfo> documentsInfo) {
        documentResolver.resolveDocuments(documentsInfo);
    }
}
