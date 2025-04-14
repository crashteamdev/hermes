package dev.crashteam.hermes.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
