package br.com.gs3tecnologia.usermanager.dto.input;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialsInputDTO {

    @NotEmpty(message = "Username is requires")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;
}
