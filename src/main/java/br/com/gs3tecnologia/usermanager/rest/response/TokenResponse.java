package br.com.gs3tecnologia.usermanager.rest.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
	private String token;
	private String name;
	private String username;
	private Boolean admin;
}
