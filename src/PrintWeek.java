import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class PrintWeek {

    static Scanner sc = new Scanner(System.in);
    static boolean quitToMenu;

    public static void main(String[] args) {
        chooseTimeSpan();
    }
    // this method is called from Harrysalon when you choose "Se Bookings".
    // it calls choosetime 2 times to get 2 dates from the users, startdate and enddate,
    // then it calls printtimespan to print all appointments each day from the startdate to the enddate
    public static void chooseTimeSpan(){
       /* TestingNames.addLastNames();
        TestingNames.randomize();
        for(int i = 0; i <2; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,11,01)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
        for(int i = 0; i <2; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,10,31)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }*/
        System.out.println("du kan altid trykke q eller 0 for at gå tilbage til menuen");
        System.out.println("Start dato (yyyy MM dd): ");
        LocalDate startDate = chooseTime();
        LocalDate endDate = LocalDate.now();
        if (!quitToMenu) {
            System.out.println("Slut dato (yyyy MM dd): ");
            endDate = chooseTime();
        }
        if (!quitToMenu)
            PrintTimeSpan(startDate, endDate);
        else
            quitToMenu = false;
    }
    //it uses a scanner to get input from the user then it converts it to a date
    // it uses try and catch to make sure they write a date in the correct format, then it returns a date
    static LocalDate chooseTime(){
        while (true){
            try {
                DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
                String temp = sc.nextLine();
                if (temp.equals("q") ||temp.equals("0")){
                    quitToMenu = true;
                    return LocalDate.now();
                }
                LocalDate date = LocalDate.parse(temp, Formatter);
                return date;
            }
            catch (DateTimeParseException e){
                System.out.println("ikke en gyldig dato");

            }
        }
    }
    // gets each day from the startdate to the enddate,
    // then it calls printDay to get a string with all appointments from that day, with all of the days
    // then it calls PrintChosendays and gives it a list with all the days
    public  static void PrintTimeSpan(LocalDate startDate, LocalDate endDate){
        ArrayList<String> days = new ArrayList<>();
       LocalDate date = startDate;
       while (!date.isAfter(endDate)){
           days.add(HarrySalon.bookingList.formatDay(date));
           date = date.plusDays(1);
       }
        System.out.println(startDate + ", " + endDate);
       PrintChosenDays(days);
    }
    //splits all of the days and adds them back together, but side by side, and then prints it
    //so you get all days side by side with all the appointments downwards
    public  static void PrintChosenDays(ArrayList<String> days){
       /* for (String i : days)
            System.out.println(i);*/
        int amountOfChar = 37;
        int longest = 0;
        for (String i : days){
            String[] d = i.split("\n");
            if (longest < d.length) {
                longest = d.length;
            }
        }
        String[] Week = new String[longest];
        for (int i = 0; i < Week.length; i++){
            Week[i] = "";
        }
        for (int i = 0; i < days.size(); i++){
            String[] day = days.get(i).split("\n");
            for (int j = 0; j < Week.length; j++){
                if (day.length > j){
                    while (day[j].length() < amountOfChar)
                    {
                        day[j] += " ";
                    }
                    Week[j] +=  day[j];
                }
                else
                    Week[j] += "                                     ";
            }
        }
        for (String i : Week){
            System.out.println(i);
        }
        System.out.println("Tryk Enter for at Gå Tilbage Til Menuen");
        sc.nextLine();
    }
}
