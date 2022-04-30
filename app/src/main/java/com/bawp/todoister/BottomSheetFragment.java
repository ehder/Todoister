package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText enterTodo;
    private ImageButton calendarButton, priorityButton, saveButton;
    private CalendarView calendarView;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;

    private Date dueDate;
    private Calendar calendar = Calendar.getInstance();
    Group calendarGroup;

    public BottomSheetFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.isEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        selectedRadioButton = view.findViewById(R.id.radioButton_high);

        Chip todayChip, tomorrowChip, nextWeekChip;
        todayChip = view.findViewById(R.id.today_chip);
        tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        nextWeekChip = view.findViewById(R.id.next_week_chip);

        todayChip.setOnClickListener(this);
        tomorrowChip.setOnClickListener(this);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                Utils.hideSoftKeyboard(v);
            }

        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.clear();
                calendar.set(year, month, dayOfMonth);
                dueDate = calendar.getTime();

            }
        });


        priorityButton.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(v);
            priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

            priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (priorityRadioGroup.getVisibility() == View.VISIBLE){
                    selectedButtonId = checkedId;
                    Log.d("Bottom_", "selectedRadioButton's id: " + selectedRadioButton.getId());
                    if (selectedRadioButton.getId() == R.id.radioButton_high){
                         priority = Priority.HIGH;
                    }else if (selectedRadioButton.getId() == R.id.radioButton_med){
                        priority = Priority.MEDIUM;
                    }else if (selectedRadioButton.getId() == R.id.radioButton_low){
                        priority = Priority.LOW;
                    }else {
                        priority = Priority.MEDIUM;
                    }
                }
            });
        });

        saveButton.setOnClickListener(v -> {

            String task = enterTodo.getText().toString().trim();

            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null){
                Task myTask = new Task();
                myTask.setTask(task);
                myTask.setPriority(priority);
                myTask.setDueDate(dueDate);
                myTask.setDateCreated(Calendar.getInstance().getTime());
                myTask.setDone(false);

                if (isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setDone(false);
                    TaskViewModel.update(updateTask);
                    saveButton.setImageResource(R.drawable.ic_baseline_arrow_circle_up_green_24);
                }else {
                    TaskViewModel.insert(myTask);
                    sharedViewModel.setEdit(false);
                    saveButton.setImageResource(R.drawable.ic_baseline_arrow_circle_up_green_24);
                }

                enterTodo.setText("");
                if (this.isVisible()){
                    this.dismiss();
                }

            }else {
                Snackbar.make(saveButton, R.string.empty_field, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.today_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
        }else if (id == R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
        }else if (id == R.id.next_week_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
        }
    }
}