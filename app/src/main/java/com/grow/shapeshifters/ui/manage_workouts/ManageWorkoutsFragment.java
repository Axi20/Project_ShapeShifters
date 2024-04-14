package com.grow.shapeshifters.ui.manage_workouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grow.shapeshifters.DatabaseHelper;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentManageWorkoutsBinding;

import java.util.ArrayList;
import java.util.List;

public class ManageWorkoutsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ManageWorkoutsAdapter adapter;
    private List<WorkoutDetail> workoutDetails;
    private FragmentManageWorkoutsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageWorkoutsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        binding.addWorkoutsBtn.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(ManageWorkoutsFragment.this);
            navController.navigate(R.id.nav_add_workouts);
        });

        workoutDetails = new ArrayList<>();
        recyclerView = binding.workoutsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ManageWorkoutsAdapter(getContext(), workoutDetails);
        recyclerView.setAdapter(adapter);

        fetchWorkoutsAndUpdateUI();
        return root;
    }

    private void fetchWorkoutsAndUpdateUI() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        workoutDetails.clear();
        workoutDetails.addAll(db.getWorkoutDetails());
        db.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
