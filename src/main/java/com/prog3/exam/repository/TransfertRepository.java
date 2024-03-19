package com.prog3.exam.repository;

import com.prog3.exam.entity.Transfert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransfertRepository {
    @Autowired
Connection connection;
public String saveTransfert(Transfert transfert){
    String sql="insert into transfert values (?,?,?,?,?,?,?)";
    LocalDateTime current_date=LocalDateTime.now();
    String reference="VIR_"+current_date.toString();
    try {
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setString(1,transfert.getReference());
        preparedStatement.setString(2,transfert.getReason());
        preparedStatement.setDouble(3,transfert.getAmount());
        preparedStatement.setDate(4,transfert.getEffectiveDate());
        preparedStatement.setDate(5,transfert.getRegistrationDate());
        preparedStatement.setString(6,transfert.getStatus());
        preparedStatement.setLong(7,transfert.getRecipientAccount());
        int row=preparedStatement.executeUpdate();

    }catch (Exception e){
        e.printStackTrace();
    }
    return "saved successfully";
}
public String updateStatus(String status,String transfertReference){
    String sql = "update transfert set status=? where reference=?";
    try {
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setString(1,status);
        preparedStatement.setString(2,transfertReference);
        preparedStatement.executeUpdate();
    }catch (Exception e){
        e.printStackTrace();
    }
return null;
}
public List<Transfert> findByEffectiveDate(Date date){
    String sql = "select * from transfert where effective_date=?";
    List<Transfert> transferts=new ArrayList<>();
    Transfert transfert =new Transfert();
    try {
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setDate(1,date);
        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
            transfert.setSenderAccount(resultSet.getLong("account"));
            transfert.setReference(resultSet.getString("reference"));
            transfert.setEffectiveDate(resultSet.getDate("effective_date"));
            transfert.setRegistrationDate(resultSet.getDate("registration_date"));
            transfert.setStatus(resultSet.getString("status"));
            transfert.setAmount(resultSet.getDouble("amount"));
            transfert.setReason(resultSet.getString("reason"));
          transferts.add(transfert);
        }
    }catch (Exception e){
        e.printStackTrace();
    }
    return transferts;
}

}
