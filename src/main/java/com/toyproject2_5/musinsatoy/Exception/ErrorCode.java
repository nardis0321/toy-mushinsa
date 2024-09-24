package com.toyproject2_5.musinsatoy.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode implements EnumModel {

    // COMMON
    INVALID_CODE(400, "E001", "Invalid Code"),
    RESOURCE_NOT_FOUND(204, "E002", "Resource not found"),
    EXPIRED_CODE(400, "E003", "Expired Code"),
    PARAMETER_NOT_VALID(400, "E004", "Parameter not valid"),


    //Network
    NETWORK_ERROR_CODE(500, "N001", "Internal network Error"),

    // AWS
    AWS_ERROR(400, "A001", "aws client error");

    private int status;
    private String code;
    private String message;
    private String detail;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    @Override
    public String getKey() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.message;
    }
}