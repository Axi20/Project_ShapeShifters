package com.grow.shapeshifters.ui.personal_trainings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentPersonalTrainingsBinding;
import com.grow.shapeshifters.ui.manage_clients.ManageClientsFragment;

public class PersonalTrainingsFragment extends Fragment {

    private FragmentPersonalTrainingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPersonalTrainingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.addWorkoutLayout.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(PersonalTrainingsFragment.this);
            navController.navigate(R.id.nav_manage_workouts);
        });

        ImageView notesImageView = binding.notesImage;
        ImageView workoutImageView = binding.workoutImage;
        ImageView scheduleImageView = binding.scheduleImage;

        // Load image from drawable resource
        int resourceId = R.drawable.workout;
        Glide.with(this)
                .load(resourceId)
                .into(workoutImageView);

        int resourceId2 = R.drawable.progress;
        Glide.with(this)
                .load(resourceId2)
                .into(notesImageView);

        int resourceId3 = R.drawable.schedule;
        Glide.with(this)
                .load(resourceId3)
                .into(scheduleImageView);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}