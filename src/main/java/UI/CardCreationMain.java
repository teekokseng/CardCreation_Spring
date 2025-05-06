package UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import dao.*;
import model.CardDetails;
import model.CardDetailsResponse;
import model.CardInfo;
import service.*;
import Validation.ValidationResult;



public class CardCreationMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Get a single shared connection
		Connection conn = DatabaseConnectionManager.getConnection();

		if (conn == null) {
			System.out.println("Database connection failed! Exiting.");
			return; // exit if connection is not established
		}

		System.out.println("===========================================");
		System.out.println("      Welcome to Card Management System    ");
		System.out.println("===========================================\n");

		boolean running = true;
		while (running) {
			System.out.println("\nPlease select an option:");
			System.out.println(" [1] ➔ Create New Card");
			System.out.println(" [2] ➔ Cards Inquiry");
			System.out.println(" [3] ➔ Exit");
			System.out.println("-------------------------------------------");
			System.out.print("Enter your choice: ");

			int choice = -1;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input, please enter a number!");
				continue;
			}

			switch (choice) {
			case 1:
				createNewCard(scanner, conn);
				break;
			case 2:
				viewCard(scanner, conn);

				break;
			case 3:
				running = false;
				System.out.println("Exiting application. Goodbye!");
				break;
			default:
				System.out.println("Invalid choice. Please select again.");
			}
		}
		// Close the connection when done
		DatabaseConnectionManager.closeConnection();

		scanner.close();
	}

	private static void createNewCard(Scanner scanner, Connection conn) {

		System.out.println("-------------------------------------------");
		System.out.println("         Card Type Selection");
		System.out.println("-------------------------------------------\n");
		System.out.println(" [1] ➔ Credit Card");
		System.out.println(" [2] ➔ Corporate Credit Card");
		System.out.println(" [9] ➔ Back to previous\n");
		System.out.print("Enter your choice: ");

		int cardTypeChoice = -1;
		try {
			cardTypeChoice = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input, please enter a number!");
			return;
		}

		if (cardTypeChoice == 9) {
			return; // go back to main menu
		}

		if (cardTypeChoice != 1 && cardTypeChoice != 2) {
			System.out.println("Invalid card type. Creation cancelled.");
			return;
		}

		System.out.print("Enter Customer Number: ");
		int customerNo = Integer.parseInt(scanner.nextLine());

		System.out.print("Enter Product: ");
		String product = scanner.nextLine();

		System.out.print("Enter Card Limit: ");
		int cardLimit = Integer.parseInt(scanner.nextLine());

		CardApplicationBaseservice application;
		CardInfo cardInfo = new CardInfo(customerNo, product, cardLimit);

		// Credit Card
		if (cardTypeChoice == 1) {
			application = new CreditCardApplicationservice(cardInfo, conn);

		} else {
		// Corporate Credit Card
			System.out.print("Enter Corporate Account: ");
			int corporateAccount = Integer.parseInt(scanner.nextLine());

			System.out.print("Enter Division Account: ");
			int divisionAccount = Integer.parseInt(scanner.nextLine());

			cardInfo = new CardInfo(customerNo, product, cardLimit, corporateAccount, divisionAccount);
			application = new CorpCreditCardApplication(cardInfo, conn);
		}

		// 2. Run applyCard()
		ValidationResult processSts = application.applyCard();

		if (processSts.isSuccess() == false) {
			System.out.println("Card creation failed due to validation issues.");
			return;
		}

		System.out.println(processSts.getMessage());

	}

	private static void viewCard(Scanner scanner, Connection conn) {

		System.out.println("-------------------------------------------");
		System.out.println("         Card Inquiry");
		System.out.println("-------------------------------------------\n");
		System.out.println(" [1] ➔ View All Cards");
		System.out.println(" [2] ➔ View Specific Card");
		System.out.println(" [9] ➔ Back to previous\n");
		System.out.print("Enter your choice: ");

		int cardInqChoice = -1;
		try {
			cardInqChoice = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("❌ Invalid input, please enter a number!");
			return;
		}

		if (cardInqChoice == 9) {
			return; // Back to previous
		}

		switch (cardInqChoice) {
		case 1:
			GetKortrgpf.viewCreatedCards(conn);
			break;
		case 2:
			System.out.print("Enter Card Number: ");
			String cardInput = scanner.nextLine().trim();

			long cardNumber;
			try {
				if (!cardInput.matches("\\d+")) {
					System.out.println("❌ Input contains non-numeric characters.");
					return;
				}

				cardNumber = Long.parseLong(cardInput); // if matches only digits, this should succeed
				System.out.println("✅ Parsed card number: " + cardNumber);

			} catch (NumberFormatException e) {
				System.out.println("❌ Invalid card number. Could not convert to long.");
				e.printStackTrace(); // this will help identify root cause
				return;
			}

			CardDetailsResponse response = GetKortrgpf.getCardDetails(conn, cardNumber);
			ValidationResult result = response.getValidationResult();

			CardDetails details = response.getCardDetails();
			if (!result.isSuccess()) {

				System.out.println(result.getCode() + result.getMessage()); // Already has ❌ or other prefix
				return;
			}

			System.out.println("\n=== Card Details ===");
			System.out.println("Card Number     : " + details.getCardNumber());
			System.out.println("Customer No     : " + details.getCustomerNo());
			System.out.println("Customer Name   : " + details.getCustomerName());
			System.out.println("Product         : " + details.getProduct());
			System.out.println("Group Account   : " + details.getGroupAccount());
			System.out.println("Credit Limit    : " + details.getCreditLimit());
			System.out.println("Account Status  : " + details.getAccountStatus());

			break;

		default:
			System.out.println("❌ Invalid choice. Please select again.");
		}
	}
}
