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
            System.out.print("ask name");
            String name = scanner.nextLine();

            System.out.print("ask phone number.");
            String phonenumber = scanner.nextLine();

            System.out.println("Indtast venligst dato og tid " + Booking.formatterString);
            String time = scanner.nextLine();

            Booking booking = new Booking(name, phonenumber, time);

        } catch (Exception e) {  // if there is any errors it would print the error (like non-valid input)
            System.out.println(e.getMessage());
            scanner.nextLine();  // clears the Buffer-scanner
        }
    }
}