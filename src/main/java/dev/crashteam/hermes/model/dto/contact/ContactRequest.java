package dev.crashteam.hermes.model.dto.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {

    public ContactRequest(String firstName, long phone, String email) {
        this.name = firstName;
        this.phones = List.of(new ContactPhone("+" + phone));
        this.emails = List.of(new ContactEmail(email));
    }

    private String name;
    private List<ContactPhone> phones;
    private List<ContactEmail> emails;

    @Data
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactPhone {
        private String phone;
    }

    @Data
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactEmail {
        private String email;
    }

}
