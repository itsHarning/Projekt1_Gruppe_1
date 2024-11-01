
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class BookingList extends ArrayList<Booking>
{
    private final ArrayList<LocalDate> vacationDays; // repurposed as Holidays.

    // adds given date to vacationDays, ensuring not to add duplicate dates or overlap Bookings.
    // returns false if attempted to add date with registered bookings.
    public boolean addVacation(LocalDate date)
    {
        for (Booking booking : this) // checks if there exists bookings on given date.
        {
            if (booking.startingTime.toLocalDate().isEqual(date)) return false;
        }

        vacationDays.removeIf(d -> d.isEqual(date)); // this is the simplest way to avoid duplicate dates.

        /* explanation of 'Predicate'
        for (LocalDate d : vacationDays)
        {
            if (d.isEqual(date));
            {
                vacationDays.remove(d);
            }
        }
         */

        vacationDays.add(date);
        return true;
    }

    // adds multiple dates as INDIVIDUAL vacation days.
    // returns false if any of given days was not registered successfully.
    public boolean addVacation(LocalDate ... date)
    {
        boolean intersectsBooking = false;

        for (LocalDate d : date)
        {
            if (!this.addVacation(d))
            {
                intersectsBooking = true;
            }
        }
        return !intersectsBooking;
    }

    // adds ALL dates between given dates as vacation days.
    // returns false if any of the days clashed with a registered booking and then does NOT register ANY vacation-days.
    public boolean addVacation(LocalDate startDate, LocalDate endDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        int addedDays = 0;

        while (!startDate.plusDays(addedDays).equals(endDate.plusDays(1)))
        {
            if (getBookingsFor(startDate.plusDays(addedDays)).size() != 0) return false;
            days.add(startDate.plusDays(addedDays));
        }
        for (LocalDate day : days)
        { // Note: addAll does not prevent duplicate dates
            this.addVacation(day);
        }
        return true;
    }

    // removes the given date from vacationDays.
    public boolean removeVacation(LocalDate date)
    {
        return vacationDays.removeIf(d -> d.isEqual(date));
    }

    // removes ALL dates registered within span of given days from vacationDays.
    public boolean removeVacation(LocalDate startDate, LocalDate endDate)
    {
        LocalDate sDate = startDate.minusDays(1); // make inclusive of given dates.
        LocalDate eDate = endDate.plusDays(1);

        return vacationDays.removeIf(d -> (d.isAfter(sDate) && d.isBefore(eDate)));
    }

    // Checks if time coincides with weekend, vacation or closing hours.
    public boolean isShopOpen(LocalDateTime time)
    {
        if (isWeekend(time.toLocalDate())) return false; // checks if weekend.
        if (time.getHour() < 10 || time.getHour() > 17) return false; // checks opening hours.
        return !isVacation(time.toLocalDate());
    }

    // Accounts for arguments of 'LocalDate'
    public boolean isShopOpen(LocalDate day)
    {
        // checks at hard set time, where shop is open if not having the day off.
        return isShopOpen(day.atTime(12,0));
    }

    // Accounts for arguments of 'Booking'
    public boolean isShopOpen(Booking booking)
    {
        return isShopOpen(booking.startingTime.toLocalDate());
    }

    // checks if given date is in the weekend
    public boolean isWeekend(LocalDate date)
    {
        return date.getDayOfWeek().getValue() > 5; // checks if weekend.
    }

    // checks if given date coincides with registered vacation.
    public boolean isVacation(LocalDate date)
    {
        if (isWeekend(date)) return true;
        for (LocalDate vDay : vacationDays) // checks if reserved vacation day.
        {
            if (vDay.getDayOfYear() == date.getDayOfYear())
            {
                return true;
            }
        }
        return false;
    }



    // constructor starts with empty lists.
    public BookingList()
    {
        vacationDays = new ArrayList<>();
        vacationDays.add(LocalDate.of(2000,  1,  1));
        vacationDays.add(LocalDate.of(2000,  6,  5));
        vacationDays.add(LocalDate.of(2000, 12, 24));
        vacationDays.add(LocalDate.of(2000, 12, 25));
        vacationDays.add(LocalDate.of(2000, 12, 26));
        vacationDays.add(LocalDate.of(2000, 12, 31));
    }

    // adds booking to list if it does not coincide with other bookings or time off.
    // returns false if booking was NOT added!
    public boolean add(Booking booking)
    {
        // Aborts if not valid timeslot.
        if (!isShopOpen(booking.startingTime)
        ||  LocalDate.now().plusYears(1).isBefore(booking.startingTime.toLocalDate())) // don't allow planning more than a year ahead?
        {
            return false;
        }

        // get bookings occurring on the same date.
        BookingList list = getBookingsFor(booking.startingTime);

        // Check if overlapping existing bookings
        for (Booking b : list)
        {
            if (booking.compareTo(b) == 0)
            {
                return false;
            }
        }

        super.add(booking); // Add to booking-list
        this.sort(null); // Sort list to chronological order.

        return true;
    }

    // add-method that constructs a new Booking automatically from given arguments.
    public boolean add(String name, String phoneNum, String startingTime)
    {
        return this.add(new Booking(name, phoneNum, startingTime));
    }

    // these adders come with being an Arraylist, but are not appropriate BookingList functionality and are therefore overridden.
    public void add(int index, Booking booking){}
    public void addFirst(Booking booking){}
    public void addLast(Booking booking){}

    // removes ALL Bookings occurring on given date.
    public boolean clearDate(LocalDate date)
    {
        return this.removeIf(d -> d.startingTime.toLocalDate().isEqual(date));
    }

    // returns the booking that overlaps given time, or null if none exists.
    public Booking getBooking(LocalDateTime time)
    {
        for (Booking booking : this)
        {
            if (booking.startingTime.isBefore(time) && booking.endTime.isAfter(time))
            {
                return booking;
            }
        }
        return null;
    }

    // Accounts for arguments of (presumably) formatted string.
    public Booking getBooking(String time)
    {
        try
        {
            return getBooking(LocalDateTime.parse(time));
        }
        catch (DateTimeParseException e)
        {
            System.out.println("Fejl i dato-format!");
            return null;
        }
    }

    // returns the next planned booking for a given customer, or null if none exists within a year.
    public Booking getBookingName(String customerName)
    {
        BookingList list = getBookingsFor(LocalDateTime.now(), LocalDateTime.now().plusYears(1));

        for (Booking booking : list)
        {
            if (booking.customerName.equals(customerName))
            {
                return booking;
            }
        }
        return null;
    }

    // returns the next planned booking for a given phone number, or null if none exists within a year.
    public Booking getBookingNumber(String phoneNumber)
    {
        BookingList list = getBookingsFor(LocalDateTime.now(), LocalDateTime.now().plusYears(1));

        for (Booking booking : list)
        {
            if (booking.phoneNumber.equals(phoneNumber))
            {
                return booking;
            }
        }
        return null;
    }

    // Return list of each booking occurring on given date
    public BookingList getBookingsFor(LocalDate date)
    { // note: could be done as getBookingsFor(date, date), though this has fewer checks than that method.
        BookingList list = new BookingList();

        // Checks each booking if on given date and adds those to return value.
        for (Booking booking : this)
        {
            if (booking.startingTime.toLocalDate().isEqual(date))
            {
                list.add(booking);
            }
            else if (list.size() > 0) // note: going through bookings backwards might be faster after having completed several bookings.
            { // OBS: this depends on the archive being sorted, breaks out of loop when past given date to save time.
                break;
            }
        }

        return list;
    }

    // Accounts for arguments of 'LocalDateTime',
    // as it for some reason doesn't extend 'LocalDate'.
    public BookingList getBookingsFor(LocalDateTime date)
    {
        return getBookingsFor(date.toLocalDate());
    }

    // Accounts for arguments of (presumably) formatted string.
    public BookingList getBookingsFor(String date)
    {
        try
        {
            return getBookingsFor(LocalDate.parse(date));
        }
        catch (DateTimeParseException e)
        {
            System.out.println("Fejl i dato-format!");
            return new BookingList();
        }
    }

    // Return list of each booking occurring on & between the given dates.
    public BookingList getBookingsFor(LocalDate startDate, LocalDate endDate)
    {
        BookingList list = new BookingList();

        // expands period by one day each end to include start- and end-date in return value.
        LocalDate start = startDate.minusDays(1);
        LocalDate end   = endDate.plusDays(1);

        // check each booking in archive if they are within the given period and, if so, add them to returned value.
        for (Booking booking : this)
        {
            if (booking.startingTime.toLocalDate().isAfter(start)
                    && booking.startingTime.toLocalDate().isBefore(end))
            {
                list.add(booking);
            }
            else if (list.size() > 0) // note: going through bookings backwards might be faster after having completed several bookings.
            { // OBS: this depends on the archive being sorted, breaks out of loop when past given date to save time.
                break;
            }
        }

        return list;
    }

    // Accounts for arguments of 'LocalDateTime',
    // as it for some reason doesn't extend 'LocalDate'.
    public BookingList getBookingsFor(LocalDateTime startDate, LocalDateTime endDate)
    {
        return getBookingsFor(startDate.toLocalDate(), endDate.toLocalDate());
    }

    public BookingList getFutureBookings()
    {
        return getBookingsFor(LocalDate.now(), LocalDate.now().plusYears(1));
    }

    public BookingList getPastBookings()
    {
        BookingList list = new BookingList();

        // expands period by one day each end to include start- and end-date in return value.
        LocalDate end   = LocalDate.now().plusDays(1);

        // check each booking in archive if they are within the given period and, if so, add them to returned value.
        for (Booking booking : this)
        {
            if (booking.startingTime.toLocalDate().isBefore(end))
            {
                list.add(booking);
            }
            else
            { // OBS: this depends on the archive being sorted, breaks when past date.
                break;
            }
        }

        return list;
    }

    // Checks given day whether there are available timeslots for a new booking,
    // Returns the all available time-spans as startTime/endTime pairs in array, or empty array if none exists for the date.
    public LocalDateTime[] getAvailableTimesFor(LocalDate date) // TODO: take account of different lengths of booking
    {
        // Quick return if date is not a workday.
        if (!isShopOpen(date)) return new LocalDateTime[0];

        ArrayList<LocalDateTime> timeList = new ArrayList<>();
        ArrayList<Booking> bookingList = getBookingsFor(date);
        if (bookingList.isEmpty()) // if there are no bookings, return time-span starting at opening time.
        {
            return new LocalDateTime[]{date.atTime(10, 0), date.atTime(17, 0)};
        }

        LocalDateTime time = date.atTime(10, 0); // start looking for available timeslots from opening time.
        for (Booking booking : bookingList)
        {
            if (time.until(booking.startingTime, ChronoUnit.MINUTES) > 60) // checks if enough time for a booking within time-span.
            {
                timeList.add(time.plusMinutes(0)); // add 0 minutes to get copy of 'time' instead of 'time' itself.
                timeList.add(booking.startingTime.minusMinutes(60)); // subtract 60 minutes to ensure time enough for bookings at end of time-span.
            }
            time = booking.endTime.plusMinutes(1); // iterate time-span check start-point to from end of booking.
        }
        if (time.isBefore(date.atTime(17, 0))) // check if time available after last booking for the date before closing time.
        {
            timeList.add(bookingList.getLast().endTime.plusMinutes(1));
            timeList.add(date.atTime(17, 0));
        }

        // returned array will be empty if fully booked.
        return timeList.toArray(new LocalDateTime[0]);
    }

    // Checks given time whether it is available for a new booking,
    // Returns same time as given, if available, or the soonest available time thereafter.
    // Otherwise, returns null if none exists for the date.
    public LocalDateTime hasTimeAt(LocalDateTime time) // TODO: more explanation comments
    {
        // ensure time is within opening hours.
        if (time.getHour() < 10 || time.getHour() > 17) time = time.toLocalDate().atTime(10, 0);

        // catch times before current time, to ensure time returned is NOT in the past.
        if (time.isBefore(LocalDateTime.now().plusMinutes(15))) time = LocalDateTime.now().plusMinutes(15); // add 30 minutes to not make booking right THIS instant.

        // Quick return if time is not on a work-day.
        if (!isShopOpen(time)) return null;

        LocalDateTime[] timeList = getAvailableTimesFor(time.toLocalDate());

        for (int i = 0; i < timeList.length; i += 2)
        {
            if (time.isBefore(timeList[i+1]) && (time.isAfter(timeList[i]) || time.isEqual(timeList[i])))
            {
                return time;
            }
            else if (time.isBefore(timeList[i]))
            {
                return timeList[i];
            }
        }

        return null;
    }

    // Accounts for arguments of 'LocalDate'.
    public LocalDateTime hasTimeAt(LocalDate date)
    {
        return hasTimeAt(date.atStartOfDay());
    }

    // Accounts for arguments of (presumably) formatted string, with or without time-of-day.
    public LocalDateTime hasTimeAt(String time)
    {
        try
        {
            return hasTimeAt(LocalDateTime.parse(time));
        }
        catch (DateTimeParseException e)
        {
            try
            {
                return hasTimeAt(LocalDate.parse(time));
            }
            catch (DateTimeParseException e2)
            {
                System.out.println("Fejl i dato-format!");
                return null;
            }
        }
    }

    // returns the soonest available time for a booking from after a given time, even if first on a later date.
    public LocalDateTime nextAvailableTimeFrom(LocalDateTime time)
    {
        LocalDateTime soonestTime = hasTimeAt(time); // checks the current day for the soonest available time.

        int addedDays = 0;
        while (soonestTime == null) // checks consecutive days for the soonest available time, so long as none has been found.
        {
            addedDays++;
            soonestTime = hasTimeAt(time.toLocalDate().plusDays(addedDays));
        }

        return soonestTime;
    }

    // Accounts for arguments of 'LocalDate'.
    public LocalDateTime nextAvailableTimeFrom(LocalDate date)
    {
        return nextAvailableTimeFrom(date.atStartOfDay());
    }

    // Accounts for arguments of (presumably) formatted string, with or without time-of-day.
    public LocalDateTime nextAvailableTimeFrom(String time)
    {
        try
        {
            return nextAvailableTimeFrom(LocalDateTime.parse(time));
        }
        catch (DateTimeParseException e)
        {
            try
            {
                return nextAvailableTimeFrom(LocalDate.parse(time));
            }
            catch (DateTimeParseException e2)
            {
                System.out.println("Fejl i dato-format!");
                return null;
            }
        }
    }

    // returns the soonest available time for a booking from REAL-TIME NOW, even if first on a later date.
    public LocalDateTime nextAvailableTime()
    {
        return nextAvailableTimeFrom(LocalDateTime.now());
    }

    // returns formatted schedule for given date.
    public String printDay(LocalDate date) // TODO: beautify
    {
        StringBuilder string = new StringBuilder();

        // Write header for given date as date and name of day.
        string.append("     ");
        string.append(date.format(DateTimeFormatter.ofPattern("dd/MM")));
        string.append(" ");
        string.append(date.getDayOfWeek().name());

        if (!isShopOpen(date)) // if the shop is not open on the date, note whether holiday or weekend.
        {
            string.append("\n\n");
            switch (date.getDayOfWeek())
            {
                case SATURDAY, SUNDAY: string.append("     WEEKEND"); break;
                default: string.append("     HELLIGDAG");
            }

            return string.toString(); // return without looking at potential bookings.
        }

        BookingList list = getBookingsFor(date); // get bookings for given date.
        LocalDateTime start = date.atTime(10, 0); // set up iterator

        // unique print for workdays without any bookings.
        if (list.isEmpty())
        {
            string.append("\n\n");
            string.append("     INGEN AFTALER");

            return string.toString(); // return without looking at potential bookings.
        }

        // determine the longest name of a costumer to apply as width of column.
        int maxLength = 8;
        for (Booking booking : list)
        {
            if (booking.customerName.length() > maxLength) maxLength = booking.customerName.length();
        }

        // adds corresponding names to schedule's columns.
        string.append("\n    TID      KUNDENAVN");
        if (HarrySalon.loggedIn) // only add column name for cost if logged in, as they are not added unless so.
        {
            for (int i = 10; i < maxLength; i++) // add offset of column name, based on 'name'-column width.
            {
                string.append(" ");
            }
            string.append(" PRIS");
        }

        for (Booking booking : list) // add each booking to the schedule.
        {
            // if substantial time free before booking, add the empty timespan to schedule.
            if (start.until(booking.startingTime, ChronoUnit.MINUTES) > 10)
            {
                string.append("\n");
                string.append(start.format(DateTimeFormatter.ofPattern("HH:mm")));
                string.append("-");
                string.append(booking.startingTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            }

            // add timespan for booking to schedule.
            string.append("\n");
            string.append(booking.startingTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            string.append("-");
            string.append(booking.endTime.plusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm")));
            string.append("  ");

            // enforce column width by adding spaces after shorter names.
            StringBuilder normalName = new StringBuilder(booking.customerName);
            while (normalName.length() <= maxLength) normalName.append(" ");

            string.append(normalName); // add costumer name after booking timespan.

            if (HarrySalon.loggedIn) // if logged in, insert price for booking after timespan.
            {
                string.append(booking.receipt.getTotalPrice());
                string.append(",-");
            }

            start = booking.endTime.plusMinutes(1); // iterate start of next timespan to just after currently handled booking.
        }

        // add final timespan to closing hour to schedule.
        string.append("\n");
        string.append(start.format(DateTimeFormatter.ofPattern("HH:mm")));
        string.append("-");
        string.append(date.atTime(18,0).format(DateTimeFormatter.ofPattern("HH:mm")));

        return string.toString();
    }

    // returns formatted schedule for TODAY.
    public String printDay()
    {
        return printDay(LocalDate.now());
    }

    // prints amount of bookkings in list, amount planned and in passed.
    public String toString()
    {
        BookingList list = getFutureBookings();
        StringBuilder string = new StringBuilder();

        string.append(list.size());
        string.append(" fremtidige bookinger. ");
        string.append((size() - list.size()));
        string.append(" tidligere bookinger. ");
        string.append(this.size());
        string.append(" total.");

        return string.toString();
    }

    // stores bookinglist as new text-file in system.
    public void saveTo()
    {
        ArrayList<String> list = new ArrayList<>();

        for (Booking booking : this) // create list of formatted crucial info for each booking.
        {
            list.add(booking.customerName + "\n" + booking.phoneNumber + "\n" + booking.startingTime.format(DateTimeFormatter.ofPattern(Booking.formatterString)));
        }

        Path f = Paths.get("BookingArchive.txt"); // open file for storing data.

        try
        {
            Files.write(f, list); // write/overwrite file in system.
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    // loads bookings to bookinglist from file in system, or leaves it empty if no file found.
    public void loadFrom()
    {
        try // try to open file.
        {
            Scanner scanner = new Scanner(new File("BookingArchive.txt"));
            this.clear(); // ensure this list is empty before loading from file

            while (scanner.hasNextLine()) // file is structured as crucial info for each booking in order.
            {
                this.add(new Booking(scanner.nextLine(), scanner.nextLine(), scanner.nextLine()));
            }
        }
        catch (Exception e) {return;} // let this bookinglist be if no file saved.
    }
}
