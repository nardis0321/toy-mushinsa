package com.toyproject2_5.musinsatoy.ETC.util.pagination;

/**
 * Cursor-based pagination request object
 * @param key Make sure this is unique and sortable
 * @param size The number of items to request
 * @param <T> The object to be displayed with pagination
 */
public record CursorRequest<T>(T key, Integer size) {

    public CursorRequest {
        if(size == null || size == 0){
            size = 10;
        }
    }

    public boolean hasKey(){
        return key != null;
    }

    public CursorRequest<T> next(T key){
        return new CursorRequest<>(key, size);
    }
}
