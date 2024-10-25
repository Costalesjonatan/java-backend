package com.challenge.java.backend.card.service;

import com.challenge.java.backend.card.dto.CreditCardDto;

import java.math.BigDecimal;

public interface CreditCardInterface {
    boolean operationIsValid(CreditCardDto creditCardDto, BigDecimal importValue);
    boolean canOperate(CreditCardDto creditCardDto);
    CreditCardDto getCreditCardInformation(CreditCardDto creditCardToSearch);
    boolean compareCreditCards(CreditCardDto creditCardOne, CreditCardDto creditCardTwo);
}