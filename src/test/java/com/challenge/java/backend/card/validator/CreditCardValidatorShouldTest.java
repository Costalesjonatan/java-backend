package com.challenge.java.backend.card.validator;

import com.challenge.java.backend.card.dto.CreditCardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
class CreditCardValidatorShouldTest {
    private CreditCardValidator creditCardValidator;
    private boolean returnedResult;
    private Exception retunedException;

    private final CreditCardDto validCreditCardDto = CreditCardDto.builder()
            .number("000000000000000")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
            .build();

    private final CreditCardDto invalidCreditCardDto = CreditCardDto.builder()
            .number(null)
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2024, 10, 1,0,0))
            .build();

    @BeforeEach
    void setUp() {
        givenAnCreditCardValidator();
        resetReturnedException();
    }

    @Test
    void returnTrueIfCreditCardDtoIsValid() {
        whenCreditCardDtoIsValidating();
        thenReturnTrue();
    }

    @Test
    void returnFalseIfCreditCardDtoIsInvalid() {
        whenInvalidCreditCardDtoIsValidating();
        thenReturnFalse();
    }

    @Test
    void throwExceptionIfCreditCardDtoIsNull() {
        whenValidatingAnNullCreditCardDto();
        thenExceptionIsThrown();
    }

    @Test
    void returnTrueIfExpirationDateIsValid() {
        whenValidatingAnValidExpirationDate();
        thenExpirationDateIsValid();
    }

    @Test
    void returnFalseIfExpirationDateIsInvalid() {
        whenValidatingAnInvalidExpirationDate();
        thenExpirationDateIsInvalid();
    }

    private void whenValidatingAnInvalidExpirationDate() {
        try {
            returnedResult = creditCardValidator.isDateAfterOfToday(invalidCreditCardDto.getExpirationDate());
        } catch (Exception exception) {
            retunedException = exception;
        }
    }

    private void thenExpirationDateIsInvalid() {
        verify(creditCardValidator, only()).isDateAfterOfToday(invalidCreditCardDto.getExpirationDate());
        then(returnedResult).isFalse();
        then(retunedException).isNull();
    }

    private void whenValidatingAnValidExpirationDate() {
        try {
            returnedResult = creditCardValidator.isDateAfterOfToday(validCreditCardDto.getExpirationDate());
        } catch (Exception exception) {
            retunedException = exception;
        }
    }

    private void thenExpirationDateIsValid() {
        verify(creditCardValidator, only()).isDateAfterOfToday(validCreditCardDto.getExpirationDate());
        then(returnedResult).isTrue();
        then(retunedException).isNull();
    }

    private void whenValidatingAnNullCreditCardDto() {
        try {
            returnedResult = creditCardValidator.isValid(null);
        } catch (Exception exception) {
            retunedException = exception;
        }
    }

    private void thenExceptionIsThrown() {
        verify(creditCardValidator, only()).isValid(null);
        then(returnedResult).isFalse();
        then(retunedException).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private void whenInvalidCreditCardDtoIsValidating() {
        try {
            returnedResult = creditCardValidator.isValid(invalidCreditCardDto);
        } catch (Exception exception) {
            retunedException = exception;
        }
    }

    private void whenCreditCardDtoIsValidating() {
        try {
            returnedResult = creditCardValidator.isValid(validCreditCardDto);
        } catch (Exception exception) {
            retunedException = exception;
        }
    }

    private void thenReturnTrue() {
        verify(creditCardValidator, only()).isValid(validCreditCardDto);
        then(returnedResult).isTrue();
        then(retunedException).isNull();
    }

    private void thenReturnFalse() {
        verify(creditCardValidator, only()).isValid(invalidCreditCardDto);
        then(returnedResult).isFalse();
        then(retunedException).isNull();
    }

    private void givenAnCreditCardValidator() {
        creditCardValidator = spy(new CreditCardValidator());
    }

    private void resetReturnedException() {
        retunedException = null;
    }

}
