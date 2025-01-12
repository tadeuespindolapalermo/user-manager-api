package br.com.gs3tecnologia.usermanager.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private T data;
    private String message;
    private Boolean success;
}
