package com.toyproject2_5.musinsatoy.Exception.exceptionDto.customExceptionClass;

import lombok.Getter;

@Getter
public class DuplicateProductDescriptionIdException extends RuntimeException{
    private String message;

    public DuplicateProductDescriptionIdException(String message) {
        this.message = message;
    }
}
