package model;

import Validation.ValidationResult;

public class CardDetailsResponse {
    private ValidationResult validationResult;
    private CardDetails cardDetails;

    public CardDetailsResponse(ValidationResult validationResult, CardDetails cardDetails) {
        this.validationResult = validationResult;
        this.cardDetails = cardDetails;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }
}