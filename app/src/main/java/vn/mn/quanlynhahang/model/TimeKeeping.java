package vn.mn.quanlynhahang.model;

import java.util.Date;
import java.util.List;

public class TimeKeeping {
    User user;
    List<TimeInOut> timeList;

    public TimeKeeping() {
    }

    public TimeKeeping(User user, List<TimeInOut> timeList) {
        this.user = user;
        this.timeList = timeList;
    }
    public int getIndexForTimeInOutForToday() {
        Date today = new Date();
        for (int i = 0; i < timeList.size(); i++) {
            Date timeIn = timeList.get(i).getTimeIn();
            if (timeIn.getYear() == today.getYear() &&
                    timeIn.getMonth() == today.getMonth() &&
                    timeIn.getDate() == today.getDate()) {
                return i;
            }
        }
        return -1;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TimeInOut> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeInOut> timeList) {
        this.timeList = timeList;
    }
}
