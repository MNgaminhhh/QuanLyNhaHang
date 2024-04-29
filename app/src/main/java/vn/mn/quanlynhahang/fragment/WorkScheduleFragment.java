package vn.mn.quanlynhahang.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.view.LichActivity;

public class WorkScheduleFragment extends Fragment {

    private EditText editTextName;
    private Spinner spinnerPosition;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private CheckBox checkBoxMorning;
    private CheckBox checkBoxNight;
    private CheckBox checkBoxBreak1;
    private CheckBox checkBoxBreak2;

    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextName = view.findViewById(R.id.textView2);
        spinnerPosition = view.findViewById(R.id.spinner);
        textViewStartDate = view.findViewById(R.id.buttonPreviousDay);
        textViewEndDate = view.findViewById(R.id.buttonNextDay);
        checkBoxMorning = view.findViewById(R.id.checkBoxMorning);
        checkBoxNight = view.findViewById(R.id.checkBoxNight);
        checkBoxBreak1 = view.findViewById(R.id.checkBoxBreak1);
        checkBoxBreak2 = view.findViewById(R.id.checkBoxBreak2);
        Button submitButton = view.findViewById(R.id.submitButton);

        String[] positions = {"Phục vụ bàn", "Nhân viên bếp", "Thu ngân"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, positions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPosition.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startYear = year;
                        startMonth = monthOfYear;
                        startDay = dayOfMonth;
                        textViewStartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },
                startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    private void showEndDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endYear = year;
                        endMonth = monthOfYear;
                        endDay = dayOfMonth;
                        textViewEndDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },
                endYear, endMonth, endDay);
        datePickerDialog.show();
    }


    public void onSubmit(View view) {
        String name = editTextName.getText().toString().trim();
        String selectedPosition = spinnerPosition.getSelectedItem().toString();

        if (!name.isEmpty()) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("position", selectedPosition);
            data.put("startDate", textViewStartDate.getText().toString());
            data.put("endDate", textViewEndDate.getText().toString());

            StringBuilder workHours = new StringBuilder();
            if (checkBoxMorning.isChecked()) {
                workHours.append("Ca sáng (06:00 - 14:00)\n");
            }
            if (checkBoxNight.isChecked()) {
                workHours.append("Ca tối (14:00 - 22:00)\n");
            }
            if (checkBoxBreak1.isChecked()) {
                workHours.append("Ca gãy (10:00 - 14:00)\n");
            }
            if (checkBoxBreak2.isChecked()) {
                workHours.append("Ca gãy (18:00 - 22:00)\n");
            }
            data.put("workHours", workHours.toString());

            FirebaseFirestore.getInstance().collection("employees")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(requireContext(), LichActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Lỗi khi lưu dữ liệu!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }

    }
}
