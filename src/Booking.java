import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking implements Comparable<Booking> {
    String customerName;
    String phoneNumber;
    int bookedMinutes=60;
    LocalDate bookedDate;
    LocalDateTime startingTime;
    LocalDateTime endTime;
    // TODO: not so much todo, just suggesting endtime is 1 minute before proper end time
    //  so that starting where another booking ends doesn't flag as overlapping.
    static String formatterString = "yyyy MM dd HH:mm";
    DateTimeFormatter Formatter = DateTimeFormatter.ofPattern(formatterString);
    Receipt receipt = new Receipt(); // receipt class doesn't have any function yet

    Booking(String name, String phoneNum, String startingTime){
        customerName = name;
        phoneNumber = phoneNum;
        this.bookedMinutes = bookedMinutes;
        this.startingTime = LocalDateTime.parse(startingTime, Formatter); // converts from a string to LocalDateTime
        endTime = this.startingTime.plusMinutes(bookedMinutes-1); // adds the booked minutes to the start time
    }

    void payBill(){

    }

    void timeTaken(){

    }

    // standard toString method, it might make sense later on to make a seperate print method
    public String toString() {
        return customerName+" har tid "+ startingTime +" til "+endTime;
    }

    // Checks time between this and another Booking.
    // Returns 0 if overlapping.
    public int compareTo(Booking other)
    {
        // First compare the day of the date.
        int disparity = this.bookedDate.getDayOfYear() - other.bookedDate.getDayOfYear();

        // Return if not on the same day
        if (disparity != 0) return disparity;

        // Compare time of day TODO: Make account for minutes.
        // Note: in two statements to account for duration.
        // Whether returning POSITIVE or NEGATIVE difference is IMPORTANT for sorting function!
        if (this.isAfterDate(other)) // if after, return POSITIVE time between bookings.
        {
            return this.startingTime.getHour() - other.endTime.getHour();
        }
        else if (this.isBeforeDate(other)) // if before, return NEGATIVE time between bookings.
        {
            return this.endTime.getHour() - other.startingTime.getHour();
        }

        // If neither before nor after, return 0 to indicate overlap.
        return disparity;
    }

    // Checks if this Booking is scheduled after another
    public boolean isAfterDate(Booking other) {
        // First check if same date.
        if (this.bookedDate.getDayOfYear() == other.bookedDate.getDayOfYear() )
        {
            // TODO: Make account for minutes.
            return 0 < (this.startingTime.getHour() + this.startingTime.getMinute()) - (other.endTime.getHour() + other.endTime.getMinute());
        }
        return 0 < this.bookedDate.getDayOfYear() - other.bookedDate.getDayOfYear();
    }

    // Checks if this Booking is scheduled before another
    public boolean isBeforeDate(Booking other) {
        // First check if same date.
        if (this.bookedDate.getDayOfYear() == other.bookedDate.getDayOfYear() )
        {
            // TODO: Make account for minutes.
            return 0 > (this.endTime.getHour() + this.endTime.getMinute()) - (other.startingTime.getHour()+ this.startingTime.getMinute());
        }
        return 0 > this.bookedDate.getDayOfYear() - other.bookedDate.getDayOfYear();
    }

    public boolean isBeforeDate(LocalDateTime date){
        return this.startingTime.isBefore(date);
    }

    public boolean isAfterDate(LocalDateTime date){
        return this.startingTime.isAfter(date);
    }
}
