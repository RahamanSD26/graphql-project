package com.graphql.learn.exception.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseStatusDTO {
    private String uic;
    private String code ;
    private String message ;
    private String actualMessage ;
    private String environment;

    public ResponseStatusDTO(String uic, String code, String message, String actualMessage) {
        this.uic = uic;
        this.code = code;
        this.message = message;
        this.actualMessage = actualMessage;
    }

    public ResponseStatusDTO(String code, String message, String actualMessage) {
        this.code = code;
        this.message = message;
        this.actualMessage = actualMessage;
    }

    @Override
    public String toString() {
        return "{" + "uic='" + uic + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", actualMessage='" + actualMessage + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}
