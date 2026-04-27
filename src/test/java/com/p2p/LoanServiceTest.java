package com.p2p;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

import com.p2p.domain.Borrower;
import com.p2p.domain.Loan;
import com.p2p.service.LoanService;

public class LoanServiceTest {

    //TC 01
    @Test
    void shouldRejectLoanWhenBorrowerNotVerified() {
        //======================================================================
        // SCENARIO:
        // Borrower tidak terverifikasi (KYC = false)
        // Ketika borrower mengajukan pinjaman
        // Maka sistem harus menolak dengan melempar exception
        // =====================================================================

        // ===========================
        // Arrange (Initial Condition)
        // ===========================
        // Borrower BELUM LOLOS proses KYC
        Borrower borrower = new Borrower(false, 700);

        //Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        //Jumlah pinjaman valid
        BigDecimal amount = BigDecimal.valueOf(1000);

        // =========================
        // ACTION + ASSERT 
        // =========================
        // Ketika borrower mengajukan loan,
        // Sistem harus MENOLAK dengan melempar exception
        // Borrower mencoba mengajukan loan        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amount);
        }); 
    }

    //TC 02
    @Test
    void shouldRejectLoanWhenAmountIsZeroOrNegative() {
        //======================================================================
        // SCENARIO:
        // Borrower terverifikasi (KYC = true)
        // Ketika borrower mengajukan pinjaman dengan amount <= 0
        // Maka sistem harus menolak dengan melempar exception
        // =====================================================================

        // ===========================
        // Arrange (Initial Condition)
        // ===========================
        // Borrower SUDAH LOLOS proses KYC
        Borrower borrower = new Borrower(true, 700);

        //Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        //Jumlah pinjaman tidak valid
        BigDecimal amount = BigDecimal.valueOf(0);

        // =========================
        // ACTION + ASSERT 
        // =========================
        // Ketika borrower mengajukan loan dengan amount yang tidak valid,
        // Sistem harus MENOLAK dengan melempar exception
        // Borrower mencoba mengajukan loan        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amount);
        }); 
        
    }
    
    //TC 03
    @Test
    void shouldApprovedLoanWhenCreditScoreHigh() {
        //======================================================================
        // SCENARIO:
        // Borrower verified
        // Credit score tinggi
        // Amount valid
        // Loan APPROVED
        // =====================================================================

        // ===========================
        // Arrange (Initial Condition)
        // ===========================
        Borrower borrower = new Borrower(true, 700);

        //Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        //Jumlah pinjaman valid
        BigDecimal amount = BigDecimal.valueOf(1000);

        // =========================
        // ACTION
        // =========================
        Loan loan = loanService.createLoan(borrower, amount);

        // =========================
        // ASSERT (expected result)
        // =========================
        // Sistem menyetujui loan karena credit score tinggi (>= threshold)
        // Status loan APPROVED
        assertEquals(Loan.Status.APPROVED, loan.getStatus());        
    }
    
    //TC 04
    @Test
    void shouldRejectLoanWhenCreditScoreLow() {
        //======================================================================
        // SCENARIO:
        // Borrower verified
        // Credit score rendah (< threshold)
        // Amount valid 
        // Loan REJECTED
        // =====================================================================

        // ===========================
        // Arrange (Initial Condition)
        // ===========================
        Borrower borrower = new Borrower(true, 500);

        //Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        //Jumlah pinjaman valid
        BigDecimal amount = BigDecimal.valueOf(1000);

        // =========================
        // ACTION
        // =========================
        Loan loan = loanService.createLoan(borrower, amount);

        // =========================
        // ASSERT (expected result)
        // =========================
        // Sistem menolak loan karena credit score rendah (< threshold)
        // Status loan REJECTED
        assertEquals(Loan.Status.REJECTED, loan.getStatus());        
    }       
}