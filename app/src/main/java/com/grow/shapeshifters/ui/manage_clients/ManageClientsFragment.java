package com.grow.shapeshifters.ui.manage_clients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentManageClientsBinding;

public class ManageClientsFragment extends Fragment {

    private FragmentManageClientsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManageClientsViewModel manageClientsFragment =
                new ViewModelProvider(this).get(ManageClientsViewModel.class);

        binding = FragmentManageClientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*
        final TextView textView = binding.textManageClients;
        manageClientsFragment.getText().observe(getViewLifecycleOwner(), textView::setText);
         */

        binding.addClientsBtn.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(ManageClientsFragment.this);
            // Use the action ID to navigate
            navController.navigate(R.id.nav_add_clients);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}