package com.challenge.java.backend.card.validator;

import com.challenge.java.backend.card.dto.CreditCardDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class CreditCardValidator {
    public boolean isValid(CreditCardDto creditCardDto) {
        if(creditCardDto == null) {
            throw new IllegalArgumentException("Credit card cannot be null.");
        }
        return isValidField(creditCardDto.getNumber()) &&
                isValidField(creditCardDto.getCardHolder()) &&
                isValidField(creditCardDto.getBrand()) &&
                isValidDate(creditCardDto.getExpirationDate());
    }

    public boolean isDateAfterOfToday(LocalDateTime expirationDate) {
        LocalDate localDate = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDateTime startOfDay = localDate.atStartOfDay();
        return expirationDate.isAfter(startOfDay);
    }

    private boolean isValidField(String stringToValidate) {
        return stringToValidate != null && !stringToValidate.isBlank() && !stringToValidate.isEmpty();
    }

    private boolean isValidDate(LocalDateTime expirationDate) {
        return expirationDate != null;
    }
}
