package br.com.gs3tecnologia.usermanager.rest.controller;

import br.com.gs3tecnologia.usermanager.dto.output.ResponseDTO;
import br.com.gs3tecnologia.usermanager.exception.BusinessException;
import br.com.gs3tecnologia.usermanager.domain.model.Profile;
import br.com.gs3tecnologia.usermanager.domain.service.ProfileService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO<List<Profile>>> getAll() {
        List<Profile> profiles = profileService.findAll();
        return new ResponseEntity<>(new ResponseDTO<>(profiles, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO<Profile>> register(@Valid @RequestBody Profile profile) {
        Profile profileCreated;
        try {
            profileCreated = profileService.register(profile);
        } catch (BusinessException exception) {
            return new ResponseEntity<>(new ResponseDTO<>(null, exception.getMessage(), Boolean.FALSE), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO<>(profileCreated, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO<Profile>> getById(@PathVariable Long id) {
        try {
            Profile profile = profileService.findById(id);
            return new ResponseEntity<>(new ResponseDTO<>(profile, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseDTO<>(null, exception.getMessage(), Boolean.FALSE), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        try {
            profileService.deleteById(id);
        } catch (BusinessException exception) {
            return new ResponseEntity<>(new ResponseDTO<>("Error removing profile", exception.getMessage(), Boolean.FALSE), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO<>("Profile deleted", StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
    }

}
