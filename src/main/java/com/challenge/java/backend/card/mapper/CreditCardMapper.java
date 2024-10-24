package com.challenge.java.backend.card.mapper;

import com.challenge.java.backend.card.dto.CreditCardDto;
import com.challenge.java.backend.card.entity.CreditCardEntity;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapper {
    public CreditCardDto toDataTransferObject(CreditCardEntity creditCardEntity) {

        if(creditCardEntity == null) {
            throw new IllegalArgumentException("Credit card cannot be null.");
        }

        return CreditCardDto.builder()
                .id(creditCardEntity.getId())
                .number(creditCardEntity.getNumber())
                .brand(creditCardEntity.getBrand())
                .cardHolder(creditCardEntity.getCardHolder())
                .expirationDate(creditCardEntity.getExpirationDate())
                .build();
    }

}
