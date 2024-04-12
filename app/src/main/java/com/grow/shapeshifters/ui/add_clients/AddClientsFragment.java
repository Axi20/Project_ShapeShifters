package com.grow.shapeshifters.ui.add_clients;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.grow.shapeshifters.DatabaseHelper;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentAddClientsBinding;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddClientsFragment extends Fragment {
    private FragmentAddClientsBinding binding;
    private String selectedLevel = "";
    Map<String, String> selectedDays = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddClientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.checkboxMonday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxMonday, "Monday"));
        binding.checkboxTuesday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxTuesday, "Tuesday"));
        binding.checkboxWednesday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxWednesday, "Wednesday"));
        binding.checkboxThursday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxThursday, "Thursday"));
        binding.checkboxFriday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxFriday, "Friday"));
        binding.checkboxSaturday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxSaturday, "Saturday"));
        binding.checkboxSunday.setOnCheckedChangeListener((buttonView, isChecked) -> collectDayAndTime(binding.checkboxSunday, "Sunday"));

        Spinner fitnessLevelDropdown = binding.fitnessLevelSpinner;
        String[] level = new String[]{"Beginner", "Intermediate", "Advanced"};
        // Create an adapter to describe how the items are displayed.
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, level);
        fitnessLevelDropdown.setAdapter(levelAdapter);

        // Choose the client's current fitness level.
        binding.fitnessLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLevel = binding.fitnessLevelSpinner.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO: Handle the case when nothing is selected
            }
        });

        // Add the client's data to the database.
        binding.clientAddBtn.setOnClickListener(view -> saveClientData());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Displays a TimePickerDialog to allow the user to select a time.
     * Once a time is selected, it sets the time as text on the provided CheckBox and stores it in a map.
     *
     * @param checkBox The CheckBox associated with the day of the week to display the selected time.
     * @param dayKey A string representing the day of the week, used as a key to store the selected time.
     */
    private void showTimePickerDialog(final CheckBox checkBox, final String dayKey) {
        // Initialize a new instance of TimePickerDialog.
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                (view, hourOfDay, minute) -> {
                    // Format the time and set it as the text of the checkbox.
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    checkBox.setText(dayKey + " " + selectedTime);
                    selectedDays.put(dayKey, selectedTime);
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), // Current hour.
                Calendar.getInstance().get(Calendar.MINUTE), // Current minute.
                true // Is 24 hour view.
        );
        timePickerDialog.setOnCancelListener(dialog -> {
            checkBox.setChecked(false);
            selectedDays.remove(dayKey);
        });
        timePickerDialog.setOnDismissListener(dialog -> {
            if (!checkBox.isChecked()) {
                selectedDays.remove(dayKey);
            }
        });
        timePickerDialog.show();
    }

    /**
     * Saves client data into the database.
     * This method collects client data from the input fields.
     * It then attempts to save this data into the database
     * using the {@link DatabaseHelper}. If the save is successful, it navigates to
     * the manage clients screen and displays a success toast message. Otherwise,
     * it shows an error toast message.
     * Selected training slots for each day of the week are also saved. If the client ID
     * is successfully generated after adding client information, each training slot is
     * added to the database associated with the client's ID. If no ID is generated,
     * the user is informed of the failure.
     */
    private void saveClientData() {
        String name = binding.clientName.getText().toString();
        String gender = binding.clientMale.isChecked() ? "Male" : "Female";
        String dob = binding.clientDateOfBirth.getText().toString();
        String phone = binding.clientPhoneNumber.getText().toString();
        String fitnessGoals = binding.clientFitnessGoals.getText().toString();
        String membershipStartDate = binding.clientMembershipStartDate.getText().toString();
        double weight = Double.parseDouble(binding.clientWeight.getText().toString());
        double bodyFat = Double.parseDouble(binding.clientBodyFatPercentage.getText().toString());

        DatabaseHelper db = new DatabaseHelper(getContext());

        Map<String, String> trainingSlots = new HashMap<>();
        extractTrainingSlot(binding.checkboxMonday, trainingSlots, "Monday");
        extractTrainingSlot(binding.checkboxTuesday, trainingSlots, "Tuesday");
        extractTrainingSlot(binding.checkboxWednesday, trainingSlots, "Wednesday");
        extractTrainingSlot(binding.checkboxThursday, trainingSlots, "Thursday");
        extractTrainingSlot(binding.checkboxFriday, trainingSlots, "Friday");
        extractTrainingSlot(binding.checkboxSaturday, trainingSlots, "Saturday");
        extractTrainingSlot(binding.checkboxSunday, trainingSlots, "Sunday");

        long clientId = db.addClientWithTrainingSlots(name, gender, dob, phone, selectedLevel, fitnessGoals, membershipStartDate, weight, bodyFat, trainingSlots);

        for (Map.Entry<String, String> entry : selectedDays.entrySet()) {
            String day = entry.getKey();
            String time = entry.getValue();
            db.addTrainingSlotForClient(clientId, day, time);
        }
        if (clientId != -1) {
            NavController navController = NavHostFragment.findNavController(AddClientsFragment.this);
            navController.navigate(R.id.nav_manage_clients);
            Toast.makeText(getActivity(), "Client added!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "Failed to add client, try again!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    /**
     * Extracts the training slot time from a CheckBox and stores it in a map.
     * If the passed CheckBox is checked, this method will parse its text to extract
     * the time part, which is assumed to be after the first space character in the format
     * "Day HH:mm". It then stores the day as the key and time as the value in the provided
     * map of training slots.
     *
     * @param checkBox The CheckBox from which to extract the time.
     * @param trainingSlots The map where the day and time are to be stored.
     * @param day The day of the week that corresponds to the CheckBox.
     */
    private void extractTrainingSlot(CheckBox checkBox, Map<String, String> trainingSlots, String day) {
        if (checkBox.isChecked()) {
            // Extract the time part from the checkbox text.
            String checkboxText = checkBox.getText().toString();
            String time = checkboxText.substring(checkboxText.indexOf(" ") + 1);
            trainingSlots.put(day, time);
        }
    }

    /**
     * Handles the selection and unselection of training days.
     * When a day is selected, this method triggers a {@link TimePickerDialog} to allow
     * the user to pick a time slot for the training. If the day is deselected, the method
     * removes the day from the selectedDays map and resets the checkbox text to the default.
     *
     * @param checkBox The CheckBox representing the day of the week being processed.
     * @param dayKey   The string representing the day key (e.g., "Monday").
     */
    private void collectDayAndTime(CheckBox checkBox, String dayKey) {
        if (checkBox.isChecked()) {
            showTimePickerDialog(checkBox, dayKey);
        } else {
            selectedDays.remove(dayKey);
            resetCheckboxText(checkBox, dayKey);
        }
    }

    /**
     * Resets the text of the provided checkbox based on the day key.
     * This method updates the text of a checkbox to its default value which is the
     * abbreviation of the day of the week (e.g., 'M' for Monday). It is used when
     * a previously checked checkbox for selecting a training slot is unchecked.
     *
     * @param checkBox The checkbox whose text is to be reset.
     * @param dayKey   The day key corresponding to the checkbox which determines
     * the text to be set. It should match the actual days of the week
     * (e.g., "Monday", "Tuesday", etc.).
     */
    private void resetCheckboxText(CheckBox checkBox, String dayKey) {
        switch (dayKey) {
            case "Monday":
                checkBox.setText(R.string.hint_m);
                break;
            case "Tuesday":
                checkBox.setText(R.string.hint_t);
                break;
            case "Wednesday":
                checkBox.setText(R.string.hint_w);
                break;
            case "Thursday":
                checkBox.setText(R.string.hint_th);
                break;
            case "Friday":
                checkBox.setText(R.string.hint_f);
                break;
            case "Saturday":
                checkBox.setText(R.string.hint_s);
                break;
            case "Sunday":
                checkBox.setText(R.string.hint_sun);
                break;
            default:
                break;
        }
    }
}