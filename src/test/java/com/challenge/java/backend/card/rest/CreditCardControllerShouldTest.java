package com.challenge.java.backend.card.rest;

import com.challenge.java.backend.card.brand.Brand;
import com.challenge.java.backend.card.dto.ErrorResponse;
import com.challenge.java.backend.card.dto.OperationRequest;
import com.challenge.java.backend.card.dto.RateResponse;
import com.challenge.java.backend.card.service.CreditCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class CreditCardControllerShouldTest {
    private CreditCardService creditCardService;
    private CreditCardController creditCardController;
    private ResponseEntity returnedResponse;
    private Exception returnedException;
    private final OperationRequest operationRequest = OperationRequest.builder()
            .operationAmount(BigDecimal.valueOf(1000))
            .cardBrand(Brand.VISA.name())
            .build();
    private final RateResponse expectedResponse = RateResponse.builder().rate("25").build();

    @BeforeEach
    void setUp() {
        giveMeAnCreditCardService();
        giveMeAnCreditCardController();
    }

    @Test
    void shouldReturnRate() {
        whenRequestingRate();
        thenRateIsReturned();
    }

    @Test
    void handleException() {
        whenHandlingException();
        thenTheExceptionIsHandled();
    }

    private void whenHandlingException() {
        when(creditCardService.getRate(operationRequest.getCardBrand(), operationRequest.getOperationAmount()))
                .thenThrow(new IllegalArgumentException("Not implemented yet."));
        try {
            returnedResponse = creditCardController.getRate(operationRequest);
        } catch (Exception exception) {
            returnedException = exception;
        }
    }

    private void thenTheExceptionIsHandled() {
        verify(creditCardService, only()).getRate(operationRequest.getCardBrand(), operationRequest.getOperationAmount());
        verify(creditCardController, only()).getRate(operationRequest);

        then(returnedException).isNull();
        then(((ResponseEntity<ErrorResponse>)returnedResponse).getBody().getMessage()).isEqualTo("Not implemented yet.");
        then(((ResponseEntity<ErrorResponse>)returnedResponse).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(((ResponseEntity<ErrorResponse>)returnedResponse).getBody().getUrl()).isEqualTo("/credit-card/rate");
        then(((ResponseEntity<ErrorResponse>)returnedResponse).getBody().getHttpMethod()).isEqualTo(String.valueOf(HttpMethod.GET));

    }

    private void whenRequestingRate() {
        when(creditCardService.getRate(operationRequest.getCardBrand(), operationRequest.getOperationAmount()))
                .thenReturn(BigDecimal.valueOf(25));
        try {
            returnedResponse = creditCardController.getRate(operationRequest);
        } catch (Exception exception) {
            returnedException = exception;
        }
    }

    private void thenRateIsReturned() {
        verify(creditCardService, only()).getRate(operationRequest.getCardBrand(), operationRequest.getOperationAmount());
        verify(creditCardController, only()).getRate(operationRequest);

        then(returnedException).isNull();
        then(returnedResponse.getBody()).isEqualTo(expectedResponse);
    }

    private void giveMeAnCreditCardService() {
        creditCardService = mock(CreditCardService.class);
    }

    private void giveMeAnCreditCardController() {
        creditCardController = spy(new CreditCardController(creditCardService));
    }
}
