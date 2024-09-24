package com.toyproject2_5.musinsatoy.Exception.exceptionDto;

import com.toyproject2_5.musinsatoy.Exception.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ExceptionResoponse {



        private String message;
        private String code;
        private int status;
        private String detail;

        public ExceptionResoponse(ErrorCode code) {
                this.message = code.getMessage();
                this.status = code.getStatus();
                this.code = code.getCode();
                this.detail = code.getDetail();
        }

        public static ExceptionResoponse of(ErrorCode  code) {
                return new ExceptionResoponse(code);
        }
}
