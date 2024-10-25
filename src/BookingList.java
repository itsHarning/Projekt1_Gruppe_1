import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingList extends ArrayList<Booking>
{
    private final ArrayList<LocalDate> vacationDays;

    // Constructor starts empty
    public BookingList()
    {
        this.vacationDays = new ArrayList<>();
    }

    // Adds booking to list if it does not coincide with other bookings or time off.
    @Override // returns false if booking was NOT added!
    public boolean add(Booking booking)
    {
        // Aborts if not valid timeslot.
        if (!isShopOpen(booking.bookedDate) || LocalDate.now().plusYears(1).isBefore(booking.bookedDate))
        {
            return false;
        }

        // fetch bookings occurring on the same date.
        ArrayList<Booking> list = getBookingsFor(booking.bookedDate);

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

    // returns the next booking for a given customer, or null if none exists within a year.
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

    // returns the next booking for a given phone number, or null if none exists within a year.
    public Booking getBookingTlf(String phoneNumber)
    {
        BookingList list = getBookingsFor(LocalDateTime.now(), LocalDateTime.now().plusYears(1));

        for (Booking booking : list) // TODO: Add PhoneNumber var in Booking.
        {
            if (booking.phoneNumber.equals(phoneNumber))
            {
                return booking;
            }
        }
        return null;
    }

    // Checks if time coincides with weekend, vacation or closing hours.
    public boolean isShopOpen(LocalDateTime time)
    {
        if (time.getDayOfWeek().getValue() > 5) return false;
        if (time.getHour() < 8 || time.getHour() > 16) return false;
        for (LocalDate vDay : vacationDays)
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
    public LocalDateTime[] timesAt(LocalDate date)
    {
        // Quick return if date is not a workday.
        if (!isShopOpen(date)) return null; // TODO: show error-screen

        ArrayList<Booking> list = getBookingsFor(date);

        // TODO: All of this

        return null;
    }

    // Checks given time whether it is available for a new booking,
    // Returns same time if available.
    // Otherwise, returns the earliest available timeslot or null if none exists for the date.
    public LocalDateTime hasTimeAt(LocalDateTime time)
    {
        // Quick return if date is not a workday.
        if (!isShopOpen(time)) return null; // TODO: show error-screen

        LocalDateTime[] earliest = timesAt(time.toLocalDate());

        return time;
    }

    // returns the next available timeslot for a booking from given time.
    public LocalDateTime nextAvailableTime(LocalDateTime startTime)
    {
        // catch times before current time, to ensure time returned is NOT in the past.
        if (startTime.isBefore(LocalDateTime.now())) startTime = LocalDateTime.now();

        // TODO: take account of workday scope and vacations.

        for (int i = 0; i < this.size(); i++)
        {
            if (this.get(i).endTime.isAfter(startTime))
            {
                if (1 > this.get(i+1%this.size()).startingTime.getHour()-this.get(i).endTime.getHour())
                {
                    return this.get(i).endTime;
                }
            }
        }

        return startTime;
    }

    // returns the next available timeslot for a booking from NOW.
    public LocalDateTime nextAvailableTime()
    {
        return nextAvailableTime(LocalDateTime.now());
    }

    // Return list of each booking occurring on given date
    public BookingList getBookingsFor(LocalDate date)
    {
        BookingList list = new BookingList();

        // Checks each booking if on given date and adds those to return value.
        for (Booking booking : this)
        {
            if (booking.bookedDate.isEqual(date))
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
            if (booking.bookedDate.isAfter(start)
                    && booking.bookedDate.isBefore(end))
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

    public String printDay(LocalDate date)
    {
        StringBuilder string = new StringBuilder();

        string.append(date.format(DateTimeFormatter.ofPattern("dd/MM")));
        string.append("\n");

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
            time = 8 + i/2;
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
        StringBuilder string = new StringBuilder();

        // TODO: all of this.

        return string.toString();
    }
}
