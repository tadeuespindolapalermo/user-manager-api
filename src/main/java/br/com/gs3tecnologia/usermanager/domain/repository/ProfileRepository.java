package br.com.gs3tecnologia.usermanager.domain.repository;

import br.com.gs3tecnologia.usermanager.domain.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByName(String name);

}
