package generators;

import operationwithFiles.FileUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ReportsGenerator {
    private Connection connection;

    public ReportsGenerator(Connection connection) {
        this.connection = connection;
    }

    public void generateReport(int roomId, String filePath) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT b.startDate, b.endDate, c.name FROM bookings b INNER JOIN customers c ON b.customerId = c.id WHERE b.roomId = ?");
            statement.setInt(1, roomId);
            ResultSet result = statement.executeQuery();

            StringBuilder reportContent = new StringBuilder();
            while (result.next()) {
                Date startDate = result.getDate("startDate");
                Date endDate = result.getDate("endDate");
                String customerName = result.getString("name");

                reportContent.append("entities.Customer Name: ").append(customerName).append("\n");
                reportContent.append("entities.Booking Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
            }

            // Display report in console
            System.out.println(reportContent.toString());

            // Save report as text file
            FileUtils.writeToFile(filePath, reportContent.toString());
            System.out.println("Report saved successfully.");
        } catch (SQLException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}