package vn.mn.quanlynhahang.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Schedule;
import vn.mn.quanlynhahang.model.ScheduleDB;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.User;

public class ScheduleFragment extends Fragment {
    com.applandeo.materialcalendarview.CalendarView calendarView;
    TextView txtMonthYear, txtDate, txtDayOfWeek;
    Button btnSign;
    Calendar selectedDay;
    public static User user;
    MutableLiveData<Schedule> schedule = new MutableLiveData<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        txtMonthYear =  view.findViewById(R.id.txtMonthYear);
        txtDate = view.findViewById(R.id.txtDate);
        txtDayOfWeek = view.findViewById(R.id.txtDayOfWeek);
        btnSign = view.findViewById(R.id.btnSign);

        Date currentDay = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDay);
        txtMonthYear.setText("Tháng "+(calendar.get(Calendar.MONTH) + 1)+", "+calendar.get(Calendar.YEAR));
        txtDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"");
        txtDayOfWeek.setText(convertToDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        btnSign.setVisibility(View.INVISIBLE);
        selectedDay = calendar;
        currentDay.setDate(0);
        calendar.setTime(currentDay);
        calendarView.setMinimumDate(calendar);

        schedule.setValue(new Schedule(user, new ArrayList<>()));
        //schedule.getValue().setScheduleDay(new ArrayList<>());
        ScheduleDB scheduleDB = new ScheduleDB(getContext(), schedule);
        schedule.observe(getViewLifecycleOwner(), schedules -> {
            if (schedules.getScheduleDay()!=null){
                calendarView.setHighlightedDays(convertToCalendarList(schedules.getScheduleDay()));
            }
        });
        scheduleDB.getAllSchedule(user);


       calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
           @Override
           public void onClick(@NonNull CalendarDay calendarDay) {
               Calendar current = Calendar.getInstance();
               Calendar calendar = calendarDay.getCalendar();
               selectedDay = calendar;
               txtMonthYear.setText("Tháng "+(calendar.get(Calendar.MONTH) + 1)+", "+calendar.get(Calendar.YEAR));
               txtDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"");
               txtDayOfWeek.setText(convertToDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
               if (current.compareTo(calendar)<0){
                   if (schedule.getValue().getScheduleDay().contains(calendar.getTime())){
                       btnSign.setText("Hủy đăng ký");
                   }
                   else {
                       btnSign.setText("Đăng ký");
                   }
                   btnSign.setVisibility(View.VISIBLE);
               }
               else {
                   btnSign.setVisibility(View.INVISIBLE);
               }
           }
       });
       btnSign.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (schedule.getValue().getScheduleDay().contains(selectedDay.getTime()))
               {
                   schedule.getValue().getScheduleDay().remove(selectedDay.getTime());
               } else {
                   schedule.getValue().getScheduleDay().add(selectedDay.getTime());
               }
               scheduleDB.addNewSchedule(schedule.getValue());
               calendarView.setHighlightedDays(convertToCalendarList(schedule.getValue().getScheduleDay()));
           }
       });
    }
    public String convertToDayOfWeek(int day){
        switch (day) {
            case 1:
                return "Chủ Nhật";
            case 2:
                return "Thứ Hai";
            case 3:
                return "Thứ Ba";
            case 4:
                return "Thứ Tư";
            case 5:
                return "Thứ Năm";
            case 6:
                return "Thứ Sáu";
        }
        return "Thứ Bảy";
    }
    public static List<Calendar> convertToCalendarList(List<Date> dateList) {
        List<Calendar> calendarList = new ArrayList<>();
        for (Date date : dateList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendarList.add(calendar);
        }
        return calendarList;
    }
}