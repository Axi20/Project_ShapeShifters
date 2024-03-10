package com.grow.shapeshifters.ui.group_trainings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.grow.shapeshifters.databinding.FragmentGroupTrainingsBinding;

public class GroupTrainingsFragment extends Fragment {

    private FragmentGroupTrainingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroupTrainingsViewModel groupTrainingsViewModel =
                new ViewModelProvider(this).get(GroupTrainingsViewModel.class);

        binding = FragmentGroupTrainingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGroupTraining;
        groupTrainingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}