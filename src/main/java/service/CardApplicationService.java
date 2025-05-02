package service;

import app.CreditCardApplication;
import dto.*;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public class CardApplicationService {

    public boolean applyCreditCard(CardApplicationRequest request) {
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            CreditCardApplication app = new CreditCardApplication(
                    request.getCustomerNo(),
                    request.getProductId(),
                    request.getCardLimit(),
                    conn
            );
            return app.applyCard();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
