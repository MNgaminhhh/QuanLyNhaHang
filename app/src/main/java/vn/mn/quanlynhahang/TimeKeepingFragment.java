package vn.mn.quanlynhahang;

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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.mn.quanlynhahang.fragment.ScheduleFragment;
import vn.mn.quanlynhahang.model.Schedule;
import vn.mn.quanlynhahang.model.ScheduleDB;
import vn.mn.quanlynhahang.model.TimeInOut;
import vn.mn.quanlynhahang.model.TimeKeeping;
import vn.mn.quanlynhahang.model.TimeKeepingDB;

public class TimeKeepingFragment extends Fragment {
    Button btnCheckin;
    TextView txtNoti, txtTimeIn, txtTimeOut;
    MutableLiveData<TimeKeeping> timeKeeping = new MutableLiveData<>();
    MutableLiveData<Schedule> schedule = new MutableLiveData<>();
    int idToday;
    boolean isScheduled =true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_keeping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCheckin = view.findViewById(R.id.btnCheckin);
        txtNoti = view.findViewById(R.id.txtNoti);
        txtTimeIn = view.findViewById(R.id.txtTimeCheckIn);
        txtTimeOut = view.findViewById(R.id.txtTimeCheckOut);

        ScheduleDB scheduleDB = new ScheduleDB(getContext(), schedule);
        schedule.setValue(new Schedule(ScheduleFragment.user, new ArrayList<>()));
        schedule.observe(getViewLifecycleOwner(), schedules -> {
            if (schedules.getScheduleDay()!=null){
                if (!thisListHaveToDay(schedules.getScheduleDay())){
                    txtNoti.setText("Bạn không có ca làm trong ngày hôm nay!");
                    isScheduled = false;
                }
                else {
                    setCheckInEvent();
                }
            }
        });
        scheduleDB.getAllSchedule(ScheduleFragment.user);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");



        timeKeeping.setValue(new TimeKeeping());
        timeKeeping.getValue().setUser(ScheduleFragment.user);
        timeKeeping.getValue().setTimeList(new ArrayList<>());

        TimeKeepingDB timeKeepingDB = new TimeKeepingDB(getContext(), timeKeeping);
        timeKeeping.observe(getViewLifecycleOwner(), timeKeeping1 -> {
            idToday = timeKeeping1.getIndexForTimeInOutForToday();
            if (isScheduled){
                if (idToday!=-1){
                    txtTimeIn.setText("Giờ vào làm: "+ sdf.format(timeKeeping1.getTimeList().get(idToday).getTimeIn()));
                    if (timeKeeping1.getTimeList().get(idToday).getTimeOut()!=null){
                        txtTimeOut.setText("Giờ kết thúc: "+ sdf.format(timeKeeping1.getTimeList().get(idToday).getTimeOut()));
                        btnCheckin.setText("Đã kết thúc ca làm việc");
                        btnCheckin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }
                    else {
                        btnCheckin.setText("Kết thúc ca làm");
                        setCheckOutEvent();
                    }
                }
            }
        });
        timeKeepingDB.getTimeKeeping(ScheduleFragment.user);

    }
    public void setCheckInEvent(){
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeKeepingDB timeKeepingDB = new TimeKeepingDB(getContext(), timeKeeping);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date current = new Date();
                timeKeeping.getValue().getTimeList().add(new TimeInOut(current, null));
                timeKeepingDB.addTimeKeeping(timeKeeping.getValue());
                idToday = timeKeeping.getValue().getTimeList().size()-1;
                txtTimeIn.setText("Giờ vào làm: "+ sdf.format(current));
                txtNoti.setText("Xác nhận bắt đầu ca làm việc thành công!");
                btnCheckin.setText("Kết thúc ca làm");
                setCheckOutEvent();
            }
        });
    }
    public void setCheckOutEvent(){
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeKeepingDB timeKeepingDB = new TimeKeepingDB(getContext(), timeKeeping);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date current = new Date();
                timeKeeping.getValue().getTimeList().get(idToday).setTimeOut(current);
                timeKeepingDB.addTimeKeeping(timeKeeping.getValue());
                txtTimeOut.setText("Giờ kết thúc: "+ sdf.format(current));
                txtNoti.setText("Xác nhận kết thúc ca làm việc thành công!");
                btnCheckin.setText("Đã kết thúc ca làm việc");
                btnCheckin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
    }
    public boolean thisListHaveToDay(List<Date> listDate) {
        Date today = new Date();
        for (int i = 0; i < listDate.size(); i++) {
            Date timeIn = listDate.get(i);
            if (timeIn.getYear() == today.getYear() &&
                    timeIn.getMonth() == today.getMonth() &&
                    timeIn.getDate() == today.getDate()) {
                return true;
            }
        }
        return false;
    }
}