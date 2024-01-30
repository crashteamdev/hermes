package dev.crashteam.hermes.repository;

import dev.crashteam.hermes.model.domain.CrmUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrmRepository extends JpaRepository<CrmUserEntity, Long> {

    Optional<CrmUserEntity> findByUserId(String userId);

}
