package movieticketsystem.service;

import java.sql.Date;

public class Booking {

    private String movieName, date;
    private int roomNumber;

    public Booking(String movieName, String date, int roomNumber) {
        this.movieName = movieName;
        this.date = date;
        this.roomNumber = roomNumber;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }


}

