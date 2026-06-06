package com.thpiffer.myfin.core.dto;

public record PagingRequest(
    Integer page,
    Integer size,
    String filter
) {

    public PagingRequest {
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = 10;
        }
    }

    public static PagingRequest of(int page, int size) {
        return new PagingRequest(page, size, null);
    }

}
