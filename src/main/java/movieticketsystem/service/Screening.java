package movieticketsystem.service;

public class Screening {

    String date,movieName;
    int mid, rnumber;

    public Screening(String movieName,String date, int mid, int rnumber) {
        this.movieName = movieName;
        this.date = date;
        this.mid = mid;
        this.rnumber = rnumber;
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

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getRnumber() {
        return rnumber;
    }

    public void setRnumber(int rnumber) {
        this.rnumber = rnumber;
    }


}
