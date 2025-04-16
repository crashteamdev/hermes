package dev.crashteam.hermes.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "demo_access_request")
@AllArgsConstructor
@NoArgsConstructor
public class DemoAccessRequestEntity {
    @Id
    @SequenceGenerator(name = "daIdSeq", sequenceName = "da_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daIdSeq")
    private Long id;
    private String userIdentity;
    private String token;
    private Boolean isUsed;

    public static DemoAccessRequestEntity createNew(String userId, String token) {
        return new DemoAccessRequestEntity(null, userId, token, false);
    }
}
