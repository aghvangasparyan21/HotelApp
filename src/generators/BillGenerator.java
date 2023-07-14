package generators;

import operationwithFiles.FileUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BillGenerator {
    private Connection connection;

    public BillGenerator(Connection connection) {
        this.connection = connection;
    }

    public void generateBill(int bookingId, String roomType, String filePath) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT b.startDate, b.endDate, c.name, c.email FROM bookings b INNER JOIN customers c ON b.customerId = c.id WHERE b.id = ?");
            statement.setInt(1, bookingId);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Date startDate = result.getDate("startDate");
                Date endDate = result.getDate("endDate");
                String customerName = result.getString("name");
                String email = result.getString("email");

                // Calculate bill details
                double pricePerDay = 0;
                if (roomType.equals("Single")) {
                    pricePerDay = 20;
                } else if (roomType.equals("Double")) {
                    pricePerDay = 35;
                } else if (roomType.equals("Deluxe")) {
                    pricePerDay = 55;
                }

                long numDays = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
                double totalPrice = pricePerDay * numDays;
                double taxes = totalPrice * 0.2;
                double serviceFee = (totalPrice + taxes) * 0.1;
                double totalAmount = totalPrice + taxes + serviceFee;

                // Generate bill content
                StringBuilder billContent = new StringBuilder();
                billContent.append("Room Type: ").append(roomType).append("\n");
                billContent.append("entities.Customer Name: ").append(customerName).append("\n");
                billContent.append("entities.Customer Email: ").append(email).append("\n");
                billContent.append("entities.Booking Period: ").append(startDate).append(" to ").append(endDate).append("\n");
                billContent.append("Price Per Day: $").append(pricePerDay).append("\n");
                billContent.append("Total Price: $").append(totalPrice).append("\n");
                billContent.append("Taxes: $").append(taxes).append("\n");
                billContent.append("Service Fee: $").append(serviceFee).append("\n");
                billContent.append("Total Amount: $").append(totalAmount);

                // Display bill in console
                System.out.println(billContent.toString());

                // Save bill as text file
                FileUtils.writeToFile(filePath, billContent.toString());
                System.out.println("Bill saved successfully.");
            } else {
                System.out.println("Invalid booking ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error generating bill: " + e.getMessage());
        }
    }
}

