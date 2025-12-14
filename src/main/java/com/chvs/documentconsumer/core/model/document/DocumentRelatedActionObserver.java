package com.chvs.documentconsumer.core.model.document;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.chvs.documentconsumer.sdk.AppUtils.acceptIfNotNull;

@Component
public class DocumentRelatedActionObserver {

    private final Map<Action, Set<UUID>> docIdsByAction = Map.of(
            Action.VERSION_UP, new HashSet<>()
    );

    public void addToAction(Action action, UUID docId) {
        acceptIfNotNull(docIdsByAction.get(action), ids -> ids.add(docId));
    }

    public Set<UUID> getDocIdsByActionAndClear(Action action) {
        var ids = Set.copyOf(docIdsByAction.get(action));
        clearObserver(action);

        return ids;
    }

    private void clearObserver(Action action) {
        acceptIfNotNull(docIdsByAction.get(action), Set::clear);
    }

    public enum Action {

        VERSION_UP
    }
}
