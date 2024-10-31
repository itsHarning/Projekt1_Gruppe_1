import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HarrySalon {
    static Scanner sc = new Scanner(System.in);
    static String[] options = {"Book Tid", "Slet Booking", "Se Bookings", "Log Ind"};
    static String[] extraOptions = {"Log Ud"};
    static String password = "HairyHarry";
    static boolean loggedIn = false;

    public static final BookingList bookingList = new BookingList();

    public static void main(String[] args) {
        mainMenu();
    }
    // prints all strings in options array, if logged in it prints all the extra options too,
    // and prints log out instead of log in
    static void mainMenu(){

        while (true) {
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
            getInput();
        }
    }
    //this is just what it prints when the user writes something that isn't valid
    static void wrongInput(){
        System.out.println("ikke et valid input");
    }
    // uses a scanner in a switchcase to get input from the user
    // and call the method corresponding to which number they wrote
    static void getInput(){

        while(true) {
            try {
                switch (sc.nextInt()) {
                    case 1:
                        BookingReserver.arrangeBooking();
                        return;
                    case 2:
                        DeleteBooking.deleteBooking(bookingList);
                        return;
                    case 3:
                        PrintWeek.chooseTimeSpan();
                        return;
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
                        return;

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
