package br.com.gs3tecnologia.usermanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@Table(name = "TB_USER")
public class User implements Serializable {


	@Serial
    private static final long serialVersionUID = -5045643614971012521L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "E-mail is invalid")
    @NotEmpty(message = "E-mail is required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "Password is required")
    @Max(value = 8, message = "Password must contain 8 characters")
    @Min(value = 8, message = "Password must contain 8 characters")
    @Column(nullable = false, length = 8)
    private String password;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
