package com.prog3.exam.repository;

import com.prog3.exam.entity.SoldWithLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class SoldWithLoanRepository {
    @Autowired
    Connection connection;

    public SoldWithLoan getActualSoldAndLoan(long accountNumber){
        String sql="SELECT * FROM get_sold_and_loan("+accountNumber+")";
        SoldWithLoan soldWithLoan=new SoldWithLoan();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
           // preparedStatement.setLong(1,accountNumber);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                soldWithLoan.setLoan(resultSet.getDouble("loan_value"));
                soldWithLoan.setSold(resultSet.getDouble("sold_balance"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return soldWithLoan;
    }
}
