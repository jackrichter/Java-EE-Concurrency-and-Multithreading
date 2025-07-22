package com.linkedin.concurrency.dao;

import com.linkedin.concurrency.beans.BankAccount;
import com.linkedin.concurrency.beans.BankAccountTransaction;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankAccountDao {

    private Logger logger = Logger.getLogger(BankAccountDao.class.getName());

    private DataSource dataSource;
    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public BankAccountDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<BankAccount> getAllBankAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        BankAccount account = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM bank_account");
            while (rs.next()) {
                account = new BankAccount();
                account.setAccNumber(rs.getInt("acc_number"));
                account.setName(rs.getString("acc_holder_name"));
                account.setEmail(rs.getString("acc_email"));
                account.setAccType(rs.getString("acc_type"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        } finally {
            closeConnection(conn, stmt, null, rs);
        }
        return accounts;
    }

    public List<BankAccountTransaction> getAllTransactionsForAccounts(BankAccount account) {
        List<BankAccountTransaction> transactions = new ArrayList<>();
        BankAccountTransaction transaction = null;

        try {
            this.conn = this.dataSource.getConnection();
            this.pstmt = conn.prepareStatement("SELECT * FROM bank_account_transaction WHERE acc_number = ?");
            pstmt.setInt(1, account.getAccNumber());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                transaction = new BankAccountTransaction();
                transaction.setAccNumber(rs.getInt("acc_number"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setTxDate(new Date(rs.getDate("transaction_date").getTime()));
                transaction.setTxId(rs.getInt("tx_id"));
                transaction.setTxType(rs.getString("transaction_type"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        } finally {
            closeConnection(conn, null, pstmt, rs);
        }
        return transactions;
    }

    private void closeConnection(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {rs.close();}
            if (stmt != null) {stmt.close();}
            if (pstmt != null) {pstmt.close();}
            if (conn != null) {conn.close();}
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
}
