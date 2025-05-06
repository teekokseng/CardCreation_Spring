package service;

import dao.*;
import model.*;


import org.springframework.stereotype.Service;

import Validation.ValidationResult;

import java.sql.Connection;

@Service
public class CardApplicationService {

	public CardCreationResp applyCreditCard(CardApplicationRequest request) {
		Connection conn = null;
		try {
			conn = DatabaseConnectionManager.getConnection();
			if (conn == null || conn.isClosed()) {
				return new CardCreationResp("DB01", "Database connection failed.");
			}

			CardInfo cardInfo = new CardInfo(request.getCustomerNo(), request.getProductId(), request.getCardLimit());
			CreditCardApplicationservice app = new CreditCardApplicationservice(cardInfo, conn);

			ValidationResult validationResult = app.applyCard(); // This returns a ValidationResult

			if (!validationResult.isSuccess()) {
				return new CardCreationResp(validationResult.getCode(), validationResult.getMessage());
			}

			// After successful validation, get the CardInfo

			return new CardCreationResp("✅ Credit card application completed successfully.",
					app.getCardInfo().getCardNumber());

		} catch (Exception e) {
			e.printStackTrace();
			return new CardCreationResp("EX99", "Unexpected server error.");
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception ignored) {
				}
		}
	}

	public CardDetailsResponse getCardDetails(long cardNumber) {
		try (Connection conn = DatabaseConnectionManager.getConnection()) {

			return GetKortrgpf.getCardDetails(conn, cardNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
