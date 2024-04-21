package org.example.musicstream.dtos.response.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiFailResponse extends ApiBaseResponse{
    public ApiFailResponse(int code, String message) {
        super(code, message, false);
    }
}
