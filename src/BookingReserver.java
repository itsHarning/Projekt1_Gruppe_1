import java.util.Scanner;

public class BookingReserver {private static boolean[] bookings = new boolean[9];  // 8 hours from the timespan 10 -17

    public static void main(String[] args) {
        BookingList dummy =new BookingList();
    }
    public static void arrangeBooking()
    {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("indtast navn: ");
            String name = scanner.nextLine();

            System.out.print("indtast tlf.nummer:");
            String phoneNumber = scanner.nextLine();

            System.out.println("Indtast venligst dato og tid:" + Booking.formatterString);
            String time = scanner.nextLine();

            Booking booking = new Booking(name, phoneNumber, time);
            if (!HarrySalon.bookingList.add(booking))
            {
                System.out.print("Denne tid er desværre allerede booket:");
                arrangeBooking();
            }

        } catch (Exception e) {  // if there is any errors it would print the error (like non-valid input)
            System.out.println("ugyldig dato");
            scanner.nextLine();  // clears the Buffer-scanner
        }
    }
}