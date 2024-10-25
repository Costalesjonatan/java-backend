package com.challenge.java.backend.card.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity(name = "credit_card")
@Table(name = "credit_card")
@EqualsAndHashCode
public class CreditCardEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number")
    private String number;
    @Column(name = "brand")
    private String brand;
    @Column(name = "cardholder")
    private String cardHolder;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;



}
