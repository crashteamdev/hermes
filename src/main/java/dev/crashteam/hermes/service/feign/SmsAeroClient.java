package dev.crashteam.hermes.service.feign;

import dev.crashteam.hermes.model.dto.sms.SmsRequest;
import dev.crashteam.hermes.model.dto.sms.SmsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "SmsAeroClient", url = "${app.integration.sms-aero.url}")
public interface SmsAeroClient {

    @GetMapping("/sms/send")
    SmsResponse smsSend(@RequestParam SmsRequest smsRequests);

}
