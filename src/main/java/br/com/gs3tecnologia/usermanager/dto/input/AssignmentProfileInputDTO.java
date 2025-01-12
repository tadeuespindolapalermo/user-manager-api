package br.com.gs3tecnologia.usermanager.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignmentProfileInputDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Profile ID is required")
    private Long profileId;
}
