import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingList extends ArrayList<Booking>
{
    private ArrayList<LocalDate> vacationDays;

    public BookingList()
    {
        this.vacationDays = new ArrayList<>();
    }

    @Override
    public boolean add(Booking booking)
    {
        ArrayList<Booking> list = getBookingsFor(booking.startingTime);

        for (Booking b : list)
        {
            if (booking.overlaps(b)) // TODO: implement 'Booking's overlap-function
            {
                return false;
            }
        }

        super.add(booking);

        // TODO: Sort list of bookings.
        return true;
    }

    public LocalDateTime nextAvailableTime()
    {
        LocalDateTime next = LocalDateTime.now();

        // TODO: take times for start and end of workday into account.

        for (int i = 0; i < this.size(); i++)
        {
            if (this.get(i).endTime.isAfter(LocalDateTime.now()))
            {
                if (1 > this.get(i+1%this.size()).startingTime.getHour()-this.get(i).endTime.getHour())
                {
                    return this.get(i).endTime;
                }
            }
        }

        return next;
    }

    public boolean hasTimeOn(LocalDateTime date)
    {
        // Accounts for arguments of 'LocalDateTime', as it for some reason doesn't extend 'LocalDateTime'.
        return hasTimeOn(date.toLocalDate());
    }

    public boolean hasTimeOn(LocalDate date)
    {
        for (LocalDate vDay : vacationDays)
        {
            if (date.isEqual(vDay)) return false; // TODO: show error-screen
        }

        ArrayList<Booking> list = getBookingsFor(date);

        if (list.size() < 6 || list.getFirst().startingTime.getHour() > 8) return true;

        for (int i = 0; i < list.size(); i++)
        {
            if (1 > list.get(i+1%list.size()).startingTime.getHour()-list.get(i).endTime.getHour())
            {
                return true;
            }
        }

        // TODO: show error-screen
        return false;
    }

    public BookingList getBookingsFor(LocalDateTime date)
    {
        // Accounts for arguments of 'LocalDateTime', as it for some reason doesn't extend 'LocalDateTime'.
        return getBookingsFor(date.toLocalDate());
    }

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

    public BookingList getBookingsFor(LocalDateTime startDate, LocalDateTime endDate)
    {
        // Accounts for arguments of 'LocalDateTime', as it for some reason doesn't extend 'LocalDateTime'.
        return getBookingsFor(startDate.toLocalDate(), endDate.toLocalDate());
    }

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

    public String toString()
    {
        StringBuilder string = new StringBuilder();

        // TODO: all of this.

        return string.toString();
    }
}
