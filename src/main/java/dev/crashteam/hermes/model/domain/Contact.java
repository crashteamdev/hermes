package dev.crashteam.hermes.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    private String userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private long phone;
    private String email;
    private Long inn;
    private boolean verification;

}
