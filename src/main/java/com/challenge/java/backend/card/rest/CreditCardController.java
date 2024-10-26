package com.challenge.java.backend.card.rest;

import com.challenge.java.backend.card.dto.ErrorResponse;
import com.challenge.java.backend.card.dto.OperationRequest;
import com.challenge.java.backend.card.dto.RateResponse;
import com.challenge.java.backend.card.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/credit-card")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardService creditCardService;

    @GetMapping("/rate")
    public ResponseEntity getRate(@RequestBody OperationRequest operationRequest) {
        try {
            BigDecimal rate = creditCardService.getRate(operationRequest.getCardBrand(),
                    operationRequest.getOperationAmount());
            return ResponseEntity.ok(RateResponse.builder().rate(rate.toString()).build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder()
                            .httpMethod(String.valueOf(HttpMethod.GET))
                            .url("/credit-card/rate")
                            .message(exception.getLocalizedMessage())
                            .timestamp(String.valueOf(LocalDateTime.now()))
                    .build());
        }
    }
}

