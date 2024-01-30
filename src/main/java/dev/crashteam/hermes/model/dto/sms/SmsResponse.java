package dev.crashteam.hermes.model.dto.sms;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {

    private boolean status;
    private List<SmsData> data;
    private String message;

    @Data
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SmsData {

        private long id;
        private String from;
        private String number;
        private String text;
        private int status;
        private String extendStatus;
        private String channel;
        private BigDecimal cost;
        private Timestamp dateCreate;
        private Timestamp dateSend;

    }

}
