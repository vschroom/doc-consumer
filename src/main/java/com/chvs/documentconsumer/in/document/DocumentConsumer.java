package com.chvs.documentconsumer.in.document;

import com.chvs.documentproducerapi.DocumentInfo;

import java.util.Collection;

public interface DocumentConsumer {

    void consume(Collection<DocumentInfo> documentsInfo);
}
