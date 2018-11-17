package musicline.cmov.org.feup.musicline.utils;

import java.io.Serializable;

public class Show implements Serializable{

    String id;
    String name;
    String date;
    Number ticketPrice;

    public Show(String id, String name, String date, Number ticketPrice) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public Number getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Number ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
