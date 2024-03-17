package com.prog3.exam.repository;

import com.prog3.exam.entity.Account;
import com.prog3.exam.entity.Loan;
import com.prog3.exam.entity.Sold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class AccountRepository extends Request<Account> {
    @Autowired
    Connection connection;

    public AccountRepository(Connection connection) {
        super(connection);
    }
    @Autowired
    SoldRepository soldRepository;
    @Autowired
    LoanRepository loanRepository;
    @Override
    public Account save(Account entity) {
        Account account= super.save(entity);
        if(account !=null){
            LocalDate date=LocalDate.now();
            Sold initalSold=new Sold();
            initalSold.setAccountId(account.getAccountNumber());
            initalSold.setBalance(0);
            initalSold.setDate(java.sql.Date.valueOf(date));
            soldRepository.save(initalSold);

            Loan initialLoan=new Loan();
            initialLoan.setIdAccount(account.getAccountNumber());
            initialLoan.setLoan_date(java.sql.Date.valueOf(date));
            initialLoan.setValue(0);
            loanRepository.save(initialLoan);

        }
        return account;
    }

    @Override
    public List<Account> findAll() {
        return super.findAll();
    }


    public Account updateAccount(Account toUpdate) {
        String clientName = toUpdate.getClientName();
        String lastName = toUpdate.getClientLastName();
        Date birthdate = toUpdate.getBirthdate();
        double monthlyNetIncome = toUpdate.getMonthlyNetIncome();
        String sql = "update account set client_name='" + clientName + "'," +
                " client_last_name='" + lastName + "', birthdate='" + birthdate + "'," +
                " monthly_net_income='" + monthlyNetIncome + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int resultSet = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toUpdate;
    }

    public Account findAccountById(float idAccount) {
        String sql="select * from account where account_number="+idAccount;
        Account account=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
             account=new Account(
                     resultSet.getLong("account_number"),
                     resultSet.getString("client_name"),
                     resultSet.getString("client_last_name"),
                     resultSet.getDate("birthdate"),
                     resultSet.getDouble("monthly_net_income"),
                     resultSet.getBoolean("is_eligible")
             );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return account;
    }

    public String updateEligibility(float idAccount, boolean option){
        String sql="update account set is_eligible="+option;
        String message="error while trying to update";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
           int response= preparedStatement.executeUpdate();
           if (response!=0){
               message="this account is now eligble to make withdrawal";
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public String getTableName() {
        return "account";
    }
}
