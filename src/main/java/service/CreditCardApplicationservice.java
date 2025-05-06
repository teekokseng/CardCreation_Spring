package service;

import java.sql.Connection;
import java.sql.SQLException;

import Validation.ValidationResult;
import dao.AddAnsokrpf;
import jakarta.transaction.Transactional;
import model.CardInfo;

public class CreditCardApplicationservice extends CardApplicationBaseservice {

	private final AccountCreation accountCreation;
	private final AccountCreationSpgrp accountCreationSpgrp;
	private final Connection conn;

	public CreditCardApplicationservice(CardInfo cardInfo, Connection conn) {
		super(cardInfo, conn);
		this.conn = conn;
		this.accountCreation = new AccountCreation(conn);
		this.accountCreationSpgrp = new AccountCreationSpgrp(conn);
	}

	@Override
	protected ValidationResult preValidate() {
		ValidationResult result;

		result = Validation.CommonValidation.validateCustomerNo(conn, cardInfo.getCustomerNo(), "CREDIT");
		if (!result.isSuccess())
			return result;

		result = Validation.CommonValidation.validateProduct(conn, cardInfo.getCustomerNo(), cardInfo.getProduct());
		if (!result.isSuccess())
			return result;
		
		

//		result = Validation.CommonValidation.checkProductAlreadyApplied(conn,cardInfo.getCustomerNo(), cardInfo.getProduct());
//		if (!result.isSuccess())
//			return result;

		return new ValidationResult("", "✅ All validations passed.");
	}

	@Override
	@Transactional
	protected ValidationResult postProcess() {
		try {
			
			//conn.setAutoCommit(false); // 🚦 Start manual transaction

			// Generate application record
			int generatedLopn = AddAnsokrpf.createApplicationRecord(conn, cardInfo);
			cardInfo.setApplLopn(generatedLopn);
			if (generatedLopn == 0) {
				return new ValidationResult("PP01", "❌ Failed to generate application number.");
			}

			// Create accounts and cards
			boolean success = accountCreation.createAccountsAndCards(conn, cardInfo);
			if (!success) {
				return new ValidationResult("PP02", "❌ Failed to create accounts and cards.");
			}

			// Create super group account
			int spgrpAcc = accountCreationSpgrp.createSuperGroup(conn, cardInfo);
			cardInfo.setSpgrpAccNumber(spgrpAcc);
			if (spgrpAcc == 0) {
				return new ValidationResult("PP03", "❌ Failed to create super group account.");
			}

			// Link group and supergroup
			LinkGrpAndSpgrpAcc linker = new LinkGrpAndSpgrpAcc(conn, cardInfo.getSpgrpAccNumber(),
					cardInfo.getGrpAccNumber());
			boolean linked = linker.linkSpgrpAndGrpAccIfNeeded(cardInfo);
			if (!linked) {
				conn.rollback();
				return new ValidationResult("PP04", "❌ Failed to link group and supergroup account.");
			}

			//conn.commit(); // ✅ All succeeded
			return new ValidationResult("", "✅ Credit card application completed successfully.");

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
			return new ValidationResult("EX99", "⚠️ Server error during post-processing.");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
