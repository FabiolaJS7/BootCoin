package com.bootcoin.transaction.bootcoin.api.util;

import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberRandomUtil {

    private static final int ORIGIN_SERIES_RAMD_LENGTH = 1000000;
    private static final int END_SERIES_RAMD_LENGTH = 9999999;

    private static final int RANGE_0 = 0;
    private static final int QUANTITY_RANGE = 3;


    public static Mono<String> generateOrderAccount() {
        return Mono.fromSupplier(() -> IntStream.range(RANGE_0, QUANTITY_RANGE) // Genera 4 series
                .mapToObj(i -> String.valueOf(ThreadLocalRandom.current().nextInt(ORIGIN_SERIES_RAMD_LENGTH, END_SERIES_RAMD_LENGTH))) // cada serie con 4 dígitos randoms
                .collect(Collectors.joining("-"))); // Uniendo las series con guiones
    }
}
