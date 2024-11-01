
import java.util.Scanner;

public class DeleteBooking
{
    public static void main(String[] args)
    {
        BookingList dummy = new BookingList();
        dummy.add(new Booking("Jens Hansen", "35353535", "2024 10 25 16:00"));
        deleteBooking(dummy);
    }


    //Delete booking by a given phone number, next reservation will be deleted.
    public static void deleteBooking(BookingList bookings)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Slet booking.");

        System.out.print("Indtast telefonnummer associeret med den booking der ønskes slettet: ");
        String phonenumber = scanner.nextLine();
        if(!phonenumber.equals("q") && !phonenumber.equals("0")){
            Booking booking = bookings.getBookingNumber(phonenumber);
            bookings.remove(booking);
            {
                System.out.println("Næste booking med dette nummer er nu slettet");
            }
        }

    }
}