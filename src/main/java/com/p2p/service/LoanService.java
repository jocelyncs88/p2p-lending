package com.p2p.service;
import com.p2p.domain.*;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoanService {
    private static final Logger logger = LogManager.getLogger(LoanService.class);

    public Loan createLoan(Borrower borrower, BigDecimal amount) {
        logger.info("Creating loan with amount: {}", amount);

        // =========================
        // VALIDASI UTAMA (TC-01)
        // =========================
        // Jika borrower belum terverifikasi,
        // maka proses harus dihentikan
        validateBorrower(borrower);
        validateAmount(amount);

        // ===========================
        // CREATE LOAN (domain object)
        // ===========================
        Loan loan = new Loan();

        // ===================================
        // BUSINESS ACTION (domain behavior)
        // ===================================
        // Jika credit score tinggi → APPROVED
        // Jika tidak → REJECTED        
        if (borrower.getCreditScore() >= 600) {
            logger.info("Loan approved: credit score meets threshold");
            loan.approve();
        } else {
            logger.info("Loan rejected: credit score below threshold");
            loan.reject();
        }
        return loan;
    }

    // ==========================
    // PRIVATE VALIDATION METHOD 
    // ==========================
    private void validateBorrower(Borrower borrower){
        //memisahkan validation logic
        //membuat createLoan() lebih bersih
        if (!borrower.canApplyLoan()) {
            logger.warn("Loan rejected: borrower not verified");
            throw new IllegalArgumentException("Borrower not verified");
        }        
    }
    // Memvalidasi bahwa amount tidak boleh 0 atau negatif
    private void validateAmount(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Loan rejected: invalid amount {}", amount);
            throw new IllegalArgumentException("Invalid amount");
        }
    }
}