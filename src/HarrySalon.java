import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HarrySalon {
    static Scanner sc = new Scanner(System.in);
    static String[] options = {"Book Tid", "Slet Booking", "Stop Programmet", "Log Ind"};
    static String[] extraOptions = {"Log Ud","Se Økonomi", "gør noget andet"};
    static String password = "HairyHarry";
    static boolean loggedIn = false;

    static BookingList bookingList = new BookingList();

    public static void main(String[] args) {
        mainMenu();
    }
    static void mainMenu(){

        while (true) {
            System.out.println("\n");
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
    static void wrongInput(){
        System.out.println("ikke et valid input");
    }
    static void getInput(){

        while(true) {
            try {
                switch (sc.nextInt()) {
                    case 1:
                        BookingReserver.arrangeBooking(bookingList);
                        return;
                    case 2:
                        System.out.println("du sletter en tid");
                        return;
                    case 3:
                        System.out.println("du stopper programmet");
                        return;
                    case 4:
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
                    case 5:
                        if(loggedIn)
                            System.out.println("du ser økonomi");
                        else{
                            wrongInput();
                            break;
                        }
                        return;

                    case 6:
                        if(loggedIn)
                            System.out.println("du gør noget andet");
                        else{
                            wrongInput();
                            break;
                        }
                        return;

                    default:
                        wrongInput();
                        break;
                }

            } catch (InputMismatchException e) {
                wrongInput();
            }

        }
    }
}
