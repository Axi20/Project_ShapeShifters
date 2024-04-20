package com.grow.shapeshifters.ui.add_workouts;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.grow.shapeshifters.DatabaseHelper;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentAddWorkoutsBinding;
import com.grow.shapeshifters.ui.manage_clients.Client;
import com.grow.shapeshifters.ui.manage_workouts.Exercise;
import com.grow.shapeshifters.ui.manage_workouts.ManageWorkoutsAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddWorkoutsFragment extends Fragment {
    private FragmentAddWorkoutsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddWorkoutsBinding.inflate(inflater, container, false);

        Spinner spinnerChooseClient = binding.chooseClientSpinner;

        // Get the list of clients from the database.
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        List<Client> clients = databaseHelper.getAllClients();

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<Client> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, clients);
        spinnerChooseClient.setAdapter(adapter);
        databaseHelper.close();

        binding.addDateBtn.setOnClickListener(view -> {
            showDateTimeDialog(binding.dateOfWorkout);
        });

        binding.addExerciseBtn.setOnClickListener(view -> {
            showAddExerciseDialog();
        });

        binding.addWorkoutBtn.setOnClickListener(view -> {
            saveWorkout();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void showDateTimeDialog(final TextView textViewDateAndTime) {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                textViewDateAndTime.setText(DateFormat.getDateTimeInstance().format(date.getTime()));
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void showAddExerciseDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise, null);

        // Create the AlertDialog builder.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setView(dialogView);

        // Access the EditText fields from the custom layout.
        final EditText editTextExerciseName = dialogView.findViewById(R.id.editTextExerciseName);
        final EditText editTextReps = dialogView.findViewById(R.id.editTextReps);
        final EditText editTextSets = dialogView.findViewById(R.id.editTextSets);
        final EditText editTextWeight = dialogView.findViewById(R.id.editTextWeight);

        // Set up the buttons.
        dialogBuilder.setPositiveButton(requireContext().getString(R.string.add), (dialog, which) -> {
            String exerciseName = editTextExerciseName.getText().toString().trim();
            String repsString = editTextReps.getText().toString().trim();
            String setsString = editTextSets.getText().toString().trim();
            String weightString = editTextWeight.getText().toString().trim();

            // Check if any of the fields are empty.
            if (exerciseName.isEmpty() || repsString.isEmpty() || setsString.isEmpty()) {
                Toast.makeText(getContext(), requireContext().getString(R.string.required_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            // Initialize the variables.
            int reps = 0;
            int sets = 0;
            float weight = 0f;

            // Use try-catch to handle NumberFormatException.
            try {
                reps = Integer.parseInt(repsString);
                sets = Integer.parseInt(setsString);
                weight = Float.parseFloat(weightString);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), requireContext().getString(R.string.bad_input), Toast.LENGTH_SHORT).show();
                return;
            }
            addExerciseToTable(exerciseName, reps, sets, weight);
        });

        dialogBuilder.setNegativeButton(requireContext().getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        // Create and show the AlertDialog.
        AlertDialog addExerciseDialog = dialogBuilder.create();
        addExerciseDialog.show();
    }
    private void addExerciseToTable(String exerciseName, int reps, int sets, float weight) {
        TableLayout tableLayout = binding.exercisesTable;

        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // A helper method to create text views simplifies this process.
        tableRow.addView(createTextView(exerciseName));
        tableRow.addView(createTextView(String.valueOf(reps)));
        tableRow.addView(createTextView(String.valueOf(sets)));
        tableRow.addView(createTextView(String.format(Locale.getDefault(), "%.2f kg", weight)));

        tableLayout.addView(tableRow);
    }
    private TextView createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        return textView;
    }
    public void saveWorkout() {
        long clientId = ((Client)binding.chooseClientSpinner.getSelectedItem()).getId();
        String workoutDate = ManageWorkoutsAdapter.formatDate(binding.dateOfWorkout.getText().toString());
        String notes = binding.addNotes.getText().toString();

        List<Exercise> exercises = collectExercisesFromTable();

        if (getContext() == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        // @TODO Fix this part as it's still allow to save the workout for the same client and same time slot.
        if (!databaseHelper.workoutExists(clientId, workoutDate)) {
            long workoutId = databaseHelper.saveWorkoutDetails(clientId, workoutDate, notes, exercises);
            if (workoutId != -1) {
                Toast.makeText(getContext(), getString(R.string.workout_added), Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.nav_manage_workouts);
            } else {
                Toast.makeText(getContext(), getString(R.string.workout_add_failed), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.workout_already_exists), Toast.LENGTH_SHORT).show();
        }
        databaseHelper.close();
    }
    private List<Exercise> collectExercisesFromTable() {
        List<Exercise> exercises = new ArrayList<>();
        TableLayout table = binding.exercisesTable;

        // Start from 1 to skip the header row.
        for (int i = 1; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);

            // Extract details from each column (TextView) in the row.
            String name = ((TextView) row.getChildAt(0)).getText().toString();
            int reps = Integer.parseInt(((TextView) row.getChildAt(1)).getText().toString());
            int sets = Integer.parseInt(((TextView) row.getChildAt(2)).getText().toString());
            //Float weight = Float.parseFloat(((TextView) row.getChildAt(3)).getText().toString().replace(" kg", ""));
            String weightString = ((TextView) row.getChildAt(3)).getText().toString().replace(" kg", "");
            // Replace comma with dot for proper parsing
            weightString = weightString.replace(",", ".");
            Float weight = Float.parseFloat(weightString);
            exercises.add(new Exercise(name, reps, sets, weight));
        }

        return exercises;
    }

}
