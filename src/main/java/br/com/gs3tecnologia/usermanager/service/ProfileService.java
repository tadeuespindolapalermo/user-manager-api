package br.com.gs3tecnologia.usermanager.service;

import br.com.gs3tecnologia.usermanager.exception.BusinessException;
import br.com.gs3tecnologia.usermanager.model.Profile;
import br.com.gs3tecnologia.usermanager.model.User;
import br.com.gs3tecnologia.usermanager.repository.ProfileRepository;
import br.com.gs3tecnologia.usermanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Profile register(Profile profile) throws BusinessException {
        if (profile.getName().trim().isEmpty()) {
            throw new BusinessException("Profile name cannot be blank!");
        }
        profile.setName(profile.getName().trim());
        if (isNull(profile.getId())) {
            return create(profile);
        }
        return update(profile);
    }

    public Profile create(Profile profile) throws BusinessException {
        Profile profileFound = profileRepository.findByName(profile.getName());
        if (nonNull(profileFound)) {
            throw new BusinessException("Profile already exists with the name: " + profile.getName());
        }
        return profileRepository.save(profile);
    }

    public Profile update(Profile profile) throws BusinessException {
        Profile profileFoundById = findById(profile.getId());
        Profile profileFoundByName = profileRepository.findByName(profile.getName());

        if (nonNull(profileFoundByName) && !profileFoundById.getId().equals(profileFoundByName.getId())) {
            throw new BusinessException("The name provided already exists for another profile.");
        }

        profileFoundById.setName(profile.getName());

        return profileRepository.save(profileFoundById);
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity Profile not found with ID: " + id));
    }

    public void deleteById(Long id) throws BusinessException {
        Profile profile = findById(id);
        List<User> users = userRepository.findByProfile(profile);
        if (!users.isEmpty()) {
            throw new BusinessException("The Profile " + profile.getName() + " cannot be removed as it is associated with an existing user!");
        }
        profileRepository.deleteById(id);
    }

}
