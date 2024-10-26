package com.challenge.java.backend.card.service;

import com.challenge.java.backend.card.dto.CreditCardDto;
import com.challenge.java.backend.card.entity.CreditCardEntity;
import com.challenge.java.backend.card.mapper.CreditCardMapper;
import com.challenge.java.backend.card.repository.CreditCardRepository;
import com.challenge.java.backend.card.validator.CreditCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardService implements CreditCardInterface {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final CreditCardValidator creditCardValidator;

    public boolean operationIsValid(CreditCardDto creditCardDto, BigDecimal operationAmount) {
        if(canOperate(creditCardDto)) {
            return operationAmount.compareTo(BigDecimal.valueOf(1000L)) <= 0;
        }
        return false;
    }

    public boolean canOperate(CreditCardDto creditCardDto) {
        return creditCardValidator.isDateAfterOfToday(creditCardDto.getExpirationDate());
    }

    public CreditCardDto getCreditCardInformation(CreditCardDto creditCardToSearch) {
        if(!creditCardValidator.isValid(creditCardToSearch)) {
            throw new IllegalArgumentException("The credit card is not valid.");
        }
        Optional<CreditCardEntity> creditCardOptional = creditCardRepository.findByNumberAndBrandAndCardHolderAndExpirationDate(
                creditCardToSearch.getNumber(),
                creditCardToSearch.getBrand(),
                creditCardToSearch.getCardHolder(),
                creditCardToSearch.getExpirationDate());
        if(creditCardOptional.isEmpty()) {
            throw new IllegalArgumentException("The credit card does not exits.");
        }
        return creditCardMapper.toDataTransferObject(creditCardOptional.get());
    }

    public boolean compareCreditCards(CreditCardDto creditCardOne, CreditCardDto creditCardTwo) {
        if(!creditCardValidator.isValid(creditCardOne) || !creditCardValidator.isValid(creditCardTwo)) {
            return false;
        }
        return creditCardOne.getNumber().equals(creditCardTwo.getNumber()) &&
                creditCardOne.getBrand().equals(creditCardTwo.getBrand()) &&
                creditCardOne.getCardHolder().equals(creditCardTwo.getCardHolder()) &&
                creditCardOne.getExpirationDate().equals(creditCardTwo.getExpirationDate());
    }

    public BigDecimal getRate(String rate, BigDecimal operationAmount) {
        LocalDateTime actualDate = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        BigDecimal rateAmount;
        float rateValue;
        rateValue = getRateValue(rate, actualDate);
        rateAmount = getRateAmount(operationAmount, rateValue);
        return rateAmount;
    }

    private BigDecimal getRateAmount(BigDecimal operationAmount, float rateValue) {
        BigDecimal rateAmount;
        if(rateValue <= 0.3) {
            rateAmount = calculateRateAmount(0.3f, operationAmount);
        } else if(rateValue >= 5) {
            rateAmount = calculateRateAmount(5f, operationAmount);
        } else {
            rateAmount = calculateRateAmount(rateValue, operationAmount);
        }
        return rateAmount;
    }

    private BigDecimal calculateRateAmount(float rateValue, BigDecimal operationAmount) {
        return BigDecimal.valueOf(rateValue).multiply(operationAmount).divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY);
    }

    private float getRateValue(String rate, LocalDateTime actualDate) {
        float rateValue;
        switch (rate.toUpperCase()) {
            case "VISA" -> {
                int year = Integer.parseInt(String.valueOf(actualDate.getYear()).substring(2, 4));
                int month = Integer.parseInt(String.valueOf(actualDate.getMonthValue()));
                rateValue = (float) year / month;
            }
            case "NARA" -> {
                int day = Integer.parseInt(String.valueOf(actualDate.getMonthValue()));
                rateValue = day * 0.5f;
            }
            case "AMEX" -> {
                int month = Integer.parseInt(String.valueOf(actualDate.getMonthValue()));
                rateValue = month * 0.1f;
            }
            default -> throw new IllegalArgumentException("Not implemented yet.");
        }
        return rateValue;
    }
}
