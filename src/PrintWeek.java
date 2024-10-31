import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PrintWeek {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {


        chooseTimeSpan();
    }
    public  static void chooseTimeSpan(){
      /*  TestingNames.addLastNames();
        TestingNames.randomize();
       for(int i = 0; i <2; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,10,29)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
        for(int i = 0; i <2; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,11,01)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
        for(int i = 0; i <2; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,10,31)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
        //PrintTimeSpan(LocalDate.of(2024,10,28), LocalDate.of(2024,11,5));
*/
        System.out.println("Start dato: ");
        LocalDate startDate = chooseTime();
        System.out.println("Slut dato: ");
        LocalDate endDate = chooseTime();

        PrintTimeSpan(startDate, endDate);
    }
    static LocalDate chooseTime(){
        while (true){
            try {
                DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
                LocalDate date = LocalDate.parse(sc.nextLine(), Formatter);
                return date;
            }
            catch (DateTimeParseException e){
                System.out.println("ikke en gyldig dato");
                sc.nextLine();
            }
        }


       // return date;
    }
    public  static void PrintTimeSpan(LocalDate startDate, LocalDate endDate){
        ArrayList<String> days = new ArrayList<>();
       LocalDate date = startDate;
       while (!date.isAfter(endDate)){
           days.add(HarrySalon.bookingList.printDay2(date));
           date = date.plusDays(1);
       }
        System.out.println(startDate + ", " + endDate);
       PrintChosenDays(days);
    }
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
    }
}
