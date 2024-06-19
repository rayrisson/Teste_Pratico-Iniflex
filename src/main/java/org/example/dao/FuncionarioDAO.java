package org.example.dao;

import org.example.model.Funcionario;
import org.example.services.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class FuncionarioDAO implements DAO<Funcionario> {
    public void create(Funcionario funcionario) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement("INSERT INTO funcionarios(name, dateOfBirth, salary, jobFunction) VALUES (?, ?, ?,?)");
            stmt.setString(1, funcionario.getNome());
            stmt.setDate(2, Date.valueOf(funcionario.getDataNascimento()));
            stmt.setBigDecimal(3, funcionario.getSalario());
            stmt.setString(4, funcionario.getFuncao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.close(connection, stmt);
        }
    }

    private List<Funcionario> getBase(String complement){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Funcionario> funcionarios = new ArrayList<>();

        try {
            connection = ConnectionFactory.getConnection();
            String sql = "SELECT name, dateOfBirth, salary, jobFunction FROM funcionarios" +
                    (complement == null ? "" : " " + complement);
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("name"),
                        rs.getDate("dateOfBirth").toLocalDate(),
                        rs.getBigDecimal("salary"),
                        rs.getString("jobFunction")
                );

                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
        }

        return funcionarios;
    }

    public List<Funcionario> getAll(){
        return getBase(null);
    }

    public List<Funcionario> getAllOrderedByName(){
        return getBase("ORDER BY name");
    }

    public List<Funcionario> getWithMonth10or12(){
        return getBase("WHERE MONTH(dateOfBirth) = 10 OR MONTH(dateOfBirth) = 12");
    }

    public Funcionario getMoreOlder(){
        try {
            return getBase("ORDER BY dateOfBirth LIMIT 1").getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public BigDecimal sumAllSalaries(){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement("SELECT SUM(salary) as salary FROM funcionarios");
            rs = stmt.executeQuery();
            rs.next();
            return rs.getBigDecimal("salary");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
        }
    }

    public void increaseTenPercent() {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement("UPDATE funcionarios SET salary = salary * 1.1");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.close(connection, stmt);
        }
    }

    public void deleteByName(String name) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement("DELETE FROM funcionarios WHERE name = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.close(connection, stmt);
        }
    }
}
