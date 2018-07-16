package autotest.entity;

import java.util.ArrayList;
import java.util.List;

public class BookedTickets {

    public static List<TicketData> getTicketsList() {
        return ticketsList;
    }

    private static List<TicketData> ticketsList = new ArrayList<>();

}
