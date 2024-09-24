package com.toyproject2_5.musinsatoy.Exception.exceptionDto.customExceptionClass;

import lombok.Getter;

@Getter
public class DuplicateProductIdException extends RuntimeException {

    private String message;

    public DuplicateProductIdException(String message) {
        this.message = message;
    }
}