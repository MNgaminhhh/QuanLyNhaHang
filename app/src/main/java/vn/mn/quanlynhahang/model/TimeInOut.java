package vn.mn.quanlynhahang.model;

import java.util.Date;
import java.util.List;

public class TimeInOut {
    Date timeIn;
    Date timeOut;

    public TimeInOut() {
    }

    public TimeInOut(Date timeIn, Date timeOut) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public Date getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Date timeIn) {
        this.timeIn = timeIn;
    }

    public Date getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Date timeOut) {
        this.timeOut = timeOut;
    }
}
