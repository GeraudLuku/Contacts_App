package com.geraudluku.contactsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.geraudluku.contactsapp.Adapters.SharedContactsRecyclerAdapter;
import com.geraudluku.contactsapp.Interfaces.SharedContactsImportContactsInterface;
import com.geraudluku.contactsapp.Models.Contact;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class SharedContactsImportContacts extends Fragment implements SharedContactsImportContactsInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CheckBox mSelectAllBox;

    private SharedContactsRecyclerAdapter mSharedContactsRecyclerAdapter;

    private ArrayList<Contact> mContactsList = new ArrayList<>();
    private ArrayList<Contact> mSelectedContacts = new ArrayList<>();

    public SharedContactsImportContacts() {
        // Required empty public constructor
    }

    public static SharedContactsImportContacts newInstance(String param1, String param2) {
        SharedContactsImportContacts fragment = new SharedContactsImportContacts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shared_contacts_import_contacts, container, false);

        //recycler view
        RecyclerView sharedContactsRecyclerView = view.findViewById(R.id.shared_contacts);

        //contacts recycler view settings
        mSharedContactsRecyclerAdapter = new SharedContactsRecyclerAdapter(view.getContext(), mContactsList, this);
        sharedContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedContactsRecyclerView.setAdapter(mSharedContactsRecyclerAdapter);

        //load dummy data
        prepareData();

        mSelectAllBox = view.findViewById(R.id.select_all);
        mSelectAllBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //select all items in recyclerview
                    mSharedContactsRecyclerAdapter.selectAll();
                    //add all items in selected contacts array list
                    mSelectedContacts = mContactsList;
                } else {
                    //unselect all items in recycler view
                    mSharedContactsRecyclerAdapter.unselectall();
                    //remove all elements from selected contacts array list
                    mSelectedContacts.clear();
                }
            }
        });

        //delete and import button
        Button deleteContactsButton = view.findViewById(R.id.delete_contacts);
        deleteContactsButton.setOnClickListener(mConCLickListener);

        Button importContactsButton = view.findViewById(R.id.import_contacts);
        importContactsButton.setOnClickListener(mConCLickListener);

        return view;
    }

    private View.OnClickListener mConCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_contacts:
                    //show warning dialog
                    if (mSelectedContacts.size() > 0) {
                        new AlertDialog.Builder(v.getContext())
                                .setMessage(String.format("Are you sure you want to delete Contacts(%d) ?", mSelectedContacts.size()))
                                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //remove contacts from recyclerview
                                        mSharedContactsRecyclerAdapter.remove(mSelectedContacts);
                                        // Continue with delete operation
                                        Toasty.success(getContext(), mSelectedContacts.size() + " Contacts successfully deleted").show();
                                        //dont forget to clear selected contacts list
                                        mSelectedContacts.clear();
                                        //unselect checkbox
                                        mSelectAllBox.setChecked(false);
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    } else {
                        Toasty.info(getContext(), " No Contact Selected").show();
                    }
                    break;
                case R.id.import_contacts:
                    if (mSelectedContacts.size() > 0) {
                        //remove contacts from recyclerview
                        mSharedContactsRecyclerAdapter.remove(mSelectedContacts);
                        // Continue with delete operation
                        Toasty.success(getContext(), mSelectedContacts.size() + " Contacts successfully imported").show();
                        //dont forget to clear selected contacts list
                        mSelectedContacts.clear();
                        //unselect checkbox
                        mSelectAllBox.setChecked(false);
                    } else {
                        Toasty.info(getContext(), "No Contact Selected").show();
                    }
                    break;
            }
        }
    };

    private void prepareData() {
        //initialize contacts array
        //create dummy favourite contacts
        mContactsList.add(new Contact("Fon Ndikum", "Ndikum@bassnectar.com", "223444000888", "https://picsum.photos/id/237/400"));
        mContactsList.add(new Contact("Godlove Fonzenyuy", "Fonzenyuy@gmail.com", "223444000888", "https://picsum.photos/id/1001/400"));
        mContactsList.add(new Contact("Geraud", "lukugeraud@yahoo.com", "237674753811", "https://picsum.photos/id/1002/400"));
        mContactsList.add(new Contact("Frankie", "frankieflash@yahoo.com", "237677333222", "https://picsum.photos/id/1003/400"));
        mContactsList.add(new Contact("Esther", "Estherfree@gmail.com", "222666444111", "https://picsum.photos/id/1040/400"));
        mContactsList.add(new Contact("Augustine", "flowersbloom@outlook.com", "223444000888", "https://picsum.photos/id/1054/400"));
        mContactsList.add(new Contact("Janiver", "Janiver@outlook.com", "223444000888", "https://picsum.photos/id/1066/400"));
        mContactsList.add(new Contact("Nganju Christopher", "Christopher@gmail.com", "223444000888", "https://picsum.photos/id/10/400"));
        mContactsList.add(new Contact("Nkaime Lovelyne", "Lovelyne@gmail.com", "223444000888", "https://picsum.photos/id/11/400"));

        mSharedContactsRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onContactSelected(Contact contact, boolean isChecked) {
        //you can only add something that doesn't exist or remove if it exists
        if (isChecked) {
            mSelectedContacts.add(contact);
        } else {
            mSelectedContacts.remove(contact);
        }
    }

}
