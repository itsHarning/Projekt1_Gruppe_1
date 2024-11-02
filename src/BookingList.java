
import java.io.File;
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

// BookingList class serves as a semi-self-managing list of bookings
// being able to account for bookings that would overlap each other,
// which times are available for new bookings,
// to deliver registered bookings from arguments of identifying information rather than index.
// and, to a degree, formatting a schedule for a given day as a string.
public class BookingList extends ArrayList<Booking>
{
    private static final ArrayList<LocalDate> vacationDays = new ArrayList<>(); // repurposed as Holidays.

    // constructor sets up vacation days.
    public BookingList()
    {
        // add holidays with consistent dates each year.
        // NOTE: though the dates must be registered with a year,
        // our comparisons to the list only checks date-of-year,
        // and thus the dates still apply every year.
        if (vacationDays.isEmpty())
        {
            vacationDays.add(LocalDate.of(2000, 1, 1)); // new years day
            vacationDays.add(LocalDate.of(2000, 6, 5)); // 5th of march
            vacationDays.add(LocalDate.of(2000, 12, 24)); // christmas eve
            vacationDays.add(LocalDate.of(2000, 12, 25)); // christmas day
            vacationDays.add(LocalDate.of(2000, 12, 26)); // 2nd christmas day
            vacationDays.add(LocalDate.of(2000, 12, 31)); // new years eve
        }
    }

// adders

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

// removers

    // removes ALL Bookings occurring on given date.
    public boolean clearDate(LocalDate date)
    {
        return this.removeIf(d -> d.startingTime.toLocalDate().isEqual(date));
        // uses a Predicate, was briefly explained and agreed to keep.
    }


// getters

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
        try // tries to parse given string as LocalDateTime.
        {
            return getBooking(LocalDateTime.parse(time, Booking.formatter));
        }
        catch (DateTimeParseException e)
        {
            // returns null and prints error message, if date not formatted correctly.
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
    { // note: could be done as 'getBookingsFor(date, date)', though this has fewer checks than that method.
        BookingList list = new BookingList();

        // Checks each booking if on given date and adds those to return value.
        for (Booking booking : this)
        {
            if (booking.startingTime.toLocalDate().isEqual(date))
            {
                list.add(booking);
            }
            else if (list.size() > 0)
            { // breaks out of loop when past given date to save time. OBS: this depends on the archive being sorted.
                break;
            }
        }

        return list;
    }

    // Accounts for arguments of 'LocalDateTime',
    public BookingList getBookingsFor(LocalDateTime date)
    {
        return getBookingsFor(date.toLocalDate());
    }

    // Accounts for arguments of (presumably) formatted string.
    public BookingList getBookingsFor(String date)
    {
        try // tries to parse the given string as a date.
        {
            return getBookingsFor(LocalDate.parse(date, Booking.formatter));
        }
        catch (DateTimeParseException e)
        {
            // returns empty BookingList and prints error message, if date not formatted correctly.
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
    public BookingList getBookingsFor(LocalDateTime startDate, LocalDateTime endDate)
    {
        return getBookingsFor(startDate.toLocalDate(), endDate.toLocalDate());
    }

    // Returns a new BookingList containing ALL bookings from this list, that occurs after 'now'
    public BookingList getFutureBookings()
    {
        return getBookingsFor(LocalDate.now(), LocalDate.now().plusYears(1));
    }

    // Returns a new BookingList containing ALL bookings from this list, that occurred before 'now'
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


// methods for checking times available.

    // Checks given day whether there are available timeslots for a new booking,
    // Returns the all available time-spans as startTime/endTime pairs in array, or empty array if none exists for the date.
    // NOTE: method assumes that any booking, that is sought a time for, has a 1-hour duration.
    public LocalDateTime[] getAvailableTimesFor(LocalDate date)
    {
        // Quick return if the date is not a workday.
        if (!isShopOpen(date)) return new LocalDateTime[0];

        ArrayList<LocalDateTime> timeList = new ArrayList<>();
        ArrayList<Booking> bookingList = getBookingsFor(date);

        // if there are no bookings, return full time-span from opening to closing time.
        if (bookingList.isEmpty())
        {
            return new LocalDateTime[]{date.atTime(10, 0), date.atTime(17, 0)};
        }

        // start looking for available timeslots from opening time.
        LocalDateTime potentialStartTime = date.atTime(10, 0);
        for (Booking booking : bookingList)
        {
            // checks if enough time for a booking within time-span.
            if (potentialStartTime.until(booking.startingTime, ChronoUnit.MINUTES) > 60)
            {
                timeList.add(potentialStartTime.plusMinutes(0)); // add 0 minutes to get a COPY of 'time' instead of 'time' itself.
                timeList.add(booking.startingTime.minusMinutes(60)); // subtract 60 minutes from the start of the existing booking to ensure time enough for potential new booking starting at end of the time-span.
            }
            // iterate start-point to check from end of 'booking',
            // adding one minute since two bookings ending/starting at the same moment would flag as overlapping.
            potentialStartTime = booking.endTime.plusMinutes(1);
        }

        // final check, if the time available after the last booking for the day
        // is enough for another booking before closing time.
        if (potentialStartTime.isBefore(date.atTime(17, 0)))
        {
            timeList.add(bookingList.getLast().endTime.plusMinutes(1));
            timeList.add(date.atTime(17, 0));
        }

        // NOTE: returned array will be automatically empty if fully booked.
        return timeList.toArray(new LocalDateTime[0]);
    }

    // Checks given time whether it is available for a new booking,
    // Returns same time as given, if available, or the soonest available time thereafter on the same date.
    // Otherwise, returns null if none exists for the date.
    public LocalDateTime nextTimeOnDayFrom(LocalDateTime time)
    {
        // catch times before 'now', to ensure time returned is NOT in the past.
        if (time.isBefore(LocalDateTime.now().plusMinutes(15))) time = LocalDateTime.now().plusMinutes(15); // add 30 minutes to not make booking right THIS instant.

        // ensure given time is within opening hours.
        if (time.getHour() < 10 || time.getHour() > 17) time = time.toLocalDate().atTime(10, 0);

        // Quick return if time is not on a work-day.
        if (!isShopOpen(time)) return null;

        // get available times for the date
        LocalDateTime[] timeList = getAvailableTimesFor(time.toLocalDate());


        for (int i = 0; i < timeList.length; i += 2)
        {
            // check 'time' against start and end of each available time-span.
            if (time.isBefore(timeList[i+1]) && (time.isAfter(timeList[i]) || time.isEqual(timeList[i])))
            {
                return time; // return 'time' if within available time-span.
            }
            else if (time.isBefore(timeList[i]))
            {
                return timeList[i]; // return start of earliest available time-span, if it is after 'time', as logically 'time' would then not be available.
            }
        }

        return null; // return null if there are no available times at or after 'time' on the same day.
    }

    // Accounts for arguments of 'LocalDate'.
    public LocalDateTime nextTimeOnDayFrom(LocalDate date)
    {
        return nextTimeOnDayFrom(date.atStartOfDay());
    }

    // Accounts for arguments of (presumably) formatted string, with or without time-of-day.
    public LocalDateTime nextTimeOnDayFrom(String time)
    {
        try // tries to parse given string as LocalDateTime.
        {
            return nextTimeOnDayFrom(LocalDateTime.parse(time, Booking.formatter));
        }
        catch (DateTimeParseException e)
        {
            try // tries to parse given string as LocalDate.
            {
                return nextTimeOnDayFrom(LocalDate.parse(time, Booking.formatter));
            }
            catch (DateTimeParseException e2)
            {
                // returns null and prints error message, if date not formatted correctly.
                System.out.println("Fejl i dato-format!");
                return null;
            }
        }
    }

    // returns the soonest available time for a booking from after a given time, even if first on a later date.
    public LocalDateTime nextAvailableTimeFrom(LocalDateTime time)
    {
        LocalDateTime soonestTime = nextTimeOnDayFrom(time); // checks the current day for the soonest available time.

        int addedDays = 0;
        while (soonestTime == null) // checks consecutive days for the soonest available time, so long as none has been found.
        {
            addedDays++;
            soonestTime = nextTimeOnDayFrom(time.toLocalDate().plusDays(addedDays));
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
        try // tries parsing given string as LocalDateTime.
        {
            return nextAvailableTimeFrom(LocalDateTime.parse(time, Booking.formatter));
        }
        catch (DateTimeParseException e)
        {
            try // tries parsing given string as LocalDate.
            {
                return nextAvailableTimeFrom(LocalDate.parse(time, Booking.formatter));
            }
            catch (DateTimeParseException e2)
            {
                // returns null and prints error message, if date not formatted correctly.
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


// methods for checks regarding opening hours/days

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


// methods for formatting strings

    // returns formatted schedule for TODAY as string.
    public String formatDay()
    {
        return formatDay(LocalDate.now());
    }

    // returns formatted schedule for given date as string.
    public String formatDay(LocalDate date)
    {
        StringBuilder string = new StringBuilder();

        string.append("     "); // tabulate
        string.append(formatHeader(date)); // add date header

        if (!isShopOpen(date)) // if the shop is not open on the date, note whether holiday or weekend.
        {
            string.append("\n\n     "); // tabulate
            string.append(getClosedLabel(date));

            return string.toString(); // return without looking at potential bookings.
        }

        BookingList list = getBookingsFor(date); // get bookings for given date.

        // unique print for workdays without any bookings.
        if (list.isEmpty())
        {
            string.append("\n\n     ");
            string.append("INGEN AFTALER");

            return string.toString(); // return without looking at potential bookings.
        }

        // determine the longest name of a costumer to apply as width of column.
        int columnWidth = getLongestNameLength(list);

        // adds corresponding titles to schedule's columns.
        string.append(formatColumnTitles(columnWidth));

        // add scheduled times with potential corresponding costumer names.
        string.append(formatBookings(list, columnWidth));

        return string.toString();
    }

    // formats day header as dd/MM name-of-day.
    private String formatHeader(LocalDate date)
    {
        String string = date.format(DateTimeFormatter.ofPattern("dd/MM"));
        string += " ";

        // add danish name of day.
        string += switch (date.getDayOfWeek())
        {
            case MONDAY    -> "MANDAG";
            case TUESDAY   -> "TIRSDAG";
            case WEDNESDAY -> "ONSDAG";
            case THURSDAY  -> "TORSDAG";
            case FRIDAY    -> "FREDAG";
            case SATURDAY  -> "LØRDAG";
            case SUNDAY    -> "SØNDAG";
        };

        return string;
    }

    // formats column titles to match with column width.
    private String formatColumnTitles(int columnWidth)
    {
        StringBuilder string = new StringBuilder();

        // adds corresponding names to schedule's columns.
        string.append("\n    TID      KUNDENAVN");
        if (HarrySalon.loggedIn) // only add column name for cost if logged in, as they are not added unless so.
        {
            // add offset of column name, based on 'name'-column width.
            for (int i = 10; i <= columnWidth; i++)
            {
                string.append(" ");
            }
            string.append("PRIS");
        }

        return string.toString();
    }

    // formats list of bookings, enforcing column width.
    private String formatBookings(BookingList list, int columnWidth)
    {
        StringBuilder string = new StringBuilder();
        LocalDateTime start = list.getFirst().startingTime.toLocalDate().atTime(10, 0); // set up iterator

        for (Booking booking : list) // add each booking to the schedule.
        {
            // if substantial time free before booking, add the empty time-span to schedule.
            if (start.until(booking.startingTime, ChronoUnit.MINUTES) > 10)
            {
                string.append("\n");
                string.append(formatTimeSpan(start, booking.startingTime));
            }

            // add booking formatted as single line.
            string.append(formatBooking(booking, columnWidth));

            start = booking.endTime.plusMinutes(1); // iterate start of next time-span to just after currently handled booking.
        }

        // add final time-span to closing hour to schedule.
        string.append("\n");
        string.append(formatTimeSpan(start, start.toLocalDate().atTime(18,0)));

        return string.toString();
    }

    // formats single booking to single line for use in schedule.
    private String formatBooking(Booking booking, int columnWidth)
    {
        StringBuilder string = new StringBuilder();

        // add timespan for booking to schedule.
        string.append("\n");
        string.append(formatTimeSpan(booking.startingTime, booking.endTime));
        string.append("  ");

        // enforce column width by adding spaces after shorter names.
        StringBuilder normalName = new StringBuilder(booking.customerName);
        while (normalName.length() <= columnWidth) normalName.append(" ");

        string.append(normalName); // add costumer name after booking timespan.

        if (HarrySalon.loggedIn) // if logged in, insert price for booking after timespan.
        {
            string.append(booking.receipt.getTotalPrice());
            string.append(",-");
        }

        return string.toString();
    }

    // formats string from two LocalDateTime as HH:mm-HH:mm.
    private String formatTimeSpan(LocalDateTime startTime, LocalDateTime endTime)
    {
        StringBuilder string = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        string.append(startTime.format(formatter));
        string.append("-");
        string.append(endTime.plusMinutes(1).format(formatter));

        return string.toString();
    }

    // returns string depending on whether date i s weekend or holiday.
    private String getClosedLabel(LocalDate date)
    {
        return switch (date.getDayOfWeek())
        {
            case SATURDAY, SUNDAY -> "WEEKEND";
            default -> "HELLIGDAG";
        };
    }

    // returns length of longest costumer name from given bookings, or 8 as minimum.
    private int getLongestNameLength(BookingList list)
    {
        // enforce least-length of 8.
        int longestLength = 8;
        for (Booking booking : list)
        {
            if (booking.customerName.length() > longestLength) longestLength = booking.customerName.length();
        }
        return longestLength;
    }

    // returns string explaining amount of bookings in list, amount planned and done.
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


// methods for storing/loading data

    // stores this booking list as new text-file in system.
    public boolean saveTo(String fileName)
    {
        ArrayList<String> list = new ArrayList<>();

        for (Booking booking : this) // create list of formatted info for each booking.
        {
            list.add(booking.customerName + "\n" + booking.phoneNumber + "\n" + booking.startingTime.format(DateTimeFormatter.ofPattern(Booking.formatterString)));
        }

        try
        {
            Path f = Paths.get(fileName); // open file for storing data.
            Files.write(f, list); // write/overwrite file in system.
        }
        catch (Exception e)
        {
            return false; // return false if save was unsuccessful.
        }
        return true;
    }

    // loads bookings to this booking list from file in system, or leaves it empty if no file found.
    // method is NOT called in constructor, as empty BookingLists are also utilized.
    public boolean loadFrom(String fileName)
    {
        Scanner scanner = null; // declare scanner outside 'try'-scope to let 'finally' see and close it.
        try // try to open file.
        {
            scanner = new Scanner(new File(fileName));
            this.clear(); // ensure this list is empty before loading from file

            while (scanner.hasNextLine()) // file is structured as crucial info for each booking in order.
            {
                // uses 'super.add' to surpass time-conflict-checks when loading from file.
                super.add(new Booking(scanner.nextLine(), scanner.nextLine(), scanner.nextLine()));
            }
            // this.sort(null); // sort list chronologically (it should be already but testing may mess with the order)
        }
        catch (Exception e) {return false;} // let this booking list be as it where, if no file saved.
        finally // ensure scanner is closed, even if error is thrown by something else.
        {
            if (scanner != null) scanner.close();
        }
        return true;
    }


// NOTE: the following methods are redundant, as there was no time
// to implement a user-interface for managing vacations.

    // adds given date to vacation days, ensuring not to add duplicate dates or overlap Bookings.
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

}
