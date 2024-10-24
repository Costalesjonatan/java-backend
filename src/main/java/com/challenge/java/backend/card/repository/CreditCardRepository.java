package com.challenge.java.backend.card.repository;

import com.challenge.java.backend.card.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
