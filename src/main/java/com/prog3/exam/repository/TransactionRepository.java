package com.prog3.exam.repository;

import com.prog3.exam.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

@Repository
public class TransactionRepository {
@Autowired
    Connection connection;
public Transaction saveTransaction(Transaction transaction){
    String sql="insert into transaction values (?,?,?,?,?,?)";
    String transactionReference= "VIR_"+LocalDateTime.now().toString();
    try {
        PreparedStatement preparedStatement=connection.prepareStatement(sql);

        preparedStatement.setString(1,transactionReference);
        preparedStatement.setString(2,transaction.getType());
        preparedStatement.setDouble(3,transaction.getAmount());
        preparedStatement.setDate(4,transaction.getDate());
        preparedStatement.setString(5,transaction.getReason());
        preparedStatement.setLong(6,transaction.getAccountNumber());
        int response=preparedStatement.executeUpdate();
    }catch (Exception e){
        e.printStackTrace();
    }
    return transaction;

}
}
