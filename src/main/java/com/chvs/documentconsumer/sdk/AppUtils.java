package com.chvs.documentconsumer.sdk;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("java:S6204")
@UtilityClass
public class AppUtils {

    public static <T, R> List<R> mapToList(Collection<T> collection, Function<T, R> func) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }

        return collection
                .stream()
                .map(func)
                .collect(Collectors.toList());
    }

    public static <T, R> Map<R, List<T>> groupToMap(Collection<T> collection, Function<T, R> func) {
        if (collection == null || collection.isEmpty()) {
            return new HashMap<>();
        }

        return collection
                .stream()
                .collect(Collectors.groupingBy(func));
    }

    public static <T, R> Map<R, T> toMap(Collection<T> collection, Function<T, R> func) {
        if (collection == null || collection.isEmpty()) {
            return new HashMap<>();
        }

        return collection
                .stream()
                .collect(Collectors.toMap(func, Function.identity()));
    }

    public static <T> void acceptIfNotEmpty(Collection<T> coll, Consumer<Collection<T>> consumer) {
        if (coll != null && !coll.isEmpty()) {
            consumer.accept(coll);
        }
    }

    public static <T> void acceptIfNotNull(T coll, Consumer<T> consumer) {
        if (coll != null) {
            consumer.accept(coll);
        }
    }
}
