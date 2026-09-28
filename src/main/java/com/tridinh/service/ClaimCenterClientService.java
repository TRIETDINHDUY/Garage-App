package com.tridinh.service;

import com.tridinh.dto.cc.CcClaimUpdateRequest;
import com.tridinh.dto.cc.CcEstimateNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Outbound: Garage App to ClaimCenter.
 * Pushes status updates and estimates to CC when Garage App has new data.
 * Disabled by default (claimcenter.enabled=false) for local dev without CC running.
 */
@Service
public class ClaimCenterClientService {

    private static final Logger log = LoggerFactory.getLogger(ClaimCenterClientService.class);

    private final RestClient ccClient;

    @Value("${claimcenter.enabled:false}")
    private boolean ccEnabled;

    @Value("${claimcenter.api.status-update-path:/rest/v1/claims/{claimId}/repair-status}")
    private String statusUpdatePath;

    @Value("${claimcenter.api.estimate-notify-path:/rest/v1/claims/{claimId}/estimates}")
    private String estimateNotifyPath;

    public ClaimCenterClientService(@Qualifier("claimCenterRestClient") RestClient ccClient) {
        this.ccClient = ccClient;
    }

    /**
     * Push repair order status update to ClaimCenter.
     */
    public void notifyStatusUpdate(CcClaimUpdateRequest request) {
        if (!ccEnabled) {
            log.info("[CC-CLIENT] Integration disabled. Would notify: claim={} status={}",
                    request.claimNumber(), request.repairOrderStatus());
            return;
        }
        try {
            String path = statusUpdatePath.replace("{claimId}", request.claimId() != null ? request.claimId() : "");
            ccClient.post()
                    .uri(path)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[CC-CLIENT] Status update sent to CC: claim={} status={}",
                    request.claimNumber(), request.repairOrderStatus());
        } catch (RestClientException ex) {
            log.error("[CC-CLIENT] Failed to notify CC of status update: claim={} error={}",
                    request.claimNumber(), ex.getMessage());
        }
    }

    /**
     * Push billing estimate notification to ClaimCenter.
     */
    public void notifyEstimate(CcEstimateNotification notification) {
        if (!ccEnabled) {
            log.info("[CC-CLIENT] Integration disabled. Would notify estimate: claim={} total={}",
                    notification.claimNumber(), notification.totalAmount());
            return;
        }
        try {
            String path = estimateNotifyPath.replace("{claimId}", notification.claimId() != null ? notification.claimId() : "");
            ccClient.post()
                    .uri(path)
                    .body(notification)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[CC-CLIENT] Estimate sent to CC: claim={} total={}",
                    notification.claimNumber(), notification.totalAmount());
        } catch (RestClientException ex) {
            log.error("[CC-CLIENT] Failed to notify CC of estimate: claim={} error={}",
                    notification.claimNumber(), ex.getMessage());
        }
    }
}
