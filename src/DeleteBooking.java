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
        System.out.println("Delete your booking");

        System.out.print("type your phone number.");
        String phonenumber = scanner.nextLine();
        Booking booking = bookings.getBookingTlf(phonenumber);
        bookings.remove(booking);
    }
}