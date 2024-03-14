package com.prog3.exam.repository;

import com.prog3.exam.ConnectionDB;
import com.prog3.exam.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class AccountCrudOperation extends Request<Account>{
    @Autowired
    Connection connection;

    public AccountCrudOperation(Connection connection) {
        super(connection);
    }

    @Override
    public Account save(Account entity) {
        return super.save(entity);
    }
    @Override
    public List<Account> findAll(){
        return super.findAll();
    }


    public  Account updateAccount(Account toUpdate){
        String clientName=toUpdate.getClientName();
        String lastName=toUpdate.getClientLastName();
        Date birthdate=toUpdate.getBirthdate();
        BigDecimal monthlyNetIncome=toUpdate.getMonthlyNetIncome();
        String sql="update account set client_name='"+clientName+"'," +
                " client_last_name='"+ lastName+"', birthdate='"+birthdate+"'," +
                " monthly_net_income='"+monthlyNetIncome+"'";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            int resultSet=preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
        return toUpdate;
    }


    @Override
    public String getTableName() {
        return "account";
    }
}
