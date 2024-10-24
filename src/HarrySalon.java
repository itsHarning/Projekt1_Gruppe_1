import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HarrySalon {
    static Scanner sc = new Scanner(System.in);
    static String[] options = {"Book Tid", "Slet Booking", "Stop Programmet", "Indtast Kodeord"};
    static String[] extraOptions = {"Se Økonomi", "gør noget andet"};
    static String password = "HairyHarry";
    static boolean loggedIn = false;
    public static void main(String[] args) {
        mainMenu();
    }
    static void mainMenu(){

        while (true) {
            System.out.println("\n\n\n\n\n\n\n");
            for (int i = 0; i < options.length; i++)
                System.out.println("tryk " + (i + 1) + ": " + options[i]);
            if (loggedIn) {
                for (int i = 0; i < extraOptions.length; i++)
                    System.out.println("tryk " + (i + 1 + options.length) + ": " + extraOptions[i]);
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
                        System.out.println("du booker en tid");
                        return;
                    case 2:
                        System.out.println("du sletter en tid");
                        return;
                    case 3:
                        System.out.println("du stopper programmet");
                        return;
                    case 4:
                        sc.nextLine();
                        String d = sc.nextLine();
                        if (d.equals(password))
                        {
                            System.out.println("Korrekt");
                            loggedIn = true;
                        }
                        else
                            System.out.println("Forkert");
                        return;
                    case 5:
                        if(loggedIn)
                            System.out.println("du ser økonomi");
                        else{
                            wrongInput();
                        }
                        break;
                    case 6:
                        if(loggedIn)
                            System.out.println("du gør noget andet");
                        else{
                            wrongInput();
                        }
                        break;
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
