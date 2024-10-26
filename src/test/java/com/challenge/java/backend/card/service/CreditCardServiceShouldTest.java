package com.challenge.java.backend.card.service;

import com.challenge.java.backend.card.dto.CreditCardDto;
import com.challenge.java.backend.card.entity.CreditCardEntity;
import com.challenge.java.backend.card.mapper.CreditCardMapper;
import com.challenge.java.backend.card.repository.CreditCardRepository;
import com.challenge.java.backend.card.validator.CreditCardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class CreditCardServiceShouldTest {
    private CreditCardRepository creditCardRepository;
    private CreditCardMapper creditCardMapper;
    private CreditCardValidator creditCardValidator;
    private CreditCardService creditCardService;
    private CreditCardDto creditCardReturned;
    private boolean isEquals;
    private boolean canOperate;
    private boolean operationIsValid;
    private Exception expectedException;
    private BigDecimal calculatedRate;

    private final CreditCardDto expectedCreditCard = CreditCardDto.builder()
            .id(1L)
            .number("000000000000000")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
            .build();

    private final CreditCardDto creditCardToObtainDto = CreditCardDto.builder()
            .number("000000000000000")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
            .build();

    private final CreditCardDto equalCreditCard = CreditCardDto.builder()
            .number("000000000000000")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
            .build();

    private final CreditCardDto notEqualCreditCard = CreditCardDto.builder()
            .number("1111111111111111")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
            .build();

    private final CreditCardDto invalidCreditCard = CreditCardDto.builder()
            .number(null)
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,1))
            .build();

    private final CreditCardEntity creditCardEntity = CreditCardEntity.builder()
            .id(1L)
            .number("000000000000000")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
            .build();

    @BeforeEach
    void setUp() {
        giveMeAnCreditCardRepository();
        giveMeAnCreditCardMapper();
        giveMeAnCreditCardValidator();
        giveMeAnCreditCardService();
        expectedException = null;
        calculatedRate = null;
    }

    @Test
    void returnCreditCardInformation() {
        whenGettingCreditCard();
        thenCreditCardIsReturned();

    }

    @Test
    void throwExceptionIfCreditCardDoesNotExist() {
        whenGettingNonExistingCreditCard();
        thenExceptionIsThrown();
    }


    @Test
    void throwExceptionIfCreditCardDoesIsInvalid() {
        whenGettingNotValidCreditCard();
        thenExceptionIsThrown();
    }

    @Test
    void compareEqualsCreditsCards() {
        whenComparingCreditCards();
        thenTrueIsReturned();
    }

    @Test
    void compareNotEqualsEqualsCreditsCards() {
        whenComparingEqualsCreditCards();
        thenFalseIsReturned();
    }

    @Test
    void compareInvalidEqualsCreditsCards() {
        whenComparingInvalidCreditCards();
        thenFalseIsReturnedByInvalid();
    }

    @Test
    void validateExpirationDate() {
        whenComparingInvalidCreditCards();
        thenFalseIsReturnedByInvalid();
    }

    @Test
    void returnTrueIfCreditCardCanOperate() {
        whenIsVerifyingThatItCanOperate();
        thenTrueIsReturnedBecauseCanOperate();
    }

    @Test
    void returnFalseIfCreditCardCanOperate() {
        whenIsVerifyingThatItCannotOperate();
        thenFalseIsReturnedBecauseCannotOperate();
    }

    @Test
    void returnTrueIfOperationIsValid() {
        whenIsVerifyingValidOperation();
        thenOperationIsValid();
    }

    @Test
    void returnFalseIfOperationIsInvalid() {
        whenIsVerifyingAnInvalidOperation();
        thenOperationIsInvalid();
    }

    @Test
    void returnFalseIfCreditCardCannotOperate() {
        whenIsVerifyingAnCreditCardCannotOperate();
        thenOperationIsInvalidBecauseCannotOperate();
    }

    private void thenOperationIsInvalidBecauseCannotOperate() {
        verify(creditCardService, times(1)).operationIsValid(invalidCreditCard, BigDecimal.valueOf(1000.01));
        verify(creditCardService, times(1)).canOperate(invalidCreditCard);

        then(operationIsValid).isFalse();
        then(expectedException).isNull();
    }

    private void whenIsVerifyingAnCreditCardCannotOperate() {
        when(creditCardService.canOperate(invalidCreditCard)).thenReturn(false);
        try {
            operationIsValid = creditCardService.operationIsValid(invalidCreditCard, BigDecimal.valueOf(1000.01));
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void whenIsVerifyingValidOperation() {
        when(creditCardService.canOperate(expectedCreditCard)).thenReturn(true);
        try {
            operationIsValid = creditCardService.operationIsValid(expectedCreditCard, BigDecimal.valueOf(999.99));
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenOperationIsValid() {
        verify(creditCardService, times(1)).operationIsValid(expectedCreditCard, BigDecimal.valueOf(999.99));
        verify(creditCardService, times(1)).canOperate(expectedCreditCard);

        then(operationIsValid).isTrue();
        then(expectedException).isNull();
    }

    private void whenIsVerifyingAnInvalidOperation() {
        when(creditCardService.canOperate(expectedCreditCard)).thenReturn(true);
        try {
            operationIsValid = creditCardService.operationIsValid(expectedCreditCard, BigDecimal.valueOf(1000.01));
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenOperationIsInvalid() {
        verify(creditCardService, times(1)).operationIsValid(expectedCreditCard, BigDecimal.valueOf(1000.01));
        verify(creditCardService, times(1)).canOperate(expectedCreditCard);

        then(operationIsValid).isFalse();
        then(expectedException).isNull();
    }


    private void whenIsVerifyingThatItCanOperate() {
        when(creditCardValidator.isDateAfterOfToday(expectedCreditCard.getExpirationDate())).thenReturn(true);
        try {
            canOperate = creditCardService.canOperate(expectedCreditCard);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenTrueIsReturnedBecauseCanOperate() {
        verify(creditCardService, only()).canOperate(expectedCreditCard);
        verify(creditCardValidator, only()).isDateAfterOfToday(expectedCreditCard.getExpirationDate());
        verify(creditCardMapper, never()).toDataTransferObject(creditCardEntity);

        then(canOperate).isTrue();
        then(expectedException).isNull();
    }

    private void whenIsVerifyingThatItCannotOperate() {
        when(creditCardValidator.isDateAfterOfToday(expectedCreditCard.getExpirationDate())).thenReturn(false);
        try {
            canOperate = creditCardService.canOperate(expectedCreditCard);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenFalseIsReturnedBecauseCannotOperate() {
        verify(creditCardService, only()).canOperate(expectedCreditCard);
        verify(creditCardValidator, only()).isDateAfterOfToday(expectedCreditCard.getExpirationDate());
        verify(creditCardMapper, never()).toDataTransferObject(creditCardEntity);

        then(canOperate).isFalse();
        then(expectedException).isNull();
    }


    private void thenFalseIsReturnedByInvalid() {
        verify(creditCardService, only()).compareCreditCards(creditCardToObtainDto, invalidCreditCard);
        verify(creditCardValidator, times(2)).isValid(any());
        verify(creditCardMapper, never()).toDataTransferObject(creditCardEntity);

        then(isEquals).isFalse();
        then(expectedException).isNull();
    }

    private void whenComparingInvalidCreditCards() {
        when(creditCardValidator.isValid(creditCardToObtainDto)).thenReturn(true);
        when(creditCardValidator.isValid(invalidCreditCard)).thenReturn(false);
        try {
            isEquals = creditCardService.compareCreditCards(creditCardToObtainDto, invalidCreditCard);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void whenComparingCreditCards() {
        when(creditCardValidator.isValid(creditCardToObtainDto)).thenReturn(true);
        when(creditCardValidator.isValid(equalCreditCard)).thenReturn(true);
        try {
            isEquals = creditCardService.compareCreditCards(creditCardToObtainDto, equalCreditCard);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenTrueIsReturned() {
        verify(creditCardService, only()).compareCreditCards(creditCardToObtainDto, equalCreditCard);
        verify(creditCardValidator, times(2)).isValid(any());
        verify(creditCardMapper, never()).toDataTransferObject(any());

        then(isEquals).isTrue();
        then(expectedException).isNull();
    }

    private void whenComparingEqualsCreditCards() {
        when(creditCardValidator.isValid(creditCardToObtainDto)).thenReturn(true);
        when(creditCardValidator.isValid(notEqualCreditCard)).thenReturn(true);
        try {
            isEquals = creditCardService.compareCreditCards(creditCardToObtainDto, notEqualCreditCard);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenFalseIsReturned() {
        verify(creditCardService, only()).compareCreditCards(creditCardToObtainDto, notEqualCreditCard);
        verify(creditCardValidator, times(2)).isValid(any());
        verify(creditCardMapper, never()).toDataTransferObject(creditCardEntity);

        then(isEquals).isFalse();
        then(expectedException).isNull();
    }

    private void whenGettingNonExistingCreditCard() {
        creditCardReturned = null;
        when(creditCardValidator.isValid(creditCardToObtainDto)).thenReturn(false);
        when(creditCardRepository.findByNumberAndBrandAndCardHolderAndExpirationDate(
                creditCardToObtainDto.getNumber(),
                creditCardToObtainDto.getBrand(),
                creditCardToObtainDto.getCardHolder(),
                creditCardToObtainDto.getExpirationDate())
        ).thenReturn(Optional.empty());
        try {
            creditCardReturned = creditCardService.getCreditCardInformation(creditCardToObtainDto);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void whenGettingNotValidCreditCard() {
        expectedException = null;
        when(creditCardValidator.isValid(creditCardToObtainDto)).thenReturn(true);
        try {
            creditCardReturned = creditCardService.getCreditCardInformation(creditCardToObtainDto);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenExceptionIsThrown() {
        verify(creditCardService, only()).getCreditCardInformation(creditCardToObtainDto);
        verify(creditCardValidator, only()).isValid(creditCardToObtainDto);
        verify(creditCardMapper, never()).toDataTransferObject(creditCardEntity);

        then(expectedException).isExactlyInstanceOf(IllegalArgumentException.class);
        then(creditCardReturned).isNull();
    }

    private void whenGettingCreditCard() {
        expectedException = null;
        when(creditCardValidator.isValid(creditCardToObtainDto)).thenReturn(true);
        when(creditCardRepository.findByNumberAndBrandAndCardHolderAndExpirationDate(
                creditCardToObtainDto.getNumber(),
                creditCardToObtainDto.getBrand(),
                creditCardToObtainDto.getCardHolder(),
                creditCardToObtainDto.getExpirationDate())
        ).thenReturn(Optional.of(creditCardEntity));
        when(creditCardMapper.toDataTransferObject(creditCardEntity)).thenReturn(expectedCreditCard);
        try {
            creditCardReturned = creditCardService.getCreditCardInformation(creditCardToObtainDto);
        } catch (Exception exception) {
            expectedException = exception;
        }
    }

    private void thenCreditCardIsReturned() {
        verify(creditCardService, only()).getCreditCardInformation(creditCardToObtainDto);
        verify(creditCardValidator, only()).isValid(creditCardToObtainDto);
        verify(creditCardMapper, only()).toDataTransferObject(creditCardEntity);

        then(expectedException).isNull();
        then(creditCardReturned).isEqualTo(expectedCreditCard);
    }

    private void giveMeAnCreditCardRepository() {
        this.creditCardRepository = mock(CreditCardRepository.class);
    }

    private void giveMeAnCreditCardMapper() {
        this.creditCardMapper = mock(CreditCardMapper.class);
    }

    private void giveMeAnCreditCardValidator() {
        this.creditCardValidator = mock(CreditCardValidator.class);

    }

    private void giveMeAnCreditCardService() {
        this.creditCardService = spy(new CreditCardService(
                creditCardRepository,
                creditCardMapper,
                creditCardValidator
        ));
    }
}
