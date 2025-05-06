package controller;

import service.CardApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import model.CardApplicationRequest;
import model.CardCreationResp;
import model.CardDetailsResponse;


@RestController
@RequestMapping("/api/cards")
public class CardApplicationController {

    @Autowired
    private CardApplicationService cardApplicationService;

    @PostMapping("/credit")
    public ResponseEntity<Object> applyCreditCard(@RequestBody CardApplicationRequest request) {
        System.out.println("Received: " + request.getCustomerNo() + ", " + request.getProductId() + ", " + request.getCardLimit());
        
        CardCreationResp response = cardApplicationService.applyCreditCard(request);

        if (response.getCode().isEmpty()) {
            return ResponseEntity.ok(response); 
        } else {
            return ResponseEntity.badRequest().body(response); // Failure response with error code/message
        }
    }
    
    @GetMapping("/{cardNumber}")
    public ResponseEntity<Object> getCardDetails(@PathVariable long cardNumber) {
        CardDetailsResponse details = cardApplicationService.getCardDetails(cardNumber);
        if (details != null) {
            return ResponseEntity.ok(details);
        } else {
        	return ResponseEntity.badRequest().body(details); // Failure response with error code/message
        }
    }
}