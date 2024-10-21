import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    String customerName;
    int amountOfBookedTime;
    LocalDateTime startingMinutes;
    LocalDateTime endTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");
    //receipt from receipt class

    Booking(String name, int amountOfBookedMinutes, String startingTime){
        customerName = name;
        this.amountOfBookedTime = amountOfBookedMinutes;
        this.startingMinutes = LocalDateTime.parse(startingTime, formatter);
    }

    void payBill(){

    }

    LocalDateTime endTime(){
        return startingMinutes.plusMinutes(amountOfBookedTime);
    }

    void timeTaken(){

    }

    public String toString() {
        return customerName+" har tid "+ startingMinutes +" til "+endTime();
    }
}
