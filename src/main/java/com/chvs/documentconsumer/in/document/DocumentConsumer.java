package com.chvs.documentconsumer.in.document;

import com.chvs.documentconsumer.DocumentInfo;

import java.util.Collection;

public interface DocumentConsumer {

    void consume(Collection<DocumentInfo> documentsInfo);
}
