package roomservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RoomBooking {
    private Connection connection;

    public RoomBooking(Connection connection) {
        this.connection = connection;
    }

    public void bookRoom(String roomType, int customerId, Date startDate, Date endDate) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM rooms WHERE type = ? AND isBooked = false");
            statement.setString(1, roomType);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int roomId = result.getInt("id");

                PreparedStatement bookingStatement = connection.prepareStatement("INSERT INTO bookings (roomId, customerId, startDate, endDate, isPaid) VALUES (?, ?, ?, ?, ?)");
                bookingStatement.setInt(1, roomId);
                bookingStatement.setInt(2, customerId);
                bookingStatement.setDate(3, new java.sql.Date(startDate.getTime())); // Convert java.util.Date to java.sql.Date
                bookingStatement.setDate(4, new java.sql.Date(endDate.getTime())); // Convert java.util.Date to java.sql.Date
                bookingStatement.setBoolean(5, false);
                bookingStatement.executeUpdate();

                PreparedStatement updateRoomStatement = connection.prepareStatement("UPDATE rooms SET isBooked = true WHERE id = ?");
                updateRoomStatement.setInt(1, roomId);
                updateRoomStatement.executeUpdate();

                System.out.println("Room booked successfully.");
            } else {
                System.out.println("No available rooms of type: " + roomType);
            }
        } catch (SQLException e) {
            System.out.println("Error booking room: " + e.getMessage());
        }
    }
}