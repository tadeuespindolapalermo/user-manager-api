package br.com.gs3tecnologia.usermanager.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table(name = "TB_USER")
public class User implements UserDetails {

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
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(profile);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
