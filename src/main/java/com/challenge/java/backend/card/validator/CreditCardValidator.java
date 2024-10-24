package com.challenge.java.backend.card.validator;

import com.challenge.java.backend.card.dto.CreditCardDto;
import org.springframework.stereotype.Component;

@Component
public class CreditCardValidator {
    public boolean isValid(CreditCardDto creditCardDto) {
        if(creditCardDto == null) {
            throw new IllegalArgumentException("Credit card cannot be null.");
        }
        return isValidField(creditCardDto.getNumber()) &&
                isValidField(creditCardDto.getCardHolder()) &&
                isValidField(creditCardDto.getExpirationDate()) &&
                isValidField(creditCardDto.getBrand());
    }

    private boolean isValidField(String stringToValidate) {
        return stringToValidate != null && !stringToValidate.isBlank() && !stringToValidate.isEmpty();
    }

}
