package app.project.pomolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ncorti.slidetoact.SlideToActView;

import java.util.Calendar;
import java.util.Objects;

import app.project.pomolist.control.TaskManager;
import app.project.pomolist.model.Task;

/**
 * Activity allows the user to edit the fields of a task, save options, and remove a task.
 */
public class ConfigureTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TaskManager taskManager = TaskManager.getInstance();
    private DatePicker datePicker;

    private static final String STATE_MSG = "Passing game state";
    private final String NO_DATE = "No date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_task);
        setActionBar();

        dueDatePicker();
        setSaveConfirmationSlider();
    }

    public static Intent launchIntent(Context context, String state) {
        Intent intent = new Intent(context, ConfigureTaskActivity.class);
        intent.putExtra(STATE_MSG, state);
        return intent;
    }

    private void setActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.new_task_title);
        editActionBarTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void editActionBarTitle() {
        // TODO: Get whether it's add or edit and change the title accordingly
    }

    // ***********************************************************
    // Let the user pick a due date
    // ***********************************************************

    private void dueDatePicker() {
        setDatePicker();
        setTimePicker();
        setNoDueDateOption();
    }

    private void setDatePicker() {
        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ConfigureTaskActivity.this,
                        ConfigureTaskActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "month/day/year: " + month + "/" + dayOfMonth + "/" + year;

        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(date);
    }

    private void setTimePicker() {
//        Button timeButton = findViewById(R.id.timeButton);
//        timeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    private void setNoDueDateOption() {
        CheckBox noDueDateCheckBox = findViewById(R.id.noDueDate);
        noDueDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TextView dateText = findViewById(R.id.dateText);
                    dateText.setText(NO_DATE);
                }
            }
        });
    }

    // ***********************************************************
    // Save slider implementation
    // ***********************************************************

    private void setSaveConfirmationSlider() {
        SlideToActView sta = findViewById(R.id.confirmationSlider);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                if (allInfoIsValid()) {
                    addTask();
                    finish();
                } else {
                    showToastWarning();
                    sta.resetSlider();
                }
            }
        });
    }

    private void addTask() {
        String taskName = getTaskNameFromInput();
        // TODO: Get date from datePicker and pass into list

        Task newTask = new Task(taskName, null);
        taskManager.addTask(newTask);
    }

    private boolean allInfoIsValid() {
        String taskName = getTaskNameFromInput();
        return TaskManager.isValidName(taskName);
    }

    private void showToastWarning() {
        String warning = "Information is invalid!";
        Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT)
                .show();
    }

    private String getTaskNameFromInput() {
        EditText taskName = findViewById(R.id.taskName);
        return taskName.getText().toString();
    }

    // ***********************************************************
    // Delete task
    // ***********************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.configure_task_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_task) {
            promptDeletion();
        }
        return true;
    }

    private void promptDeletion() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ConfigureTaskActivity.this);
        alert.setMessage(R.string.task_deletion_warning)
                .setPositiveButton("Yes", ((dialogInterface, i) -> {
                    // TODO: Delete task
                    finish();
                }))
                .setNegativeButton("No", null);
        alert.show();
    }

    // set confirmation slider


}