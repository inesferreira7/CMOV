package musicline.cmov.org.feup.musicline.utils;

import java.util.Date;

public class Show {

    String name;
    Date date;
    Number ticketPrice;

    public Show(String name, Date date, Number ticketPrice) {
        this.name = name;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Number getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Number ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
