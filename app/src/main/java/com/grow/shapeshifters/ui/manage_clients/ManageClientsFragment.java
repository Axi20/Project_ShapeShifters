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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.grow.shapeshifters.DatabaseHelper;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.databinding.FragmentManageClientsBinding;
import java.util.ArrayList;
import java.util.List;

public class ManageClientsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ManageClientsAdapter adapter;
    private List<Client> clients;
    private FragmentManageClientsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManageClientsViewModel manageClientsFragment =
                new ViewModelProvider(this).get(ManageClientsViewModel.class);

        binding = FragmentManageClientsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        clients = new ArrayList<>();

        // Setup RecyclerView and Adapter.
        RecyclerView recyclerView = binding.clientsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ManageClientsAdapter(getContext(), clients);
        recyclerView.setAdapter(adapter);

        // Fetch clients and update the RecyclerView.
        fetchClientsAndUpdateUI();

        binding.addClientsBtn.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(ManageClientsFragment.this);
            navController.navigate(R.id.nav_add_clients);
        });

        return root;
    }

    /**
     * Fetches all client records from the database and updates the UI to display them.
     * This method retrieves a list of all clients stored in the database by calling
     * the {@link DatabaseHelper#getAllClients()} method. It then clears the current list
     * of clients maintained by the adapter and adds all fetched clients to ensure the
     * RecyclerView displays the most up-to-date information. Finally, it notifies the
     * adapter that the underlying dataset has changed to refresh the displayed list.
     */
    private void fetchClientsAndUpdateUI() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<Client> updatedClients = db.getAllClients();

        // Update adapter's dataset.
        this.clients.clear();
        this.clients.addAll(updatedClients);

        // Notify the adapter that the data has changed.
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}