import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PrintWeek {

    public static void main(String[] args) {

        TestingNames.addLastNames();
        TestingNames.randomize();

       for(int i = 0; i <1; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,10,29)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
        for(int i = 0; i <1; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,10,30)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
        for(int i = 0; i <4; i++){
            HarrySalon.bookingList.add(new Booking(TestingNames.getName(), TestingNames.getNumber(), HarrySalon.bookingList.nextAvailableTimeFrom(LocalDate.of(2024,10,31)).format(DateTimeFormatter.ofPattern(Booking.formatterString))));
        }
      /*  LocalDate date = LocalDate.of(2024,10,29);
        String day = bList.printDay2(date);
        LocalDate date2 = LocalDate.of(2024,10,30);
        String day2 = bList.printDay2(date2);
        LocalDate date3 = LocalDate.of(2024,10,31);
        String day3 = bList.printDay2(date3);
       // System.out.println(day);
         ArrayList<String> list = new ArrayList<>();
         list.add(day);
        list.add(day2);
        list.add(day3);
        //PrintChosenDays(list);*/
        PrintTimeSpan(LocalDate.of(2024,10,28), LocalDate.of(2024,10,31));
    }
    public  static void PrintTimeSpan(LocalDate startDate, LocalDate endDate){
        ArrayList<String> days = new ArrayList<>();
       LocalDate date = startDate;
       while (!date.isAfter(endDate)){
           days.add(HarrySalon.bookingList.printDay2(date));
           date = date.plusDays(1);
       }
       PrintChosenDays(days);
    }
    public  static void PrintChosenDays(ArrayList<String> days){
       /* for (String i : days)
            System.out.println(i);*/
        int amountOfChar = 30;
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
                    Week[j] += "                              ";
            }
        }
        for (String i : Week){
            System.out.println(i);
        }
    }
}
