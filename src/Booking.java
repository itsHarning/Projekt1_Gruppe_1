import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    String customerName;
    int bookedMinutes;
    LocalDateTime startingTime;
    LocalDateTime endTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");
    Receipt receipt = new Receipt(); // denne klasse mangler funktionalitet

    Booking(String name, int bookedMinutes, String startingTime){
        customerName = name;
        this.bookedMinutes = bookedMinutes;
        this.startingTime = LocalDateTime.parse(startingTime, formatter); // konvertere fra en String til LocalDateTime
        endTime = this.startingTime.plusMinutes(bookedMinutes); // ligger den bookede mængde minutter til start tiden for at finde slut
    }

    void payBill(){

    }

// kommenteret ud da den ummidelbart ikke er nødvendig
    // tager den givne start tider og ligger den mængde tid bookingen tager til for at udregne slut tid
    // LocalDateTime endTime(){
    //     return startingTime.plusMinutes(amountOfBookedMinutes);
    // }

    void timeTaken(){

    }

    // standard toString metode, giver muligvis mening senere at lave en reel print metode
    public String toString() {
        return customerName+" har tid "+ startingTime +" til "+endTime;
    }
}
