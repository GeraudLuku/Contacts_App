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
import com.geraudluku.contactsapp.Interfaces.ShareContactsRecyclerInterface;
import com.geraudluku.contactsapp.Interfaces.SharedContactsImportContactsInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShareContactsRecyclerAdapter extends RecyclerView.Adapter<ShareContactsRecyclerAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Contact> shareContactsList;
    private ShareContactsRecyclerInterface shareContactsRecyclerInterface;

    private boolean isSelectedAll;
    private int adapterPosition;
    private List<Contact> contactListFiltered;

    public ShareContactsRecyclerAdapter(Context context, ArrayList<Contact> shareContactsList, ShareContactsRecyclerInterface shareContactsRecyclerInterfacee) {
        this.context = context;
        this.shareContactsList = shareContactsList;
        this.contactListFiltered = shareContactsList;
        this.shareContactsRecyclerInterface = shareContactsRecyclerInterfacee;
    }

    @NonNull
    @Override
    public ShareContactsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_contact_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareContactsRecyclerAdapter.MyViewHolder holder, int position) {

        Contact contacts = contactListFiltered.get(position);
        adapterPosition = position;
        holder.mName.setText(contacts.getUsername());

        Glide.with(context).load(contacts.getProfile_picture()).into(holder.mProfile);

        //if selected all box is pressed
        if (!isSelectedAll) {
            holder.mCheckBox.setOnCheckedChangeListener(null);
            holder.mCheckBox.setChecked(false);
        } else {
            holder.mCheckBox.setOnCheckedChangeListener(null);
            holder.mCheckBox.setChecked(true);
        }

        holder.mCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox mCheckBox;
        public ImageView mProfile;
        public TextView mName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.star);
            mProfile = itemView.findViewById(R.id.profile_image);
            mName = itemView.findViewById(R.id.name);
        }
    }

    public void selectAll() {
        isSelectedAll = true;
        notifyDataSetChanged();
    }

    public void unselectall() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }

    public void getSelectedContacts() {
        shareContactsRecyclerInterface.getContacts(new ArrayList<>(contactListFiltered));
    }

    //checkbox onSetCheck listener
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // pass contact object and boolean state
            shareContactsRecyclerInterface.onContactSelected(contactListFiltered.get(adapterPosition), isChecked);
        }
    };


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = shareContactsList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : shareContactsList) {

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
