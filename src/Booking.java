import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class Booking implements Comparable<Booking> {
    String customerName;
    String phoneNumber;
    int bookedMinutes;
    LocalDateTime startingTime;
    LocalDateTime endTime;
    static String formatterString = "yyyy MM dd HH:mm";
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterString); // sets up a formatter for how LocalDateTime should parse strings
    Receipt receipt = new Receipt(); // receipt class doesn't have much of a function yet

    Booking(String name, String phoneNum, String startingTime){
        customerName = name;
        phoneNumber = phoneNum;
        this.bookedMinutes = 60;
        this.startingTime = LocalDateTime.parse(startingTime, formatter); // converts from a string to LocalDateTime
        endTime = this.startingTime.plusMinutes(bookedMinutes-1); // adds the booked minutes to the start time
    }

    void payBill(){

    }

    void timeTaken(){

    }

    // standard toString method, it might make sense later on to make a separate print method
    public String toString() {
        return "navn: "+customerName+"\ttlf: "+phoneNumber+"\ntid: "+ startingTime +" til "+endTime;
    }

    public void printBooking() {
        System.out.println("navn: "+customerName+"\ttlf: "+phoneNumber);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("dato: "+startingTime);
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println("\ttid: "+startingTime.format(formatter)+"-"+(endTime.plusMinutes(1)).format(formatter));
        formatter = DateTimeFormatter.ofPattern(formatterString);
    }

    // Checks time between this and another Booking.
    // Returns 0 if overlapping.
    public int compareTo(Booking other) {
        // First compare the day of the date.
        int difference = this.startingTime.getDayOfYear() - other.startingTime.getDayOfYear();

        // Return if not on the same day
        if (difference != 0) return difference;

        // Note: in two statements to account for duration.
        // Whether returning POSITIVE or NEGATIVE difference is IMPORTANT for sorting function!
        if (this.isAfterDate(other)) // if after, return POSITIVE time between bookings.
        {
            return (int)other.endTime.until(this.startingTime, ChronoUnit.MINUTES);
        }
        else if (this.isBeforeDate(other)) // if before, return NEGATIVE time between bookings.
        {
            return -((int)this.endTime.until(other.startingTime, ChronoUnit.MINUTES));
        }

        // If neither before nor after, return 0 to indicate overlap.
        return difference;
    }

    // Checks if this Booking is scheduled before another
    public boolean isBeforeDate(Booking other){
        return this.isBeforeDate(other.startingTime);
    }

    // Checks if this Booking is scheduled after another
    public boolean isAfterDate(Booking other){
        return this.isAfterDate(other.endTime);
    }

    // checks if the booking is before a given date
    public boolean isBeforeDate(LocalDateTime date){
        return this.endTime.isBefore(date);
    }

    // checks if the booking is after a given date
    public boolean isAfterDate(LocalDateTime date){
        return this.startingTime.isAfter(date);
    }
}
