package com.prog3.exam.repository;

import com.prog3.exam.entity.Sold;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class SoldCrudOperation extends Request<Sold>{

    public SoldCrudOperation(Connection connection) {
        super(connection);
    }

    @Override
    public String getTableName() {
        return "sold";
    }
    @Override
    public List<Sold>  findAll(){
        return super.findAll();
    }
    @Override
    public Sold save(Sold toSave){
        return super.save(toSave);
    }
    public Sold findLastSoldByIdAccount(float idAccount){
        String sql="select * from sold where account_id="+idAccount+"" +
                " order by date desc limit 1";
        Sold sold=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
               sold= new Sold(
                        resultSet.getInt("id_sold"),
                        resultSet.getDouble("balance"),
                        resultSet.getDouble("loans"),
                        resultSet.getInt("loansinterest"),
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
