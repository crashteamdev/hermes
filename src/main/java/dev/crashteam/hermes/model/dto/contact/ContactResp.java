package dev.crashteam.hermes.model.dto.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ContactResp {

    private Integer id;

}
