import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    String customerName;
    int bookedMinutes;
    LocalDateTime startingTime;
    LocalDateTime endTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");
    Receipt receipt = new Receipt(); // receipt class doesn't have any function yet

    Booking(String name, int bookedMinutes, String startingTime){
        customerName = name;
        this.bookedMinutes = bookedMinutes;
        this.startingTime = LocalDateTime.parse(startingTime, formatter); // converts from a string to LocalDateTime
        endTime = this.startingTime.plusMinutes(bookedMinutes); // adds the booked minutes to the start time
    }

    void payBill(){

    }

// commented out, as the method is probably not needed
    // adds the booked amount of minutes to the start time to calculate the end time
    // LocalDateTime endTime(){
    //     return startingTime.plusMinutes(amountOfBookedMinutes);
    // }

    void timeTaken(){

    }

    // standard toString method, it might make sense later on to make a seperate print method
    public String toString() {
        return customerName+" har tid "+ startingTime +" til "+endTime;
    }
}
