package br.com.gs3tecnologia.usermanager.rest.response;

import lombok.Getter;

@Getter
public class ResponseErrorBody implements IResponseError {

	private final String type;
	private final String message;
	
	public ResponseErrorBody(String message) {
		this.type = ResponseErrorEnum.BODY_ERROR.getDescricao();
		this.message = message;
	}
	
}
