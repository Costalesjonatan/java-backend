package com.challenge.java.backend.card.service;

import com.challenge.java.backend.card.dto.CreditCardDto;
import com.challenge.java.backend.card.entity.CreditCardEntity;
import com.challenge.java.backend.card.mapper.CreditCardMapper;
import com.challenge.java.backend.card.repository.CreditCardRepository;
import com.challenge.java.backend.card.validator.CreditCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final CreditCardValidator creditCardValidator;

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
}
