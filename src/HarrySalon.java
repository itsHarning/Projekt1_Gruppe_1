import java.io.Console;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HarrySalon {
    static Scanner sc = new Scanner(System.in);
    static String[] options = { "Book Tid", "Slet Booking", "Se Bookings", "Log Ind"};
    static String[] extraOptions = {"Log Ud"};
    static String password = "HairyHarry";
    static boolean loggedIn = false;

    public static final BookingList bookingList = new BookingList();

    // main for testing!
    public static void main(String[] args) {

        boolean addTestingBookings = false;
        if (addTestingBookings)
        {
            TestingNames.addLastNames();
            TestingNames.randomize();
            for (int i = 0; i < 100; i++)
            {
                HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(
                                TestingNames.getTime())
                        .format(DateTimeFormatter.ofPattern(Booking.formatterString))));
            }
        }

        mainMenu();
    }
    // prints all strings in options array, if logged in it prints all the extra options too,
    // and prints log out instead of log in
    static void mainMenu(){

        boolean run = true;

        if (bookingList.isEmpty()) bookingList.loadFrom();

        while (run) {
            System.out.println();
            if (loggedIn) {
                for (int i = 0; i < options.length - 1; i++)
                    System.out.println("tryk " + (i + 1) + ": " + options[i]);
                for (int i = 0; i < extraOptions.length; i++)
                    System.out.println("tryk " + (i + options.length) + ": " + extraOptions[i]);
            }
            else {
                for (int i = 0; i < options.length; i++)
                    System.out.println("tryk " + (i + 1) + ": " + options[i]);
            }
            System.out.println("tryk 0: Stop Programmet");
            run = getInput();
        }

        bookingList.saveTo();
    }
    //this is just what it prints when the user writes something that isn't valid
    static void wrongInput(){
        System.out.println("ikke et valid input");
    }
    // uses a scanner in a switchcase to get input from the user
    // and call the method corresponding to which number they wrote
    static boolean getInput(){

        while(true) {
            try {
                switch (sc.nextInt()) {
                    case 0: return false;
                    case 1:
                        BookingReserver.arrangeBooking();
                        return true;
                    case 2:
                        DeleteBooking.deleteBooking(bookingList);
                        return true;
                    case 3:
                        PrintWeek.chooseTimeSpan();
                        return true;
                    case 4:
                        // if logged in then it logs you out else it gets input from the user via scanner
                        // it checks if the input is the same as the password, if it is it logs you in, else it doesn't
                        if(loggedIn){
                            loggedIn = false;
                        }
                        else {
                            System.out.println("Kodeord: ");
                            sc.nextLine();
                            String d = sc.nextLine();
                            if (d.equals(password)) {
                                System.out.println("Korrekt");
                                loggedIn = true;
                            } else
                                System.out.println("Forkert");
                        }
                        return true;

                    default:
                        wrongInput();
                        break;
                }

            } catch (InputMismatchException e) {
                wrongInput();
                sc.nextLine();

            }

        }
    }
}
