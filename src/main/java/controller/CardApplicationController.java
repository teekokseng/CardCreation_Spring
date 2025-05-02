package controller;

import dto.CardApplicationRequest;
import service.CardApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardApplicationController {

    @Autowired
    private CardApplicationService cardApplicationService;

    @PostMapping("/credit")
    public ResponseEntity<String> applyCreditCard(@RequestBody CardApplicationRequest request) {
        boolean result = cardApplicationService.applyCreditCard(request);
        if (result) {
            return ResponseEntity.ok("✅ Credit card application completed successfully.");
        } else {
            return ResponseEntity.badRequest().body("❌ Card creation failed due to validation issues.");
        }
    }
}