package roomservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoomRegistration {
    private Connection connection;

    public RoomRegistration(Connection connection) {
        this.connection = connection;
    }

    public void addRoom(int roomId, String roomType) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO rooms (id, type, isBooked) VALUES (?, ?, false)");
            statement.setInt(1, roomId);
            statement.setString(2, roomType);
            statement.executeUpdate();
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }
}