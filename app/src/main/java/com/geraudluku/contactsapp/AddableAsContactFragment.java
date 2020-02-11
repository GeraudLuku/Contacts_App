package com.geraudluku.contactsapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.concurrent.TimeUnit;


public class AddableAsContactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddableAsContactFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddableAsContactFragment newInstance(String param1, String param2) {
        AddableAsContactFragment fragment = new AddableAsContactFragment();
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
        View view = inflater.inflate(R.layout.fragment_addable_as_contact, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //info button
        ImageButton infoButton = view.findViewById(R.id.info_button);
        infoButton.setOnClickListener(mOnClickListener);

        //save button
        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(mOnClickListener);

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.info_button:
                    //show dialog to display custom message
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled " +
                                    "it to make a type specimen book. " +
                                    "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.")
                            .setIcon(R.drawable.ic_info)
                            .show();
                    break;
                case R.id.save_button:
                    Toasty.success(v.getContext(), "Contact settings saved", 1).show();
                    Navigation.findNavController(getView()).popBackStack();
                    break;
            }
        }
    };


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(getView()).popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
