package dev.crashteam.hermes.service.sms;

import dev.crashteam.hermes.model.dto.sms.SmsRequest;
import dev.crashteam.hermes.service.crm.CrmService;
import dev.crashteam.hermes.service.feign.SmsAeroClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final CrmService crmService;
    private final SmsAeroClient smsAeroClient;

    public void smsSend(Long number, String text) {
        smsSend(number, "SMS Aero", text);
    }

    public void smsSend(Long number, String senderName, String text) {
        smsAeroClient.smsSend(new SmsRequest(number.toString(), senderName, text));
        log.info("Code {} has been sent to the number {}", text, number);
    }

    public String generateApproveCode() {
        int min = 100000;
        int max = 999999;
        int approveCode = new Random().nextInt((max - min) + 1) + min;
        crmService.saveApproveCode(Integer.toString(approveCode));
        return Integer.toString(approveCode);
    }

}
