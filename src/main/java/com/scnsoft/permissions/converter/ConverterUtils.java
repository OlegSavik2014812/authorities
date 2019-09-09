package com.scnsoft.permissions.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ConverterUtils {
    private ConverterUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T, K> List<T> transform(List<K> source, Function<K, T> mapper) {
        return batchTransform(source, list -> list.stream().map(mapper).collect(Collectors.toList()));
    }

    public static <T, K> List<T> batchTransform(List<K> source, Function<List<K>, List<T>> mapper) {
        return Optional.ofNullable(source)
                .filter(list -> !list.isEmpty())
                .map(mapper)
                .orElse(Collections.emptyList());
    }
}
