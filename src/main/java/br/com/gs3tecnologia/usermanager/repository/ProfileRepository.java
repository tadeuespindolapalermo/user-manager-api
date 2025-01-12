package br.com.gs3tecnologia.usermanager.repository;

import br.com.gs3tecnologia.usermanager.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByName(String name);

}
