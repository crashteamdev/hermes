package dev.crashteam.hermes.service.demo;


import dev.crashteam.hermes.model.domain.DemoAccessRequestEntity;
import dev.crashteam.hermes.repository.DemoAccessRepository;
import dev.crashteam.hermes.service.analytics.KeAnalyticsService;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class DemoAccessService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public DemoAccessService(
            DemoAccessRepository demoAccessRepository,
            KeAnalyticsService keAnalyticsService) {
        this.demoAccessRepository = demoAccessRepository;
        this.keAnalyticsService = keAnalyticsService;
    }

    private final DemoAccessRepository demoAccessRepository;
    private final KeAnalyticsService keAnalyticsService;

    public String createDemoAccess(String userId) {
        String token = generateBase62Token(16);
        DemoAccessRequestEntity demoAccessRequestEntity = DemoAccessRequestEntity.createNew(userId, token);
        demoAccessRepository.save(demoAccessRequestEntity);
        return token;
    }

    public boolean giveDemoByToken(String userId, String token) {
        DemoAccessRequestEntity demoAccessRequestEntity = demoAccessRepository.findByToken(token);
        if (demoAccessRequestEntity != null && !demoAccessRequestEntity.getIsUsed()) {
            boolean isDemoAccess = keAnalyticsService.giveSubscription(
                    userId, "demo", 3);
            if (isDemoAccess) {
                demoAccessRepository.markAsUsedByToken(token);
            }
        }
        return demoAccessRequestEntity != null && !demoAccessRequestEntity.getIsUsed();
    }

    public static String generateBase62Token(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(BASE62_ALPHABET.length());
            sb.append(BASE62_ALPHABET.charAt(index));
        }
        return sb.toString();
    }

}
