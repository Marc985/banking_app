package com.prog3.exam.repository;

import com.prog3.exam.entity.EntryExpense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class EntryExpenseRepository {
    @Autowired
    Connection connection;
    public EntryExpense getEntryExepenseSum(long accountNumber, Date startDate,Date endDate){
        String sql="select * from entry_expense_sum (?,?,?)";
        EntryExpense entryExpense=new EntryExpense();

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,accountNumber);
            preparedStatement.setDate(2,startDate);
            preparedStatement.setDate(3,endDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            String expenseColumn="expense";
            String entryColumn="entry";
            while (resultSet.next()){
                entryExpense.setEntry(resultSet.getDouble(entryColumn));
                entryExpense.setExpense(resultSet.getDouble(expenseColumn));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  entryExpense;
    }
}
