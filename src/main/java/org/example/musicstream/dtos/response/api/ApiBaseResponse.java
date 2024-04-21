package org.example.musicstream.dtos.response.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ApiBaseResponse {
    private int code;
    private String message;
    private boolean success;
}
