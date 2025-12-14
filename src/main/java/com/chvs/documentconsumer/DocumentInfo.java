package com.chvs.documentconsumer;

import java.util.UUID;

public record DocumentInfo(UUID documentId, UUID packetId, String title, String type, String state, String body) {
}