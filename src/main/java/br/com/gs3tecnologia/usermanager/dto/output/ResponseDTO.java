package br.com.gs3tecnologia.usermanager.dto.output;

import br.com.gs3tecnologia.usermanager.rest.response.ResponseErrorBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {

    private T data;
    private String message;
    private Boolean success;

    public void addErrorBodyMsgToResponse(String msgError) {
        ResponseErrorBody error = new ResponseErrorBody(msgError);
        message = error.getMessage();
    }
}
