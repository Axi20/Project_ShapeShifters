package com.grow.shapeshifters.ui.manage_workouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grow.shapeshifters.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageWorkoutsAdapter extends RecyclerView.Adapter<ManageWorkoutsAdapter.ViewHolder> {
    private List<WorkoutDetail> workoutDetails;
    private Context context;

    public ManageWorkoutsAdapter(Context context,  List<WorkoutDetail> workoutDetails) {
        this.context = context;
        this.workoutDetails = workoutDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutDetail detail = workoutDetails.get(position);
        holder.workoutDate.setText(formatDate(detail.getWorkoutDate()) + " - " + detail.getClientName());
    }

    @Override
    public int getItemCount() {
        return workoutDetails.size();
    }

    private String formatDate(String dateString) {
        Locale locale = new Locale("hu", "HU");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy. MMM dd. HH:mm:ss", locale);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US);

        try {
            Date date = inputFormat.parse(dateString);
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutDate;

        public ViewHolder(View itemView) {
            super(itemView);
            workoutDate = itemView.findViewById(R.id.workout_date);
        }
    }
}
