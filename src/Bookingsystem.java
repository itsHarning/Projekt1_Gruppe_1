import java.util.Scanner;

public class Bookingsystem {private static boolean[] bookings = new boolean[9];  // 8 timer fra 10 til 17 (fordi behandlingen tager en time)

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("vælg venligst en tid indenfor tidsperioden kl. 10-17.");

        boolean running = true;  // Kontrollerer, om systemet skal køre, hvis "korrekt" tid er valgt
        while (running) {
            try {
                System.out.print("Vælg et tidspunkt mellem (10-17): ");
                int tid = scanner.nextInt();

                if (tid == 0) {  // Afslut programmet
                    running = false;
                } else if (tid < 10 || tid > 17) {
                    System.out.println("tidsperioden er ugyldig, vælg venligst en ny tid.");
                } else if (bookings[tid - 10]) {
                    System.out.println(tid + "tidsperioden er desværre allerede booket, vælg venligst en ny tid.");
                } else {
                    bookings[tid - 10] = true;
                    running = false;  // fjerner man denne linje af kode fra programmet, kan progammet ikke komme videre til næste del af koden
                    System.out.println("Du har booket tidsperioden "+tid+" ");
                }
            } catch (Exception e) {  // Hvis der opstår en fejl (f.eks. ugyldigt input)
                System.out.println("hov noget gik galt, vælg venligst en tid mellem Kl. 10:00 og 17:00");
                scanner.nextLine();  // Ryd scanner-bufferen
            }
        }

        System.out.println("vælg venligst hvilken behandling du ønsker.");
    }

}