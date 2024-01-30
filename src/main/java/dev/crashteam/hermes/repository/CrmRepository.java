package dev.crashteam.hermes.repository;

import dev.crashteam.hermes.model.domain.CrmDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrmRepository extends JpaRepository<CrmDomain, Long> {

    Optional<CrmDomain> findByUserId(String userId);

}
