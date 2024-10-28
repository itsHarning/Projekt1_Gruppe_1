import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingList extends ArrayList<Booking> implements Serializable // serializable to save to file.
{
    private final ArrayList<LocalDate> vacationDays;

    // adds given date to vacationDays, ensuring not to add duplicate dates or overlap Bookings.
    // returns false if attempted to add date with registered bookings.
    public boolean addVacation(LocalDate date)
    {
        for (Booking booking : this) // checks if there exists bookings on given date.
        {
            if (booking.startingTime.toLocalDate().isEqual(date)) return false;
        }
        vacationDays.removeIf(d -> d.isEqual(date)); // this is the simplest way to avoid duplicate dates.
        vacationDays.add(date);
        return true;
    }

    // removes the given date from vacationDays.
    public boolean removeVacation(LocalDate date)
    {
        return vacationDays.removeIf(d -> d.isEqual(date));
    }

    // Constructor starts with empty lists.
    public BookingList(){vacationDays = new ArrayList<>();}

    // Adds booking to list if it does not coincide with other bookings or time off.
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
        ArrayList<Booking> list = getBookingsFor(booking.startingTime);

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

    // These adders come with being an Arraylist, but are not appropriate BookingList functionality and are therefore overridden.
    public void add(int index, Booking booking){}
    public void addFirst(Booking booking){}
    public void addLast(Booking booking){}


    // removes ALL Bookings occurring on given date.
    public boolean clearDate(LocalDate date)
    {
        return this.removeIf(d -> d.startingTime.toLocalDate().isEqual(date));
    }

    // These getters come with being an Arraylist, but are not appropriate BookingList functionality and are therefore overridden.
    // Returns dummy Booking.
    public Booking get(int index) {return this.getFirst();}
    public Booking getFirst() {return this.getLast();}
    public Booking getLast()
    {
        return new Booking("John Doe", "01010101", "2001 01 01 01:01");
    }

    // returns the booking that overlaps given time, or null if none exists.
    public Booking getBooking(LocalDateTime dateTime)
    {
        for (Booking booking : this)
        {
            if (booking.startingTime.isBefore(dateTime) && booking.endTime.isAfter(dateTime))
            {
                return booking;
            }
        }
        return null;
    }

    // returns the next planned booking for a given customer, or null if none exists within a year.
    public Booking getBooking(String customerName)
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

    // Checks if time coincides with weekend, vacation or closing hours.
    // TODO: implement 'global' var for opening hours.
    public boolean isShopOpen(LocalDateTime time)
    {
        if (time.getDayOfWeek().getValue() > 5) return false; // checks if weekend.
        if (time.getHour() < 10 || time.getHour() > 17) return false; // checks opening hours.
        for (LocalDate vDay : vacationDays) // checks if reserved vacation day.
        {
            if (vDay.isEqual(time.toLocalDate()))
            {
                return false;
            }
        }

        return true;
    }

    // Accounts for arguments of 'LocalDateTime'
    public boolean isShopOpen(LocalDate day)
    {
        // checks at hard set time, where shop is open if not having the day off.
        return isShopOpen(day.atTime(12,0));
    }

    // Checks given day whether there is an available timeslot for a new booking,
    // Returns the all available times in hourly increments or null if none exists for the date.
    // TODO: implement 'global' var for opening hours.
    // TODO: take account of different lengths of booking
    public LocalDateTime[] timesAt(LocalDate date)
    {
        // Quick return if date is not a workday.
        if (!isShopOpen(date)) return null;

        ArrayList<Booking> list = getBookingsFor(date);
        ArrayList<LocalDateTime> times = new ArrayList<>();

        LocalDateTime time = date.atTime(10, 0);
        for (Booking booking : list)
        {
            if (time.until(booking.startingTime, ChronoUnit.MINUTES) >= 60)
            {
                times.add(time.plusMinutes(0));
                times.add(booking.startingTime.minusMinutes(60));
                time = booking.endTime.plusMinutes(1);
            }
        }
        if (list.isEmpty())
        {
            return new LocalDateTime[]{time, date.atTime(17, 0)};
        }
        if (list.getLast().endTime.isBefore(date.atTime(17, 0)))
        {
            times.add(list.getLast().endTime.plusMinutes(1));
            times.add(date.atTime(17, 0));
        }

        return times.toArray(new LocalDateTime[0]);
    }

    // Checks given time whether it is available for a new booking,
    // Returns same time if available.
    // Otherwise, returns the earliest available timeslot or null if none exists for the date.
    // TODO: implement 'global' var for opening hours.
    public LocalDateTime hasTimeAt(LocalDateTime time)
    {
        // ensure time is within opening hours.
        if (time.getHour() < 10 || time.getHour() > 17) time = time.toLocalDate().atTime(10, 0);

        // catch times before current time, to ensure time returned is NOT in the past.
        if (!time.isAfter(LocalDateTime.now())) time = LocalDateTime.now().plusMinutes(30); // add 30 minutes to not make booking right THIS instant.

        // Quick return if time is not on a work-day.
        if (!isShopOpen(time)) return null; // TODO: show error-screen?

        LocalDateTime[] list = timesAt(time.toLocalDate());

        if (list.length == 0) return time; // Quick return if there are no bookings on given date.
        for (int i = 0; i < list.length; i += 2) // TODO: more explanation comments
        {
            if (time.isAfter(list[i]) && time.isBefore(list[i+1]))
            {
                return time;
            }
            else if (time.isBefore(list[i]))
            {
                return list[i];
            }
        }

        return null;
    }

    public LocalDateTime hasTimeAt(LocalDate date)
    {
        return hasTimeAt(date.atStartOfDay());
    }

    // returns the next available timeslot for a booking from NOW.
    public LocalDateTime nextAvailableTime()
    {
        LocalDateTime time = hasTimeAt(LocalDateTime.now());
        int addedDays = 0;

        while (time == null) // TODO: comment
        {
            addedDays++;
            time = hasTimeAt(LocalDate.now().plusDays(addedDays));
        }

        return time;
    }

    // Return list of each booking occurring on given date
    public BookingList getBookingsFor(LocalDate date)
    {
        BookingList list = new BookingList();

        // Checks each booking if on given date and adds those to return value.
        for (Booking booking : this)
        {
            if (booking.startingTime.toLocalDate().isEqual(date))
            {
                list.add(booking);
            }
            else if (list.size() > 0)
            { // OBS: this depends on the archive being sorted, breaks out of loop when past given date.
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
            else if (list.size() > 0)
            { // OBS: this depends on the archive being sorted, breaks when past date.
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

    // TODO: implement 'global' var for opening hours.
    // TODO: make display accurate times for bookings.
    public String printDay(LocalDate date) // todo WiP
    {
        StringBuilder string = new StringBuilder("\t");

        string.append(date.format(DateTimeFormatter.ofPattern("dd/MM")));

        if (!isShopOpen(date))
        {
            string.append("\n");
            switch (date.getDayOfWeek())
            {
                case SATURDAY, SUNDAY: string.append("WEEKEND"); break;
                default: string.append("VACATION");
            }

            return string.toString();
        }

        BookingList list = getBookingsFor(date);
        String[] timeSlots = new String[16];

        int time;
        for (int i = 0; i < 16; i++)
        {
            time = 10 + i/2;
            string.append("\n" + time + ":");
            if (i%2 == 0)
            {
                string.append("00");
            }
            else
            {
                string.append("30");
            }
            string.append("\t");

            for (Booking booking : list)
            {
                if (booking.startingTime.getHour() == time) // TODO: make account for minutes.
                {
                    string.append(booking.customerName);
                    break;
                }
            }

            timeSlots[i] = string.toString();
            // TODO: clear StringBuilder.
        }

        return string.toString();
    }

    public String toString()
    {
        BookingList list = getFutureBookings();
        StringBuilder string = new StringBuilder();

        string.append(list.size());
        string.append(" future bookings. ");
        string.append((size() - list.size()));
        string.append(" completed.");
        string.append(this.size());
        string.append(" total.");

        return string.toString();
    }
}
