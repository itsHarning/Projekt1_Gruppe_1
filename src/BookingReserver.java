import java.util.Scanner;

public class Bookingsystem {private static boolean[] bookings = new boolean[9];  // 8 timer fra 10 til 17 (fordi behandlingen tager en time)

    public static void main(String[] args) {
        arrangeBooking();
    }

    public static void arrangeBooking() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("vælg venligst en tid indenfor tidsperioden kl. 10-17.");

        try {
            System.out.print("Indtast venligst navn");
            String navn = scanner.nextLine();

            System.out.print("Indtast venligst Tlf. nummer");
            String tlfNummer = scanner.nextLine();

            System.out.println("Indtast venligst hvilken tidsperiode mellem (10-17).");
            int tid = scanner.nextInt();

            Booking booking = new Booking(navn, tlfNummer, tid);











            // TODO: GIVE TO TOBIAS
            if (tid == 0) {  // Afslut programmet
                return;
            } else if (tid < 10 || tid > 17) {
                System.out.println("tidsperioden er ugyldig, vælg venligst en ny tid.");
            } else if (bookings[tid - 10]) {
                System.out.println(tid + "tidsperioden er desværre allerede booket, vælg venligst en ny tid.");
            } else {
                bookings[tid - 10] = true;
                System.out.println("Du har booket tidsperioden "+tid+" ");
            }
        } catch (Exception e) {  // Hvis der opstår en fejl (f.eks. ugyldigt input)
            System.out.println(e.getMessage());
            scanner.nextLine();  // Ryd scanner-bufferen
        }

    }

}