import java.io.*;
import java.util.*;

class Room {
    int roomNo;
    String category;
    double price;
    boolean isBooked;

    Room(int roomNo, String category, double price, boolean isBooked) {
        this.roomNo = roomNo;
        this.category = category;
        this.price = price;
        this.isBooked = isBooked;
    }

    public String toString() {
        return roomNo + "," + category + "," + price + "," + isBooked;
    }
}

class Booking {
    int bookingId;
    int roomNo;
    String guestName;
    int nights;
    double totalAmount;
    String status;

    Booking(int bookingId, int roomNo, String guestName, int nights, double totalAmount, String status) {
        this.bookingId = bookingId;
        this.roomNo = roomNo;
        this.guestName = guestName;
        this.nights = nights;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String toString() {
        return bookingId + "," + roomNo + "," + guestName + "," + nights + "," + totalAmount + "," + status;
    }
}

public class HotelReservationSystem {

    static final String ROOM_FILE = "rooms.txt";
    static final String BOOKING_FILE = "bookings.txt";

    static ArrayList<Room> roomList = new ArrayList<>();
    static ArrayList<Booking> bookingList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextBookingId = 1;

    public static void main(String[] args) {

        loadRooms();
        loadBookings();

        System.out.println("===================================");
        System.out.println("     HOTEL RESERVATION SYSTEM");
        System.out.println("===================================");

        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt();

            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewAllBookings();
                    break;
                case 5:
                    viewAllRooms();
                    break;
                case 6:
                    running = false;
                    System.out.println("Thank you for using the system. Bye!");
                    break;
                default:
                    System.out.println("Invalid choice, try again.\n");
            }
        }

        sc.close();
    }

    static void printMenu() {
        System.out.println("\n---- MENU ----");
        System.out.println("1. Search available rooms");
        System.out.println("2. Book a room");
        System.out.println("3. Cancel a booking");
        System.out.println("4. View all bookings");
        System.out.println("5. View all rooms");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void loadRooms() {
        File file = new File(ROOM_FILE);

        if (!file.exists()) {
            roomList.add(new Room(101, "Standard", 1500, false));
            roomList.add(new Room(102, "Standard", 1500, false));
            roomList.add(new Room(103, "Standard", 1500, false));
            roomList.add(new Room(201, "Deluxe", 2500, false));
            roomList.add(new Room(202, "Deluxe", 2500, false));
            roomList.add(new Room(301, "Suite", 4500, false));
            roomList.add(new Room(302, "Suite", 4500, false));
            saveRooms();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int roomNo = Integer.parseInt(parts[0]);
                String category = parts[1];
                double price = Double.parseDouble(parts[2]);
                boolean isBooked = Boolean.parseBoolean(parts[3]);
                roomList.add(new Room(roomNo, category, price, isBooked));
            }
        } catch (IOException e) {
            System.out.println("Error reading room file: " + e.getMessage());
        }
    }

    static void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOM_FILE))) {
            for (Room r : roomList) {
                pw.println(r.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving room file: " + e.getMessage());
        }
    }

    static void loadBookings() {
        File file = new File(BOOKING_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int bookingId = Integer.parseInt(parts[0]);
                int roomNo = Integer.parseInt(parts[1]);
                String guestName = parts[2];
                int nights = Integer.parseInt(parts[3]);
                double totalAmount = Double.parseDouble(parts[4]);
                String status = parts[5];
                bookingList.add(new Booking(bookingId, roomNo, guestName, nights, totalAmount, status));

                if (bookingId >= nextBookingId) {
                    nextBookingId = bookingId + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading booking file: " + e.getMessage());
        }
    }

    static void saveBookings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKING_FILE))) {
            for (Booking b : bookingList) {
                pw.println(b.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving booking file: " + e.getMessage());
        }
    }

    static void searchRooms() {
        System.out.print("Enter category to search (Standard/Deluxe/Suite) or press enter for all: ");
        String category = sc.nextLine().trim();

        System.out.println("\nAvailable Rooms:");
        System.out.println("RoomNo\tCategory\tPrice/Night");
        System.out.println("--------------------------------------");

        boolean found = false;
        for (Room r : roomList) {
            if (r.isBooked) continue;

            if (category.isEmpty() || r.category.equalsIgnoreCase(category)) {
                System.out.println(r.roomNo + "\t" + r.category + "\t\t" + r.price);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available rooms found for that category.");
        }
    }

    static void bookRoom() {
        System.out.print("Enter room number to book: ");
        int roomNo = readInt();

        Room targetRoom = null;
        for (Room r : roomList) {
            if (r.roomNo == roomNo) {
                targetRoom = r;
                break;
            }
        }

        if (targetRoom == null) {
            System.out.println("Room not found.");
            return;
        }

        if (targetRoom.isBooked) {
            System.out.println("Sorry, this room is already booked.");
            return;
        }

        System.out.print("Enter guest name: ");
        String guestName = sc.nextLine().trim();

        System.out.print("Enter number of nights: ");
        int nights = readInt();

        if (nights <= 0) {
            System.out.println("Nights should be at least 1.");
            return;
        }

        double totalAmount = targetRoom.price * nights;

        System.out.println("\n--- Booking Summary ---");
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room No    : " + targetRoom.roomNo);
        System.out.println("Category   : " + targetRoom.category);
        System.out.println("Nights     : " + nights);
        System.out.println("Total Bill : " + totalAmount);

        System.out.print("\nProceed with payment simulation? (yes/no): ");
        String confirm = sc.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Booking cancelled by user.");
            return;
        }

        boolean paymentSuccess = simulatePayment(totalAmount);

        if (!paymentSuccess) {
            System.out.println("Payment failed. Booking not confirmed.");
            return;
        }

        targetRoom.isBooked = true;
        saveRooms();

        Booking newBooking = new Booking(nextBookingId, targetRoom.roomNo, guestName, nights, totalAmount, "CONFIRMED");
        bookingList.add(newBooking);
        nextBookingId++;
        saveBookings();

        System.out.println("\nPayment successful! Booking confirmed.");
        System.out.println("Your Booking ID is: " + newBooking.bookingId);
    }

    static boolean simulatePayment(double amount) {
        System.out.println("\nProcessing payment of Rs " + amount + " ...");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
        }

        System.out.println("Payment of Rs " + amount + " received successfully.");
        return true;
    }

    static void cancelBooking() {
        System.out.print("Enter your booking ID to cancel: ");
        int bookingId = readInt();

        Booking targetBooking = null;
        for (Booking b : bookingList) {
            if (b.bookingId == bookingId) {
                targetBooking = b;
                break;
            }
        }

        if (targetBooking == null) {
            System.out.println("Booking ID not found.");
            return;
        }

        if (targetBooking.status.equals("CANCELLED")) {
            System.out.println("This booking is already cancelled.");
            return;
        }

        targetBooking.status = "CANCELLED";

        for (Room r : roomList) {
            if (r.roomNo == targetBooking.roomNo) {
                r.isBooked = false;
                break;
            }
        }

        saveBookings();
        saveRooms();

        System.out.println("Booking ID " + bookingId + " has been cancelled.");
        System.out.println("Refund of Rs " + targetBooking.totalAmount + " will be processed.");
    }

    static void viewAllBookings() {
        if (bookingList.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }

        System.out.println("\nBookingID\tRoomNo\tGuest\t\tNights\tAmount\tStatus");
        System.out.println("-------------------------------------------------------------------");

        for (Booking b : bookingList) {
            System.out.println(b.bookingId + "\t\t" + b.roomNo + "\t" + b.guestName + "\t\t"
                    + b.nights + "\t" + b.totalAmount + "\t" + b.status);
        }
    }

    static void viewAllRooms() {
        System.out.println("\nRoomNo\tCategory\tPrice/Night\tStatus");
        System.out.println("-------------------------------------------------");

        for (Room r : roomList) {
            String status = r.isBooked ? "Booked" : "Available";
            System.out.println(r.roomNo + "\t" + r.category + "\t\t" + r.price + "\t\t" + status);
        }
    }
}