package dev.crashteam.hermes.service.sms;

import dev.crashteam.hermes.model.dto.sms.SmsRequest;
import dev.crashteam.hermes.service.feign.SmsAeroClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsAeroClient smsAeroClient;

    @Override
    public void smsSend(Long number, String text) {
        smsSend(number, "SMS Aero", text);
    }

    @Override
    public void smsSend(Long number, String senderName, String text) {
        smsAeroClient.smsSend(new SmsRequest(number.toString(), senderName, text));
        log.info("Code {} has been sent to the number {}", text, number);
    }

    @Override
    public String generateSmsCode() {
        int min = 100000;
        int max = 999999;
        int smsCode = new Random().nextInt((max - min) + 1) + min;

        return String.valueOf(smsCode);
    }

}
