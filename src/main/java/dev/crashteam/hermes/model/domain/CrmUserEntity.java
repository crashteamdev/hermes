package dev.crashteam.hermes.model.domain;

import dev.crashteam.hermes.model.CrmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "crm_user")
@AllArgsConstructor
@NoArgsConstructor
public class CrmUserEntity {

    @Id
    @SequenceGenerator(name = "crmIdSeq", sequenceName = "crm_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crmIdSeq")
    private Long id;
    private String userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private Long phone;
    private String email;
    private Long inn;
    @Enumerated(EnumType.STRING)
    private CrmType crmType;
    private String crmExternalId;
    private boolean verification;
    private String approveCode;

}
