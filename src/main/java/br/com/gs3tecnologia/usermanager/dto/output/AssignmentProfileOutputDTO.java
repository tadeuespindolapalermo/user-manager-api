package br.com.gs3tecnologia.usermanager.dto.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentProfileOutputDTO {
    private String name;
    private String profile;
}
