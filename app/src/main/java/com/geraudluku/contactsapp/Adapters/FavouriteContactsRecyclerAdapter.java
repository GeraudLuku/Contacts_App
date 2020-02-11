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
import com.geraudluku.contactsapp.Interfaces.FavoriteContactsRecyclerInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteContactsRecyclerAdapter extends RecyclerView.Adapter<FavouriteContactsRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contact> favouriteContactsList;
    private FavoriteContactsRecyclerInterface favoriteContactsRecyclerInterface;

    public FavouriteContactsRecyclerAdapter(Context context, ArrayList<Contact> favouriteContactsList, FavoriteContactsRecyclerInterface favoriteContactsRecyclerInterface) {
        this.context = context;
        this.favouriteContactsList = favouriteContactsList;
        this.favoriteContactsRecyclerInterface = favoriteContactsRecyclerInterface;
    }


    @NonNull
    @Override
    public FavouriteContactsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final Contact contact = favouriteContactsList.get(position);
        holder.mName.setText(contact.getUsername());
        holder.mCheckBox.setChecked(true);

        Glide.with(context).load(contact.getProfile_picture()).into(holder.mProfile);

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                removeAt(position);
                favoriteContactsRecyclerInterface.onFavouriteContactCheckListener(contact, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouriteContactsList.size();
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

    private void removeAt(int position) {
        favouriteContactsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favouriteContactsList.size());
    }

   public void addItemAt(Contact dataObj, int index) {
        favouriteContactsList.add(dataObj);
        notifyItemInserted(index);
    }
}
