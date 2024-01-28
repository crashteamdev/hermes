package dev.crashteam.hermes.service.feign;

import dev.crashteam.hermes.model.dto.sms.SmsRequest;
import dev.crashteam.hermes.model.dto.sms.SmsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "SmsAeroClient", url = "https://rns572@mail.ru:GYS6A6xfVA1-zV1TC1HJrQ8xeaPApHnI@gate.smsaero.ru/v2/")
public interface SmsAeroClient {

    @GetMapping("/sms/send")
    SmsResponse smsSend(@RequestParam SmsRequest smsRequests);

}
