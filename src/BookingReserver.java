
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class BookingReserver {

    public static void main(String[] args) {
        BookingList dummy = new BookingList();
    }

    public static void arrangeBooking() // the class functionality
    {   
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("du kan altid trykke q eller 0 for at gå tilbage til menuen");
            System.out.println("Indtast venligst dato og tid: " + Booking.formatterString);
            String time = scanner.nextLine();
            if (time.equals("q")|| time.equals("0")) //adds the option to go back to menu
                return;
            LocalDateTime wantTime = LocalDateTime.parse(time, Booking.formatter);
            LocalDateTime suggestedTime = HarrySalon.bookingList.hasTimeAt(wantTime);

            if (!suggestedTime.equals(wantTime)) // checks if time is not available
            {
                if (HarrySalon.bookingList.isShopOpen(wantTime))
                {
                    System.out.print("Denne tid er desværre allerede booket. ");
                }
                else
                {
                    System.out.print("Denne tid er der desværre lukket i salonen. ");
                }
                System.out.println("Den tidligste tid derefter vil være: " + suggestedTime.format(DateTimeFormatter.ofPattern(Booking.formatterString)));

                arrangeBooking(); //attempts a new booking from the beginning
                return;
            }

            System.out.print("Indtast kundenavn: ");
            String name = scanner.nextLine();
            if (name.equals("q")|| name.equals("0"))
                return;
            System.out.print("Indtast tlf.nummer: ");
            String phoneNumber = scanner.nextLine();
            if (phoneNumber.equals("q")|| phoneNumber.equals("0"))
                return;
            Booking booking = new Booking(name, phoneNumber, time);
            HarrySalon.bookingList.add(booking); // adds a new booking to bookinglist

        } catch (Exception e) {  // if there is any errors it would print the error (like non-valid input)
            System.out.println("Ugyldigt formatteret dato!");
            arrangeBooking();
        }
    }
}