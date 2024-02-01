package dev.crashteam.hermes.repository;

import dev.crashteam.hermes.model.domain.UserFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedbackEntity, Long> {
}
