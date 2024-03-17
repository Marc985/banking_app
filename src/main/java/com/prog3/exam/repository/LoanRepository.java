package com.prog3.exam.repository;

import com.prog3.exam.entity.Loan;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class LoanRepository extends Request <Loan> {
    public LoanRepository(Connection connection) {
        super(connection);
    }

    @Override
    public String getTableName() {
        return "loans";
    }

    @Override
    public Loan save(Loan loan){
        return  (super.save(loan));
    }

    public Loan getLastLoan(long accountNumber){
        String sql="select * from loans where id_account=? order by loan_date desc limit 1";
        Loan loan=new Loan();

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,accountNumber);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                loan.setLoan_date(resultSet.getDate("loan_date"));
                loan.setValue(resultSet.getDouble("value"));
                loan.setIdAccount(resultSet.getLong("id_account"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  loan;
    }
}
