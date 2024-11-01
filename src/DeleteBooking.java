
import java.util.Scanner;

public class DeleteBooking
{
    public static void main(String[] args)
    {
        BookingList dummy = new BookingList();
        dummy.add(new Booking("Jens Hansen", "35353535", "2024 10 25 16:00"));
        deleteBooking(dummy);
    }

    public static void deleteBooking(BookingList bookings)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Slet booking.");

        System.out.print("Indtast telefonnummer associeret med den booking der ønskes slettet: ");
        String phonenumber = scanner.nextLine();
        Booking booking = bookings.getBookingNumber(phonenumber);
        bookings.remove(booking);
    }
}