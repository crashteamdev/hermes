package dev.crashteam.hermes.model.domain;

import dev.crashteam.hermes.model.CrmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_feedback")
@AllArgsConstructor
@NoArgsConstructor
public class UserFeedbackEntity {

    @Id
    @SequenceGenerator(name = "ufIdSeq", sequenceName = "uf_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ufIdSeq")
    private Long id;
    private String firstName;
    private Long phone;
    private String email;

}
