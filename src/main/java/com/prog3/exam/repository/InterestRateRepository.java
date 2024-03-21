package com.prog3.exam.repository;

import com.prog3.exam.entity.InterestRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class InterestRateRepository {
    @Autowired
    Connection connection;
public InterestRate getInterestRate(){
    String sql="select * from interest_rate";
    InterestRate interestRate=new InterestRate();
    try {
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        ResultSet resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            interestRate.setFirst7days(resultSet.getFloat("first_7days"));
            interestRate.setAfter7days(resultSet.getFloat("after_7days"));
        }
    }catch (Exception e){
        e.printStackTrace();
    }
    return interestRate;
}

public String updateInterestRate(float first7days,float after7days){
    String sql="update interest_rate set first_7days=?,after_7days=?";
    String response="error while updating interest rate";
    try {
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setFloat(1,first7days);
        preparedStatement.setFloat(2, after7days);
        int result=preparedStatement.executeUpdate();
        if(result>0)
            response="interest rate updated successfully";
    }catch (Exception e){
        e.printStackTrace();
    }
    return  response;
}
}
