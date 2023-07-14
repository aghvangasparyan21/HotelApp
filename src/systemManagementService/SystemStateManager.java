package systemManagementService;

import entities.Customer;
import entities.Booking;
import operationwithFiles.FileUtils;
import rooms.DeluxeRoom;
import rooms.DoubleRoom;
import entities.Room;
import rooms.SingleRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemStateManager {
    private Connection connection;

    public SystemStateManager(Connection connection) {
        this.connection = connection;
    }

    public void saveState(String filePath) {
        try {
            List<Room> rooms = getAllRooms();
            List<Customer> customers = getAllCustomers();
            List<Booking> bookings = getAllBookings();

            // Serialize and save objects to the file path
            FileUtils.serializeToFile(filePath, rooms, customers, bookings);
            System.out.println("System state saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public void loadState(String filePath) {
        try {
            // Deserialize objects from the file path
            Object[] objects = FileUtils.deserializeFromFile(filePath);
            List<Room> rooms = (List<Room>) objects[0];
            List<Customer> customers = (List<Customer>) objects[1];
            List<Booking> bookings = (List<Booking>) objects[2];

            // Update the database with the deserialized objects
            updateDatabase(rooms, customers, bookings);
            System.out.println("System state loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading system state: " + e.getMessage());
        }
    }

    private List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM rooms");

        while (result.next()) {
            int roomId = result.getInt("id");
            String type = result.getString("type");
            boolean isBooked = result.getBoolean("isBooked");

            if (type.equals("Single")) {
                rooms.add(new SingleRoom(roomId, isBooked));
            } else if (type.equals("Double")) {
                rooms.add(new DoubleRoom(roomId, isBooked));
            } else if (type.equals("Deluxe")) {
                rooms.add(new DeluxeRoom(roomId, isBooked));
            }
        }

        return rooms;
    }

    private List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM customers");

        while (result.next()) {
            int customerId = result.getInt("id");
            String name = result.getString("name");
            String email = result.getString("email");
            customers.add(new Customer(customerId, name, email));
        }

        return customers;
    }

    private List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM bookings");

        while (result.next()) {
            int bookingId = result.getInt("id");
            int roomId = result.getInt("roomId");
            int customerId = result.getInt("customerId");
            Date startDate = result.getDate("startDate");
            Date endDate = result.getDate("endDate");
            boolean isPaid = result.getBoolean("isPaid");
            bookings.add(new Booking(bookingId, roomId, customerId, startDate, endDate, isPaid));
        }

        return bookings;
    }

    private void updateDatabase(List<Room> rooms, List<Customer> customers, List<Booking> bookings) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM rooms");
        statement.executeUpdate("DELETE FROM customers");
        statement.executeUpdate("DELETE FROM bookings");

        for (Room room : rooms) {
            PreparedStatement roomStatement = connection.prepareStatement("INSERT INTO rooms (id, type, isBooked) VALUES (?, ?, ?)");
            roomStatement.setInt(1, room.getId());
            roomStatement.setString(2, room.getType());
            roomStatement.setBoolean(3, room.isBooked());
            roomStatement.executeUpdate();
        }

        for (Customer customer : customers) {
            PreparedStatement customerStatement = connection.prepareStatement("INSERT INTO customers (id, name, email) VALUES (?, ?, ?)");
            customerStatement.setInt(1, customer.getId());
            customerStatement.setString(2, customer.getName());
            customerStatement.setString(3, customer.getEmail());
            customerStatement.executeUpdate();
        }

        for (Booking booking : bookings) {
            PreparedStatement bookingStatement = connection.prepareStatement("INSERT INTO bookings (id, roomId, customerId, startDate, endDate, isPaid) VALUES (?, ?, ?, ?, ?, ?)");
            bookingStatement.setInt(1, booking.getId());
            bookingStatement.setInt(2, booking.getRoomId());
            bookingStatement.setInt(3, booking.getCustomerId());
            bookingStatement.setDate(4, (java.sql.Date) booking.getStartDate());
            bookingStatement.setDate(5, (java.sql.Date) booking.getEndDate());
            bookingStatement.setBoolean(6, booking.isPaid());
            bookingStatement.executeUpdate();
        }
    }
}