package com.geraudluku.contactsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.geraudluku.contactsapp.Adapters.ContactSpinnerAdapter;
import com.geraudluku.contactsapp.Models.Contact;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class ReportContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mReportContactsBtn;
    private EditText mReportEditText;

    private ArrayList<Contact> mContactsList = new ArrayList<>();

    private Contact mSelectedContact;

    public ReportContactsFragment() {
        // Required empty public constructor
    }

    public static ReportContactsFragment newInstance(String param1, String param2) {
        ReportContactsFragment fragment = new ReportContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_report_contacts, container, false);

        //setup toolbar
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        //spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        //initialize spinner array adapter
        ContactSpinnerAdapter spinnerAdapter = new ContactSpinnerAdapter(view.getContext(), mContactsList);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedContact = (Contact) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // report edittext
        mReportEditText = view.findViewById(R.id.reporting_reason);

        //report contact button
        mReportContactsBtn = view.findViewById(R.id.report);
        mReportContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///if a contact is selected
                if (mSelectedContact != null)
                    if (!mReportEditText.getText().toString().matches("")) { //if there is a reporting reason message
                        new AlertDialog.Builder(v.getContext())
                                .setMessage(String.format("Are you sure you want to Report %s ?", mSelectedContact.getUsername()))
                                .setPositiveButton("REPORT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        Toasty.success(getContext(), mSelectedContact.getUsername() + " Successfully reported", Toasty.LENGTH_SHORT).show();
                                        //send user back to settings page
                                        Navigation.findNavController(getView()).popBackStack();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    } else
                        mReportEditText.setError("Kindly enter a reason");
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
