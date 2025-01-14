package br.com.gs3tecnologia.usermanager.config.database;

import br.com.gs3tecnologia.usermanager.domain.model.Profile;
import br.com.gs3tecnologia.usermanager.domain.model.User;
import br.com.gs3tecnologia.usermanager.domain.repository.ProfileRepository;
import br.com.gs3tecnologia.usermanager.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Profile profileCommonUser = new Profile();
        profileCommonUser.setName("ROLE_USER");
        profileCommonUser.setDescription("Profile Common");

        Profile profileAdmin = new Profile();
        profileAdmin.setName("ROLE_ADMIN");
        profileAdmin.setDescription("Profile Admin");

        User user = new User();
        user.setName("User Admin");
        user.setEmail("admin@admin.com");
        user.setPassword(new BCryptPasswordEncoder().encode("admin123"));
        user.setProfile(profileAdmin);

        if (profileRepository.findAll().isEmpty()) {
            profileRepository.save(profileCommonUser);
            profileRepository.save(profileAdmin);
        }

        if (userRepository.findAll().isEmpty()) {
            userRepository.save(user);
        }
    }
}