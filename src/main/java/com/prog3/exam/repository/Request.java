package com.prog3.exam.repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class Request<T> implements  CrudOperation<T> {
    Connection connection;
    public Request(Connection connection){
        this.connection=connection;
    }
    public List<T> findAll() {
        String tableName=getTableName();
        String sql="select * from "+tableName;
        List<String> columns=new ArrayList<>();
        List<T> results=new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                T instance=createInstance(resultSet);
                results.add(instance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public T save(T entity) {
        try {
            String tableName = getTableName();

            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
            List<String> columns = new ArrayList<>();
            List<String> placeholders = new ArrayList<>();
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = camelToSnakeCase(field.getName());
                columns.add(fieldName);
                placeholders.add("?");
            }
            sql.append(String.join(", ", columns));
            sql.append(") VALUES (");
            sql.append(String.join(", ", placeholders));
            sql.append(")");

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            int index = 1;
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value instanceof java.util.Date) {
                    java.sql.Date sqlDate = new java.sql.Date(((java.util.Date) value).getTime());
                    preparedStatement.setDate(index++, sqlDate);
                } else {
                    preparedStatement.setObject(index++, value);
                }
            }

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }




    private T createInstance(ResultSet resultSet)  {
        try {
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

            Constructor<T> constructor=clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance=constructor.newInstance();
            Field[] fields=clazz.getDeclaredFields();
            int index=1;
            for(Field field:fields){
                field.setAccessible(true);

                Object Column=resultSet.getObject(index);
                field.set(instance,Column);
                index++;
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static String camelToSnakeCase(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return camelCaseString;
        }


        String snakeCaseString = camelCaseString.replaceAll("([a-z])([A-Z]+)", "$1_$2");


        snakeCaseString = snakeCaseString.substring(0, 1).toLowerCase() + snakeCaseString.substring(1);

        return snakeCaseString.toLowerCase();
    }
    public abstract String getTableName();
}
