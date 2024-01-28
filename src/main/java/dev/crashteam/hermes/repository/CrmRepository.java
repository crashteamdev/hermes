package dev.crashteam.hermes.repository;

import dev.crashteam.hermes.model.domain.Crm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrmRepository extends JpaRepository<Crm, Long> {

    Optional<Crm> findByUserId(String userId);

}
