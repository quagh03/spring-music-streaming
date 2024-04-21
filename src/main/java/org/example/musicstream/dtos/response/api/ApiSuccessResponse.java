package org.example.musicstream.dtos.response.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiSuccessResponse<T> extends ApiBaseResponse {
    private T data;
    public ApiSuccessResponse(int code, String message, T data) {
        super(code, message, true);
        this.data = data;
    }
}
