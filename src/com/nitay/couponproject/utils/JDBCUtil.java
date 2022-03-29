package com.nitay.couponproject.utils;

import com.nitay.couponproject.config.Config;

import java.sql.*;

/**
 * A utility class for simple database connection.
 *
 * @see ConnectionPool
 * @deprecated Replaced with ConnectionPool
 */
public class JDBCUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/coupons_project?useSSL=false&serverTimezone=UTC",
                "root",
                Config.getMySQLpass()
        );
    }

    public static void closeResources(Connection connection, PreparedStatement preparedStatement) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        closeResources(connection, preparedStatement);
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
