package movieticketsystem.service;

public class Screening {

    String date,movieName;
    int movieId;
    int roomNumber;
    int attendance;

    public Screening(String movieName,String date,  int roomNumber, int attendance) {
        this.movieName = movieName;
        this.date = date;
        this.roomNumber = roomNumber;
        this.attendance = attendance;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }
}