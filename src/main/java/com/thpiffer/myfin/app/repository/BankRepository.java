package com.thpiffer.myfin.app.repository;

import com.thpiffer.myfin.app.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, UUID> {
}
