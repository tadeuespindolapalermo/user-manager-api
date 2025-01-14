package br.com.gs3tecnologia.usermanager.domain.service;

import br.com.gs3tecnologia.usermanager.dto.input.AssignmentProfileInputDTO;
import br.com.gs3tecnologia.usermanager.dto.output.AssignmentProfileOutputDTO;
import br.com.gs3tecnologia.usermanager.exception.BusinessException;
import br.com.gs3tecnologia.usermanager.domain.model.Profile;
import br.com.gs3tecnologia.usermanager.domain.model.User;
import br.com.gs3tecnologia.usermanager.domain.repository.UserRepository;
import br.com.gs3tecnologia.usermanager.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.gs3tecnologia.usermanager.util.Constants.PROFILE_ADMIN;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ProfileService profileService;

    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        /**
         * Non-admin user will only see their own information
         */
        String profileCurrentUser = getProfileCurrentUser();
        if (!profileCurrentUser.equals(PROFILE_ADMIN)) {
            users = users.stream().filter(user -> user.getEmail().equals(getUsernameCurrentUser())).collect(Collectors.toList());
        }

        return users;
    }

    public User register(User user) throws BusinessException {
        if (isNull(user.getId())) {
            return create(user);
        }
        return update(user);
    }

    public User create(User user) throws BusinessException {
        User userFound = userRepository.findByEmail(user.getEmail());
        if (nonNull(userFound)) {
            throw new BusinessException("User already exists with the email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User user) throws BusinessException {
        User userFoundById = findById(user.getId());
        User userFoundByEmail = userRepository.findByEmail(user.getEmail());

        if (nonNull(userFoundByEmail) && !userFoundById.getId().equals(userFoundByEmail.getId())) {
            throw new BusinessException("The email provided already exists for another user.");
        }

        userFoundById.setName(user.getName());
        userFoundById.setEmail(user.getEmail());
        userFoundById.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(userFoundById);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity User not found with ID: " + id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public AssignmentProfileOutputDTO assignProfile(AssignmentProfileInputDTO assignmentProfileInputDTO) throws BusinessException {
        Profile profile = profileService.findById(assignmentProfileInputDTO.getProfileId());

        User user = findById(assignmentProfileInputDTO.getUserId());
        user.setProfile(profile);

        userRepository.save(user);

        return AssignmentProfileOutputDTO.builder()
            .name(user.getName())
            .profile(profile.getName())
            .build();
    }

    public String getProfileCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return ((Profile)((List<?>) authentication.getAuthorities()).getFirst()).getName();
        }
        return null;
    }

    public String getUsernameCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

}
