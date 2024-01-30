package dev.crashteam.hermes.repository;

import dev.crashteam.hermes.model.domain.CrmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrmRepository extends JpaRepository<CrmUser, Long> {

    Optional<CrmUser> findByUserId(String userId);

}
