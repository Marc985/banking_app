package com.prog3.exam.repository;

import com.prog3.exam.entity.CategorySum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategorySumRepository {
    @Autowired
    Connection connection ;
    public List<CategorySum> getCategoryAmountSum(long accountNumber, Date startDate, Date endDate){
        String sql ="SELECT * FROM category_amount_sum(?,?,?)";
        List<CategorySum> categorySums=new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,accountNumber);
            preparedStatement.setDate(2,startDate);
            preparedStatement.setDate(3,endDate);
            ResultSet resultSet= preparedStatement.executeQuery();
            String categoryNameColumn="category_name";
            String categoryTotalColumn="category_amount_sum";
            while (resultSet.next()){
                CategorySum categorySum=new CategorySum();
                categorySum.setCategoryName(resultSet.getString(categoryNameColumn));
                categorySum.setTotalAmount(resultSet.getDouble(categoryTotalColumn));
                categorySums.add(categorySum);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  categorySums;
    }
}
