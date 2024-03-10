package com.grow.shapeshifters.ui.manage_clients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.grow.shapeshifters.databinding.FragmentManageClientsBinding;

public class ManageClientsFragment extends Fragment {

    private FragmentManageClientsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManageClientsViewModel manageClientsFragment =
                new ViewModelProvider(this).get(ManageClientsViewModel.class);

        binding = FragmentManageClientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textManageClients;
        manageClientsFragment.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}