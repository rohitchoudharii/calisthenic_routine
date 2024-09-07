package com.calisthenics.routine.common

import java.util.stream.Collectors

fun sanitiseString(string: String): MutableList<String> =
    string.split("\n")
        .stream()
        .filter{ s -> s.trim().isNotEmpty() }
        .map { s -> s.trim() }
        .collect(Collectors.toList())