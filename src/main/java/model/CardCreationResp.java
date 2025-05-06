package model;

import java.time.LocalDateTime;


public class CardCreationResp {

	private String code;
	private String message;
	private LocalDateTime timestamp;
	 private long cardNumber;

	// Constructor for success response
	 public CardCreationResp(String message, long cardNumber) {
	        this.code = ""; // No code for success
	        this.message = message;
	        this.timestamp = LocalDateTime.now(); // Current timestamp
	        this.cardNumber = cardNumber;
	    }

	// Constructor for error response
	public CardCreationResp(String code, String message) {
		this.code = code;
		this.message = message;
		this.timestamp = LocalDateTime.now(); // Current timestamp
	}

    // Getter and Setter methods
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }
}