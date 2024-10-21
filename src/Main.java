public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // ints are temporarily placed here for ease of function
        // will likely be moved to a separate class later
        int standardCut = 30;
        int longHair = 15;
        int shave = 15;
        int dye = 30;
        // formatting of the date/time can easily be changes
        Booking booking = new Booking("Test Booking",standardCut+longHair,"2024 10 21 10:00");

        System.out.println(booking);
    }
}