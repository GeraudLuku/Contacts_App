package com.geraudluku.contactsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geraudluku.contactsapp.Interfaces.WazapayUsersRecyclerInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WazapayUserContactsRecyclerAdapter extends RecyclerView.Adapter<WazapayUserContactsRecyclerAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Contact> wazapayContactsList;
    private WazapayUsersRecyclerInterface wazapayUsersRecyclerInterface;

    private List<Contact> contactListFiltered;

    public WazapayUserContactsRecyclerAdapter(Context context, ArrayList<Contact> wazapayContactsList, WazapayUsersRecyclerInterface wazapayUsersRecyclerInterface) {
        this.context = context;
        this.wazapayContactsList = wazapayContactsList;
        this.contactListFiltered = wazapayContactsList;
        this.wazapayUsersRecyclerInterface = wazapayUsersRecyclerInterface;
    }

    @NonNull
    @Override
    public WazapayUserContactsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_contacts_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WazapayUserContactsRecyclerAdapter.MyViewHolder holder, int position) {

        final Contact contacts = contactListFiltered.get(position);
        holder.mName.setText(contacts.getUsername());
        holder.mSecName.setText("@ contact alias");

        Glide.with(context).load(contacts.getProfile_picture()).into(holder.mProfile);

        //if contact is selected
        holder.mSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wazapayUsersRecyclerInterface.onContactSelected(contacts, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mProfile;
        public TextView mName;
        public TextView mSecName;
        public CheckBox mSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfile = itemView.findViewById(R.id.profile_image);
            mName = itemView.findViewById(R.id.name);
            mSecName = itemView.findViewById(R.id.sec_name);
            mSelect = itemView.findViewById(R.id.select);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = wazapayContactsList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : wazapayContactsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getUsername().toLowerCase().contains(charString.toLowerCase()) || row.getNumber().contains(charSequence) || row.getEmail().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
