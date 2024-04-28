package com.grow.shapeshifters.ui.manage_workouts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.grow.shapeshifters.DatabaseHelper;
import com.grow.shapeshifters.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageWorkoutsAdapter extends RecyclerView.Adapter<ManageWorkoutsAdapter.ViewHolder> {
    private List<WorkoutDetail> workoutDetails;
    private Context context;
    private DatabaseHelper databaseHelper;

    public ManageWorkoutsAdapter(Context context,  List<WorkoutDetail> workoutDetails) {
        this.context = context;
        this.workoutDetails = workoutDetails;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.deleteButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showDeleteConfirmationDialog(adapterPosition);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutDetail detail = workoutDetails.get(position);
        holder.workoutDate.setText(context.getString(R.string.workout_date2_with_placeholder, formatDate(detail.getWorkoutDate()), detail.getClientName()));
        holder.showDetailsButton.setOnClickListener(v -> {
            showDetailsDialog(detail);
        });
    }

    @Override
    public int getItemCount() {
        return workoutDetails.size();
    }

    public void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.confirm_delete))
                .setPositiveButton(context.getString(R.string.delete), (dialog, which) -> deleteItem(position))
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show();
    }

    private void deleteItem(int position) {
        WorkoutDetail detail = workoutDetails.get(position);
        new Thread(() -> {
            databaseHelper.deleteWorkout(detail.getWorkoutId());
            ((Activity)context).runOnUiThread(() -> {
                workoutDetails.remove(position);
                notifyItemRemoved(position);
            });
        }).start();
    }

    public static String formatDate(String dateString) {
        Locale defaultLocale = Locale.getDefault();

        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", defaultLocale);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", defaultLocale);

        try {
            Date date = inputFormat.parse(dateString);
            if (date != null) {
                return outputFormat.format(date);
            } else {
                return "Invalid date";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    private void showDetailsDialog(WorkoutDetail detail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_personal_workout_details, null);
        builder.setView(dialogView);

        TextView clientName = dialogView.findViewById(R.id.client_name);
        EditText workoutNotes = dialogView.findViewById(R.id.workout_notes);
        TextView workoutDate = dialogView.findViewById(R.id.workout_date);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        clientName.setText(context.getString(R.string.client_name_with_placeholder, detail.getClientName()));
        workoutNotes.setText(context.getString(R.string.workout_notes_with_placeholder, detail.getWorkoutNotes()));
        workoutDate.setText(context.getString(R.string.workout_date_with_placeholder, formatDate(detail.getWorkoutDate())));

        TableLayout tableLayout = dialogView.findViewById(R.id.exercise_details_table);
        List<Exercise> exercises = databaseHelper.getAllExercises(detail.getWorkoutId());
        tableLayout.removeAllViews();
        addExerciseDetailRow(tableLayout, context.getString(R.string.exercise), context.getString(R.string.reps2), context.getString(R.string.sets2), context.getString(R.string.weight2), true); // Header row

        // Add a row for each exercise.
        for (Exercise exercise : exercises) {
            addExerciseDetailRow(tableLayout,
                    exercise.getName(),
                    String.valueOf(exercise.getRepetitions()),
                    String.valueOf(exercise.getSets()),
                    exercise.getWeight() + " kg",
                    false);
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addExerciseDetailRow(TableLayout table, String name, String reps, String sets, String weight, boolean isHeader) {
        TableRow tableRow = new TableRow(context);
        tableRow.addView(createTextViewForTable(name, isHeader));
        tableRow.addView(createTextViewForTable(reps, isHeader));
        tableRow.addView(createTextViewForTable(sets, isHeader));
        tableRow.addView(createTextViewForTable(weight, isHeader));
        table.addView(tableRow);
    }

    private TextView createTextViewForTable(String text, boolean isHeader) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8);
        if (isHeader) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        return textView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutDate;
        ImageButton deleteButton;
        ImageButton showDetailsButton;
        public ViewHolder(View itemView) {
            super(itemView);
            workoutDate = itemView.findViewById(R.id.workout_date);
            deleteButton = itemView.findViewById(R.id.delete_btn);
            showDetailsButton = itemView.findViewById(R.id.show_details_btn);
        }
    }
}
