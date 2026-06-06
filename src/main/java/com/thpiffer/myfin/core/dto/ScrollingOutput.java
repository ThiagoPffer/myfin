package com.thpiffer.myfin.core.dto;

import java.util.List;

public record ScrollingOutput<T>(
    boolean hasNext,
    List<T> content
) {}
