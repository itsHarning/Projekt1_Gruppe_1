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
        if (!isShopOpen(booking.startingTime)) return false;

        ArrayList<Booking> list = getBookingsFor(booking.startingTime);

        for (Booking b : list)
        {
            // TODO: implement proper 'Booking's overlap-function
            if (booking.startingTime.getHour() == b.startingTime.getHour())
            {
                return false;
            }
        }

        super.add(booking);
        // TODO: Sort list of bookings.

        return true;
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
        if (!isShopOpen(date)) return null; // TODO: show error-screen

        ArrayList<Booking> list = getBookingsFor(date);

        if (list.size() < 6 || list.getFirst().startingTime.getHour() > 8) return null;

        for (int time = 8; time < 16; time++)
        {
            for (Booking booking : list)
            {
                // TODO: implement proper 'Booking's overlap-function
                if (hasTimeAt(date.atTime(time,0)))
                {
                    continue;
                }
            }
        }

        // TODO: show error-screen
        return null;
    }

    // Checks given time whether it is available for a new booking,
    // Returns same time if available.
    // Otherwise, returns the earliest available timeslot or null if none exists for the date.
    public boolean hasTimeAt(LocalDateTime time)
    {
        LocalDateTime earliest = hasTimeAt(time.toLocalDate());
        if (earliest == null || time.isBefore(earliest)) return earliest;

        ArrayList<Booking> list = getBookingsFor(time);

        for (Booking booking : list)
        {
            // TODO: implement proper 'Booking's overlap-function
            if (Math.abs(time.getHour()-booking.startingTime.getHour()) < 1)
            {
                return null;
            }
        }

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
            if (booking.startingTime.toLocalDate().isEqual(date))
            {
                list.add(booking);
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
        }

        return list;
    }

    // Accounts for arguments of 'LocalDateTime',
    // as it for some reason doesn't extend 'LocalDate'.
    public BookingList getBookingsFor(LocalDateTime startDate, LocalDateTime endDate)
    {
        return getBookingsFor(startDate.toLocalDate(), endDate.toLocalDate());
    }

    public String toString()
    {
        StringBuilder string = new StringBuilder();

        // TODO: all of this.

        return string.toString();
    }
}
