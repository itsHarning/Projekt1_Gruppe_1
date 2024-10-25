import java.util.Scanner;

public class BookingReserver {private static boolean[] bookings = new boolean[9];  // 8 hours from the timespan 10 -17

    public static void main(String[] args) {
        BookingList dummy =new BookingList();
        arrangeBooking(dummy);
    }

    public static void arrangeBooking(BookingList bookinglist)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please choose a timeperiod between 10-17");

        try {
            System.out.println("ask date");
            String date = scanner.nextLine();

            System.out.print("ask name");
            String name = scanner.nextLine();

            System.out.print("ask phone number.");
            String phonenumber = scanner.nextLine();

            System.out.println("Indtast venligst hvilken tidsperiode mellem (10-17).");
            String time = scanner.nextLine();

            Booking booking = new Booking(name, phonenumber, time);

        } catch (Exception e) {  // Hvis der opstår en fejl (f.eks. ugyldigt input)
            System.out.println(e.getMessage());
            scanner.nextLine();  // Ryd scanner-bufferen
        }

    }

}