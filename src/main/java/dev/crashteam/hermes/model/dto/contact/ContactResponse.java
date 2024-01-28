package dev.crashteam.hermes.model.dto.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {

    public ContactResponse(String email, Long phone) {
        this.email = email;
        this.phone = phone;
    }

    private String email;
    private Long phone;
    private String inn;

}
