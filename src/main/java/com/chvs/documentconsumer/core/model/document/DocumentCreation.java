package com.chvs.documentconsumer.core.model.document;

import java.util.UUID;

public record DocumentCreation(
        UUID documentId,
        UUID packetId,
        String title,
        String type,
        String state,
        String body
) {
}
