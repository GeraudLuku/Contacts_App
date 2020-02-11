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
import com.geraudluku.contactsapp.Interfaces.SharedContactsImportContactsInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SharedContactsRecyclerAdapter extends RecyclerView.Adapter<SharedContactsRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contact> sharedContactsList;
    private SharedContactsImportContactsInterface sharedContactsImportContactsInterface;

    private boolean isSelectedAll;
    private int adapterPosition;

    public SharedContactsRecyclerAdapter(Context context, ArrayList<Contact> sharedContactsList, SharedContactsImportContactsInterface sharedContactsImportContactsInterface) {
        this.context = context;
        this.sharedContactsList = sharedContactsList;
        this.sharedContactsImportContactsInterface = sharedContactsImportContactsInterface;
    }


    @NonNull
    @Override
    public SharedContactsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_contact_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedContactsRecyclerAdapter.MyViewHolder holder, int position) {

        final Contact contact = sharedContactsList.get(position);
        adapterPosition = position;
        holder.mName.setText(contact.getUsername());

        Glide.with(context).load(contact.getProfile_picture()).into(holder.mProfile);

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
        return sharedContactsList.size();
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

    public void remove(ArrayList<Contact> contacts) {
        for (Contact contact : contacts) {
            sharedContactsList.remove(contact);
            notifyItemRemoved(sharedContactsList.indexOf(contact));
            notifyItemRangeChanged(sharedContactsList.indexOf(contact), sharedContactsList.size());
        }
    }

    //checkbox onSetCheck listener
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // pass contact object and boolean state
            sharedContactsImportContactsInterface.onContactSelected(sharedContactsList.get(adapterPosition), isChecked);
        }
    };

}
