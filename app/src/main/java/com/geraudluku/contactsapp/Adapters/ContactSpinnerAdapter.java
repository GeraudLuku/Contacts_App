package com.geraudluku.contactsapp.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.R;

import java.util.ArrayList;

public class ContactSpinnerAdapter extends ArrayAdapter<Contact> {
    public ContactSpinnerAdapter(@NonNull Context context, ArrayList<Contact> contactArrayList) {
        super(context, 0, contactArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);

        Contact contact = getItem(position);

        if (contact != null) {
            name.setText(contact.getUsername());
        }

        return convertView;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
