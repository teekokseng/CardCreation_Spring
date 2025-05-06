package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Validation.ValidationResult;
import model.CardInfo;

public abstract class CardApplicationBaseservice {

	
	protected Connection conn;
	 protected final CardInfo cardInfo;

	public CardApplicationBaseservice(CardInfo cardInfo, Connection conn) {
		this.cardInfo = cardInfo;
		this.conn = conn;  
	}

	// Abstract Methods that Child Classes MUST implement
	// ==================================================
	protected abstract ValidationResult preValidate();// Validation before creating card

	protected abstract ValidationResult postProcess();// Processing after validation (generate card, insert into DB,
														// etc.)

	// Generic method
	// ==============
	public final ValidationResult applyCard() {
	    var result = preValidate();
	    if (!result.isSuccess()) return result;

	    result = postProcess();
	    if (!result.isSuccess()) return result;

	    return new ValidationResult("", "Card application completed successfully.");
	}
	
	public CardInfo getCardInfo() {
		return this.cardInfo;
	}

}