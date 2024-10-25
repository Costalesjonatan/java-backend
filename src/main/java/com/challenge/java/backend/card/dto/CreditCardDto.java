package com.challenge.java.backend.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CreditCardDto {
    private Long id;
    private String number;
    private String brand;
    private String cardHolder;
    private LocalDateTime expirationDate;
}
