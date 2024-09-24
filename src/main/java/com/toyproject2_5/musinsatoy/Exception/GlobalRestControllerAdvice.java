package com.toyproject2_5.musinsatoy.Exception;


import com.toyproject2_5.musinsatoy.Exception.exceptionDto.ExceptionResoponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestControllerAdvice
public class GlobalRestControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalRestControllerAdvice.class);

    //@Valid 애너테이션으로 받은거.
    //ExceptionDto 사용해서 반환하도록. -- 사용자에게 예외 시간을 보여줄 필요가 있나?
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

//        StringBuilder builder = new StringBuilder();
        List<ExceptionResoponse> exceptionResoponses = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
//            builder//append(fieldError.getField())
//                    .append(fieldError.getDefaultMessage())
//                    .append(" 입력된 값: ")
//                    .append(fieldError.getRejectedValue());
            ExceptionResoponse resoponse = ExceptionResoponse.of(ErrorCode.PARAMETER_NOT_VALID);
            resoponse.setMessage(fieldError.getDefaultMessage());

            exceptionResoponses.add(resoponse);
        }

        return ResponseEntity.badRequest().body(exceptionResoponses);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<?> processConnectException(ConnectException exception) {
        ExceptionResoponse resoponse =ExceptionResoponse.of(ErrorCode.NETWORK_ERROR_CODE);
        resoponse.setMessage("Internal Server Error");

        System.out.println(Arrays.toString(exception.getStackTrace()));
        System.out.println(exception.getMessage());

        return ResponseEntity.internalServerError().body(resoponse);
    }

}