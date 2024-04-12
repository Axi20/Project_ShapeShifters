package com.grow.shapeshifters.ui.manage_workouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentManageWorkoutsBinding;

public class ManageWorkoutsFragment extends Fragment {

    private FragmentManageWorkoutsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageWorkoutsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        binding.addWorkoutsBtn.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(ManageWorkoutsFragment.this);
            navController.navigate(R.id.nav_add_workouts);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
