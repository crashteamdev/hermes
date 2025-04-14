package dev.crashteam.hermes.repository;

import dev.crashteam.hermes.model.domain.DemoAccessRequestEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoAccessRepository extends JpaRepository<DemoAccessRequestEntity, Long> {
    DemoAccessRequestEntity findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE DemoAccessRequestEntity d SET d.isUsed = true WHERE d.token = :token")
    void markAsUsedByToken(@Param("token") String token);
}
