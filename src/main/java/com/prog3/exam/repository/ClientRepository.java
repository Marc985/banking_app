package com.prog3.exam.repository;

import com.prog3.exam.entity.Client;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import javax.swing.text.html.HTMLDocument;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

@Repository
public class ClientRepository extends  Request<Client> {
    String idClientColumn="id_client";
    String firstNameColumn="first_name";
    String lastNameColumn="last_name";
    String birthdateColumn="birthdate";
    String monthlySalaryColumn="monthly_net_salary";

    public ClientRepository(Connection connection) {
        super(connection);
    }
    @Override
    public List<Client> findAll() {
        return super.findAll();
    }
    public Client saveOrUpdate(Client client) {
        String sql = "INSERT INTO client(id_client, name, email, pic) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (id_client) DO UPDATE SET name = EXCLUDED.name, " +
                "email = EXCLUDED.email, pic = EXCLUDED.pic";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getIdClient());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setString(4, client.getPic());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Client saved/updated successfully.");
            } else {
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            // Utilisez un logger au lieu de printStackTrace dans une vraie application
            e.printStackTrace();
            throw new RuntimeException("Error saving or updating client.", e);
        }

        return client;
    }

    public Client findById(String id){
        String sql="select * from client where id_client=?";
        Client client=new Client();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                client.setIdClient(resultSet.getString("id_client"));
                client.setName(resultSet.getString("name"));
                client.setEmail(resultSet.getString("email"));
                client.setPic(resultSet.getString("pic"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }
    public Client findByIdAccount(long idAccount){
        String sql="select client.id_client,name,email,pic from client inner " +
                "join account on client.id_client=account.id_client where account_number=?";
        Client client=new Client();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,idAccount);
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                client.setIdClient(resultSet.getString("id_client"));
                client.setEmail(resultSet.getString("name"));
                client.setName(resultSet.getString("email"));
                client.setPic(resultSet.getString("pic"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;

    }
    public Client updateClientById(String idClient,Client client){
       String sql= "UPDATE client SET name = ?, email = ?, pic = ? WHERE id_client = ?";
       try {
           PreparedStatement preparedStatement=connection.prepareStatement(sql);
           preparedStatement.setString(1,client.getName());
           preparedStatement.setString(2,client.getEmail());
           preparedStatement.setString(3,client.getPic());
           preparedStatement.setString(4,client.getIdClient());
           int result=preparedStatement.executeUpdate();


       }catch (Exception e){
           e.printStackTrace();
       }
       return client;
    }
    @Override
    public Client save(Client client){
        return super.save(client);
    }
    @Override
    public String getTableName() {
        return "client";
    }
}
