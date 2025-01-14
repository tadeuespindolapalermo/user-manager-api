package br.com.gs3tecnologia.usermanager.rest.response;

import lombok.Getter;

@Getter
public enum ResponseErrorEnum {

	BODY_ERROR("body_error"),
	BUSINESS_ERROR("business_error"),
	INTERNAL_ERROR("internal_error");
	
	private final String descricao;
	
	ResponseErrorEnum(String descricao) {
		this.descricao = descricao;
	}

}
