package com.grow.shapeshifters.ui.personal_trainings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}