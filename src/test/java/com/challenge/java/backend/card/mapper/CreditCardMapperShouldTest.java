package com.challenge.java.backend.card.mapper;

import com.challenge.java.backend.card.dto.CreditCardDto;
import com.challenge.java.backend.card.entity.CreditCardEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
class CreditCardMapperShouldTest {
    private CreditCardMapper creditCardMapper;
    private CreditCardDto creditCardDtoReturned;
    private Exception exceptionReturned;

    private final CreditCardDto creditCardDtoExpected = CreditCardDto.builder()
            .id(1L)
            .number("000000000000000")
            .cardHolder("CARD HOLDER")
            .brand("BRAND")
            .expirationDate(LocalDateTime.of(2030, 12, 1,0,0))
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
        givenMeAnCreditCardMapper();
    }

    @Test
    void mapACreditCardEntity() {
        whenMappingACreditCardEntity();
        thenACreditCardDtoIsReturned();
    }

    @Test
    void throwExceptionIfCreditCarEntityIsInvalid() {
        whenMappingAnInvalidCreditCardEntity();
        thenExceptionIsThrown();
    }

    private void whenMappingAnInvalidCreditCardEntity() {
        try {
            creditCardDtoReturned = creditCardMapper.toDataTransferObject(null);
        } catch (Exception exception) {
            exceptionReturned = exception;
        }
    }

    private void thenExceptionIsThrown() {
        verify(creditCardMapper, only()).toDataTransferObject(null);
        then(creditCardDtoReturned).isNull();
        then(exceptionReturned).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private void whenMappingACreditCardEntity() {
        try {
            creditCardDtoReturned = creditCardMapper.toDataTransferObject(creditCardEntity);
        } catch (Exception exception) {
            exceptionReturned = exception;
        }
    }

    private void thenACreditCardDtoIsReturned() {
        verify(creditCardMapper, only()).toDataTransferObject(creditCardEntity);
        then(creditCardDtoReturned.getId()).isEqualTo(creditCardDtoExpected.getId());
        then(creditCardDtoReturned.getNumber()).isEqualTo(creditCardDtoExpected.getNumber());
        then(creditCardDtoReturned.getExpirationDate()).isEqualTo(creditCardDtoExpected.getExpirationDate());
        then(creditCardDtoReturned.getCardHolder()).isEqualTo(creditCardDtoExpected.getCardHolder());
        then(creditCardDtoReturned.getBrand()).isEqualTo(creditCardDtoExpected.getBrand());
        then(exceptionReturned).isNull();
    }

    private void givenMeAnCreditCardMapper() {
        creditCardMapper = spy(new CreditCardMapper());
    }
}
