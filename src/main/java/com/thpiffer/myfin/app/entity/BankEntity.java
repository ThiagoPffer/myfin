package com.thpiffer.myfin.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@EqualsAndHashCode
@Table(name = "tb_bank")
@SQLDelete(sql = "UPDATE tb_bank SET exclusion_date_time = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("exclusion_date_time IS NULL") // testar
@NoArgsConstructor
@AllArgsConstructor
public class BankEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private int code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "exclusion_date_time")
    private LocalDateTime exclusionDateTime;

}
