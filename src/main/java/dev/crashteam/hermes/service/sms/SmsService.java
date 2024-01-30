package dev.crashteam.hermes.service.sms;

public interface SmsService {
    void smsSend(Long number, String text);

    void smsSend(Long number, String senderName, String text);

    String generateApproveCode();
}
