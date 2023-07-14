package systemManagementService;

import customerservice.CustomerRegistration;
import generators.BillGenerator;
import generators.ReportsGenerator;
import roomservice.RoomBooking;
import roomservice.RoomRegistration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HotelApp {
    public void run() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hotel", "root", "aghvankey");

            RoomRegistration roomRegistration = new RoomRegistration(connection);
            CustomerRegistration customerRegistration = new CustomerRegistration(connection);
            RoomBooking roomBooking = new RoomBooking(connection);
            BillGenerator billGenerator = new BillGenerator(connection);
            ReportsGenerator reportsGenerator = new ReportsGenerator(connection);
            SystemStateManager systemStateManager = new SystemStateManager(connection);

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("Welcome to the Hotel Room entities.Booking System!");
                System.out.println("Please select an option:");
                System.out.println("1. Add a room");
                System.out.println("2. Add a customer");
                System.out.println("3. Book a room");
                System.out.println("4. Generate bill");
                System.out.println("5. Generate report");
                System.out.println("6. Save system state");
                System.out.println("7. Load system state");
                System.out.println("8. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        System.out.println("Enter room ID:");
                        int roomId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        System.out.println("Enter room type (Single, Double, Deluxe):");
                        String roomType = scanner.nextLine();

                        roomRegistration.addRoom(roomId, roomType);
                        break;
                    case 2:
                        System.out.println("Enter customer ID:");
                        int customerId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        System.out.println("Enter customer name:");
                        String name = scanner.nextLine();

                        System.out.println("Enter customer email:");
                        String email = scanner.nextLine();

                        customerRegistration.addCustomer(customerId, name, email);
                        break;
                    case 3:
                        System.out.println("Enter customer ID:");
                        int bookingCustomerId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        System.out.println("Enter room type (Single, Double, Deluxe):");
                        String bookingRoomType = scanner.nextLine();

                        System.out.println("Enter start date (yyyy-MM-dd):");
                        String startDateStr = scanner.nextLine();
                        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);

                        System.out.println("Enter end date (yyyy-MM-dd):");
                        String endDateStr = scanner.nextLine();
                        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);

                        roomBooking.bookRoom(bookingRoomType, bookingCustomerId, startDate, endDate);
                        break;
                    case 4:
                        System.out.println("Enter booking ID:");
                        int bookingId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        System.out.println("Enter room type (Single, Double, Deluxe):");
                        String billRoomType = scanner.nextLine();

                        System.out.println("Enter file path to save the bill:");
                        String billFilePath = scanner.nextLine();

                        billGenerator.generateBill(bookingId, billRoomType, billFilePath);
                        break;
                    case 5:
                        System.out.println("Enter room ID:");
                        int roomIdForReport = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        System.out.println("Enter file path to save the report:");
                        String reportFilePath = scanner.nextLine();

                        reportsGenerator.generateReport(roomIdForReport, reportFilePath);
                        break;
                    case 6:
                        System.out.println("Enter file path to save the system state:");
                        String saveFilePath = scanner.nextLine();

                        systemStateManager.saveState(saveFilePath);
                        break;
                    case 7:
                        System.out.println("Enter file path to load the system state:");
                        String loadFilePath = scanner.nextLine();

                        systemStateManager.loadState(loadFilePath);
                        break;
                    case 8:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
