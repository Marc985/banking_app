package com.prog3.exam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ConnectionDB {
    private String username="postgres";

    private String password="postgres";
    @Bean
    public  Connection getConnection() throws SQLException {


        return  DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/digital_bank",
                username,
                password
        );


    }

}
