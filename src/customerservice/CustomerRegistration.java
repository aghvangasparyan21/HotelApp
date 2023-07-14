package customerservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerRegistration {
    private Connection connection;

    public CustomerRegistration(Connection connection) {
        this.connection = connection;
    }

    public void addCustomer(int customerId, String name, String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO customers (id, name, email) VALUES (?, ?, ?)");
            statement.setInt(1, customerId);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.executeUpdate();
            System.out.println("entities.Customer added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }
}
