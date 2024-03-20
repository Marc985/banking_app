package com.prog3.exam.repository;

import com.prog3.exam.entity.Sold;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SoldRepository extends Request<Sold>{

    public SoldRepository(Connection connection) {
        super(connection);
    }

    @Override
    public String getTableName() {
        return "sold";
    }
    public List<Sold>  findSoldsByIdAccount(long idAccount){
        String sql="select * from sold where account_id="+idAccount;
        List<Sold> solds=new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                solds.add(
                        new Sold(
                                resultSet.getDouble("balance"),
                                resultSet.getDate("date"),
                                resultSet.getLong("account_id")
                )
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  solds;
    }
    @Override
    public Sold save(Sold toSave){
        return super.save(toSave);
    }
    public Sold findLastSoldByIdAccount(float idAccount){
        String sql="select * from sold where account_id="+idAccount+"" +
                " order by id_sold desc limit 1";
        Sold sold=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
               sold= new Sold(
                        resultSet.getDouble("balance"),
                        resultSet.getDate("date"),
                       resultSet.getLong("account_id")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  sold;
    }


}
