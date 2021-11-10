package common.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateTime implements Serializable {

    private String time;
    private int year;
    private int month;
    private int day;

    public DateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.time = formatter.format(date);
        String help = this.time.substring(0,10);
        String[] helps = help.split("/");
        this.day = Integer.parseInt(helps[0]);
        this.month = Integer.parseInt(helps[1]);
        this.year = Integer.parseInt(helps[2]);
    }

    public DateTime(int day, int month, int year){
        if(check(day,month,year)){
            this.day = day;
            this.month = month;
            this.year = year;
        } else throw new IllegalStateException("Illegal date argument");
    }

    public DateTime(LocalDate date){
        Date date1 = java.sql.Date.valueOf(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.time = formatter.format(date1);
        String help = this.time.substring(0,10);
        String[] helps = help.split("/");
        this.day = Integer.parseInt(helps[0]);
        this.month = Integer.parseInt(helps[1]);
        this.year = Integer.parseInt(helps[2]);
    }

    public String getTimestamp() {
        return time;
    }

    public String getSortableDate() {
        return String.format("%d-%d-%d", year,month,day);
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%d", day,month,year);
    }

    public static String getToday(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return formatter.format(date);
    }

    public static int yearsBetween(DateTime date)
    {
        DateTime dateTime = new DateTime();

        int yearsDifference = dateTime.year - date.year;
        if (dateTime.month > date.month)
            {
                return yearsDifference;
            }
        else
            {
                if (dateTime.day > date.day)
                {
                    return yearsDifference;
                }
                return (yearsDifference - 1);
            }
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    private boolean check(int day, int month, int year){
        if(day<0 || day> 31 || month<1 || month>12 || year<1900) return false;
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11: if(day >30) return false; break;
            case 2:
                if(((year % 4 == 0) && ((year % 100 != 0) || (year % 40  == 0)))){
                    if(day<=29)
                        return true;
                }
                else if (day > 28) return false;

        }
        return true;
    }
}
