package vn.mn.quanlynhahang.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Schedule {
    User user;
    List<Date> scheduleDay;

    public Schedule() {
    }

    public Schedule(User user, List<Date> scheduleDay) {
        this.user = user;
        this.scheduleDay = scheduleDay;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Date> getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(List<Date> scheduleDay) {
        this.scheduleDay = scheduleDay;
    }
}
