package com.prog3.exam.repository;

import com.prog3.exam.entity.AccountStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountStatementRepository {
    @Autowired
    Connection connection;
    public List<AccountStatement> getAccountStatement(long accountNumber){
        String sql="select * from account_statement("+accountNumber+")";
        List<AccountStatement> accountStatements=new ArrayList<>();
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                AccountStatement newAcocuntStatement=new AccountStatement();
                newAcocuntStatement.setReference(resultSet.getString("reference"));
                newAcocuntStatement.setDate(resultSet.getDate("transaction_date"));
                newAcocuntStatement.setSold(resultSet.getDouble("balance"));
                newAcocuntStatement.setReason("reason");

                String type= resultSet.getString("type");
                if(type.equals("debit")){
                    newAcocuntStatement.setDebit(resultSet.getDouble("amount"));
                    newAcocuntStatement.setCredit(0);
                }
                else{
                    newAcocuntStatement.setCredit(resultSet.getDouble("amount"));
                    newAcocuntStatement.setDebit(0);
                }
                accountStatements.add(newAcocuntStatement);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountStatements;
    }
}