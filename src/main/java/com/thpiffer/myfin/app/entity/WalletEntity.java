package com.thpiffer.myfin.app.entity;


import com.thpiffer.myfin.app.enumeration.EnumWalletType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@EqualsAndHashCode
@Table(name = "tb_wallet")
@SQLDelete(sql = "UPDATE tb_wallet SET exclusion_date_time = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("exclusion_date_time IS NULL")
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", unique = true, nullable = false)
    private int code;

    @Column(name = "description")
    private String description;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EnumWalletType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private BankEntity bank;

    @Column(name = "exclusion_date_time")
    private LocalDateTime exclusionDateTime;

}
