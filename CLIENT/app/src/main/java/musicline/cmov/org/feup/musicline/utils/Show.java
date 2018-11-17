package musicline.cmov.org.feup.musicline.utils;

import java.io.Serializable;

public class Show implements Serializable{

    String id, name, description, place, date;
    Number ticketPrice;

    public Show(String id, String name, String description, String place, String date, Number ticketPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.place = place;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
