package br.com.gs3tecnologia.usermanager.service;

import br.com.gs3tecnologia.usermanager.dto.input.AssignmentProfileInputDTO;
import br.com.gs3tecnologia.usermanager.dto.output.AssignmentProfileOutputDTO;
import br.com.gs3tecnologia.usermanager.exception.BusinessException;
import br.com.gs3tecnologia.usermanager.model.Profile;
import br.com.gs3tecnologia.usermanager.model.User;
import br.com.gs3tecnologia.usermanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ProfileService profileService;

    public List<User> findAll() {
        return userRepository.findAll();
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
        userFoundById.setPassword(user.getPassword());

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

        update(user);

        return AssignmentProfileOutputDTO.builder()
            .name(user.getName())
            .profile(profile.getName())
            .build();
    }

}
