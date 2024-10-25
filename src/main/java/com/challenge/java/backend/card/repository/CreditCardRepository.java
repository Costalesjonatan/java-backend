package com.challenge.java.backend.card.repository;

import com.challenge.java.backend.card.entity.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {
    Optional<CreditCardEntity> findByNumberAndBrandAndCardHolderAndExpirationDate(String number, String brand, String cardHolder, LocalDateTime expirationDate);
}
