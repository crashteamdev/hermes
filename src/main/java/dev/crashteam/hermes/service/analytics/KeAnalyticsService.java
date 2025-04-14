package dev.crashteam.hermes.service.analytics;

import dev.crashteam.mp.external.analytics.management.ExternalCategoryAnalyticsServiceGrpc;
import dev.crashteam.mp.external.analytics.management.GiveSubscriptionRequest;
import dev.crashteam.mp.external.analytics.management.GiveSubscriptionResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KeAnalyticsService {

    public KeAnalyticsService(@GrpcClient("keAnalyticsService")
                              ExternalCategoryAnalyticsServiceGrpc.ExternalCategoryAnalyticsServiceBlockingStub keAnalyticsServiceGrpc) {
        this.keAnalyticsServiceGrpc = keAnalyticsServiceGrpc;
    }

    private final ExternalCategoryAnalyticsServiceGrpc.ExternalCategoryAnalyticsServiceBlockingStub keAnalyticsServiceGrpc;

    public boolean giveSubscription(String userId, String subscriptionId, int dayCount) {
        GiveSubscriptionResponse giveSubscriptionResponse = keAnalyticsServiceGrpc.giveSubscription(
                GiveSubscriptionRequest.newBuilder()
                        .setUserId(userId)
                        .setSubscriptionId(subscriptionId)
                        .setDayCount(dayCount).build()
        );
        if (giveSubscriptionResponse.hasErrorResponse()) {
            log.warn("Failed to give subscription for {}: {}", subscriptionId, giveSubscriptionResponse.getErrorResponse());
            return false;
        }
        return true;
    }
}
