package com.grow.shapeshifters.ui.manage_clients;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.grow.shapeshifters.DatabaseHelper;
import com.grow.shapeshifters.R;
import com.grow.shapeshifters.ui.manage_workouts.Exercise;
import com.grow.shapeshifters.ui.manage_workouts.ManageWorkoutsAdapter;
import com.grow.shapeshifters.ui.manage_workouts.WorkoutDetail;

import java.util.List;
import java.util.Locale;

public class ManageClientsAdapter extends RecyclerView.Adapter<ManageClientsAdapter.ViewHolder> {

    private List<Client> clients;
    private ItemClickListener mClickListener;
    private DatabaseHelper databaseHelper;
    private Context context;

    public ManageClientsAdapter(Context context,  List<Client> clients) {
        this.context = context;
        this.clients = clients;
        this.databaseHelper = new DatabaseHelper(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView clientName;
        ImageButton clientDetailsBtn, clientDeleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.client_name);
            clientDetailsBtn = itemView.findViewById(R.id.show_details_btn);
            clientDeleteBtn = itemView.findViewById(R.id.delete_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ManageClientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.clientDeleteBtn.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showDeleteConfirmationDialog(adapterPosition);
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.clientName.setText(client.getName());
        holder.clientDetailsBtn.setOnClickListener(v -> {
            showDetailsDialog(client);
        });
    }

    private void showDetailsDialog(Client client) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_client_details, null);
        builder.setView(dialogView);

        TextView clientName = dialogView.findViewById(R.id.client_name);
        TextView clientDateOfBirth = dialogView.findViewById(R.id.client_date_of_birth);
        TextView clientPhoneNumber = dialogView.findViewById(R.id.client_phone_number);
        TextView clientFitnessLevel = dialogView.findViewById(R.id.client_fitness_level);
        TextView clientFitnessGoal = dialogView.findViewById(R.id.client_fitness_goals);
        TextView clientPreferredTrainingSlot = dialogView.findViewById(R.id.client_training_slots_view);
        TextView clientMembershipStartDate = dialogView.findViewById(R.id.client_membership_start);
        TextView clientWeight = dialogView.findViewById(R.id.client_weight);
        EditText clientNotes = dialogView.findViewById(R.id.client_notes);
        Button saveButton = dialogView.findViewById(R.id.save_button);


        clientName.setText(context.getString(R.string.client_name_with_placeholder, client.getName()));
        clientDateOfBirth.setText(context.getString(R.string.client_date_of_birth_with_placeholder, client.getDateOfBirth()));
        clientPhoneNumber.setText(context.getString(R.string.client_phone_number_with_placeholder, client.getPhoneNumber()));
        clientFitnessLevel.setText(context.getString(R.string.client_fitness_level_with_placeholder, client.getFitnessLevel()));
        clientFitnessGoal.setText(context.getString(R.string.client_fitness_goal_with_placeholder, client.getFitnessGoal()));
        //clientPreferredTrainingSlot.setText(context.getString(R.string.client_training_slot_with_placeholder, client.getDayOfWeek(), client.getTimeSlot()));

        if (client.getTrainingSlots() != null && !client.getTrainingSlots().isEmpty()) {
            StringBuilder slotsText = new StringBuilder();
            for (TrainingSlot slot : client.getTrainingSlots()) {
                slotsText.append(String.format("%s - %s\n", slot.getDayOfWeek(), slot.getTimeSlot()));
            }
            clientPreferredTrainingSlot.setText(context.getString(R.string.client_training_slot_with_placeholder, slotsText.toString()));
        }

        clientMembershipStartDate.setText(context.getString(R.string.client_membership_start_date_with_placeholder, client.getMembershipStartDate()));
        clientWeight.setText(context.getString(R.string.client_weight_with_placeholder, client.getWeight()));
        clientNotes.setText(context.getString(R.string.client_notes_with_placeholder, client.getNotes()));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void deleteItem(int position) {
        Client client = clients.get(position);
        new Thread(() -> {
            databaseHelper.deleteClient(client.getId());
            ((Activity)context).runOnUiThread(() -> {
                clients.remove(position);
                notifyItemRemoved(position);
            });
        }).start();
    }

    public void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.confirm_client_delete))
                .setPositiveButton(context.getString(R.string.delete), (dialog, which) -> deleteItem(position))
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show();
    }
}


