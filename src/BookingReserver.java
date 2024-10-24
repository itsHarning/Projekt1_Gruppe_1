import java.util.Scanner;

public class BookingReserver {private static boolean[] bookings = new boolean[9];  // 8 hours from the timespan 10 -17

    public static void main(String[] args) {
        arrangeBooking();
    }

    public static void arrangeBooking() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("vælg venligst en tid indenfor tidsperioden kl. 10-17.");

        try {
            System.out.println("ask date");
            String date = scanner.nextLine();

            System.out.print("Indtast venligst navn");
            String name = scanner.nextLine();

            System.out.print("Indtast venligst Tlf. nummer");
            String phoneNum = scanner.nextLine();

            System.out.println("Indtast venligst hvilken tidsperiode mellem (10-17).");
            String time = scanner.nextLine();

            Booking booking = new Booking(name, phoneNum, date, time);

        } catch (Exception e) {  // Hvis der opstår en fejl (f.eks. ugyldigt input)
            System.out.println(e.getMessage());
            scanner.nextLine();  // Ryd scanner-bufferen
        }

    }

}