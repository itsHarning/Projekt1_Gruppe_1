public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // ummidelbart giver det nok egentlig mere mening at have disse tal i receipt klassen eller noget
        // de er midlertidigt her da de er en let måde at teste funktionalitet
        int standardCut = 30;
        int longHair = 15;
        int shave = 15;
        int dye = 30;
        // formatteringen af hvordan datoen indtastes kan nemt ændres
        Booking booking = new Booking("Test Booking",standardCut+longHair,"2024 10 21 10:00");

        System.out.println(booking);
    }
}