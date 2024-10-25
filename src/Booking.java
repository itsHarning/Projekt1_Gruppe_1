import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Booking implements Comparable<Booking> {
    String customerName;
    String phoneNumber;
    int bookedMinutes;
    LocalDate bookedDate;
    LocalTime startingTime;
    LocalTime endTime;
    // TODO: not so much todo, just suggesting endtime is 1 minute before proper end time
    //  so that starting where another booking ends doesn't flag as overlapping.
    DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");
    Receipt receipt = new Receipt(); // receipt class doesn't have any function yet

    Booking(String name, String phoneNum, String startingTime){
        customerName = name;
        phoneNumber = phoneNum;
        this.bookedMinutes = bookedMinutes;
        this.startingTime = LocalTime.parse(startingTime, Formatter); // converts from a string to LocalDateTime
        endTime = this.startingTime.plusMinutes(bookedMinutes); // adds the booked minutes to the start time
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
        if (this.isAfter(other)) // if after, return POSITIVE time between bookings.
        {
            return this.startingTime.getHour() - other.endTime.getHour();
        }
        else if (this.isBefore(other)) // if before, return NEGATIVE time between bookings.
        {
            return this.endTime.getHour() - other.startingTime.getHour();
        }

        // If neither before nor after, return 0 to indicate overlap.
        return disparity;
    }

    // Checks if this Booking is scheduled after another
    public boolean isAfter(Booking other)
    {
        // First check if same date.
        if (this.bookedDate.getDayOfYear() == other.bookedDate.getDayOfYear() )
        {
            // TODO: Make account for minutes.
            return 0 < this.startingTime.getHour() - other.endTime.getHour();
        }
        return 0 < this.bookedDate.getDayOfYear() - other.bookedDate.getDayOfYear();
    }

    // Checks if this Booking is scheduled before another
    public boolean isBefore(Booking other)
    {
        // First check if same date.
        if (this.bookedDate.getDayOfYear() == other.bookedDate.getDayOfYear() )
        {
            // TODO: Make account for minutes.
            return 0 > this.endTime.getHour() - other.startingTime.getHour();
        }
        return 0 > this.bookedDate.getDayOfYear() - other.bookedDate.getDayOfYear();
    }
}
