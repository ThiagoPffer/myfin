package com.thpiffer.myfin.app.entity;


import com.thpiffer.myfin.app.enumeration.EnumMovementStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@EqualsAndHashCode
@Table(name = "tb_movement")
@SQLDelete(sql = "UPDATE tb_movement SET exclusion_date_time = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("exclusion_date_time IS NULL")
@NoArgsConstructor
@AllArgsConstructor
public class MovementEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", unique = true, nullable = false)
    private int code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletEntity wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnumMovementStatus status;

    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;

    @Column(name = "exclusion_date_time")
    private LocalDateTime exclusionDateTime;

}
