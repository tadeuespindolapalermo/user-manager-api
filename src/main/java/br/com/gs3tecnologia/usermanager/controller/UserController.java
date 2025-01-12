package br.com.gs3tecnologia.usermanager.controller;

import br.com.gs3tecnologia.usermanager.dto.input.AssignmentProfileInputDTO;
import br.com.gs3tecnologia.usermanager.dto.output.AssignmentProfileOutputDTO;
import br.com.gs3tecnologia.usermanager.dto.output.ResponseDTO;
import br.com.gs3tecnologia.usermanager.exception.BusinessException;
import br.com.gs3tecnologia.usermanager.model.User;
import br.com.gs3tecnologia.usermanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<User>>> getAll() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(new ResponseDTO<>(users, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<User>> register(@Valid @RequestBody User user) {
        User userRegistered;
        try {
            userRegistered = userService.register(user);
        } catch (BusinessException exception) {
            return new ResponseEntity<>(new ResponseDTO<>(null, exception.getMessage(), Boolean.FALSE), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO<>(userRegistered, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<User>> getById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(new ResponseDTO<>(user, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseDTO<>(null, exception.getMessage(), Boolean.FALSE), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(new ResponseDTO<>("User deleted", StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
    }

    @PostMapping("/assign-profile")
    public ResponseEntity<ResponseDTO<AssignmentProfileOutputDTO>> assignProfile(@Valid @RequestBody AssignmentProfileInputDTO assignmentProfileInputDTO) {
        try {
            AssignmentProfileOutputDTO output = userService.assignProfile(assignmentProfileInputDTO);
            return new ResponseEntity<>(new ResponseDTO<>(output, StringUtils.EMPTY, Boolean.TRUE), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseDTO<>(null, exception.getMessage(), Boolean.FALSE), HttpStatus.OK);
        }
    }

}
