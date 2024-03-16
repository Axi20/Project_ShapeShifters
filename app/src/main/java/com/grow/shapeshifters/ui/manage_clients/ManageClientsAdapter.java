package com.grow.shapeshifters.ui.manage_clients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grow.shapeshifters.R;
import java.util.List;

public class ManageClientsAdapter extends RecyclerView.Adapter<ManageClientsAdapter.ViewHolder> {

    private List<Client> clients;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ManageClientsAdapter(Context context, List<Client> clients) {
        this.mInflater = LayoutInflater.from(context);
        this.clients = clients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView clientName;
        ImageButton clientDetailsBtn, clientEditBtn, clientDeleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.client_name);
            clientDetailsBtn = itemView.findViewById(R.id.show_details_btn);
            clientEditBtn = itemView.findViewById(R.id.edit_btn);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.client_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.clientName.setText(client.getName());
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    Client getItem(int id) {
        return clients.get(id);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}

