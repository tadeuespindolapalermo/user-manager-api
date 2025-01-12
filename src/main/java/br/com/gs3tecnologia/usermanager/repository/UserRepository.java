package br.com.gs3tecnologia.usermanager.repository;

import br.com.gs3tecnologia.usermanager.model.Profile;
import br.com.gs3tecnologia.usermanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByProfile(Profile profile);

}

