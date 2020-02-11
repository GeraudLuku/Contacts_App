package com.geraudluku.contactsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geraudluku.contactsapp.Interfaces.ContactsRecyclerInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contact> contactsList;
    private ContactsRecyclerInterface contactsRecyclerInterface;

    public ContactsRecyclerAdapter(Context context, ArrayList<Contact> contactsList, ContactsRecyclerInterface contactsRecyclerInterface) {
        this.context = context;
        this.contactsList = contactsList;
        this.contactsRecyclerInterface = contactsRecyclerInterface;
    }


    @NonNull
    @Override
    public ContactsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRecyclerAdapter.MyViewHolder holder, final int position) {

        final Contact contact = contactsList.get(position);
        holder.mName.setText(contact.getUsername());
        holder.mCheckBox.setChecked(false);

        Glide.with(context).load(contact.getProfile_picture()).into(holder.mProfile);

        //set onCLick from checkbox
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                removeAt(position);
                contactsRecyclerInterface.onContactCheckListener(contact, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox mCheckBox;
        public ImageView mProfile;
        public TextView mName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.star);
            mProfile = itemView.findViewById(R.id.profile_image);
            mName = itemView.findViewById(R.id.contact_name);

        }
    }

    public void removeAt(int position) {
        contactsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contactsList.size());
    }

    public void addItemAt(Contact dataObj, int index) {
        contactsList.add(dataObj);
        notifyItemInserted(index);
    }
}
