package com.geraudluku.contactsapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import es.dmoral.toasty.Toasty;


public class ExportContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mExportButton;
    private ProgressBar mProgressBar;

    private static int progress;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public ExportContactsFragment() {
        // Required empty public constructor
    }

    public static ExportContactsFragment newInstance(String param1, String param2) {
        ExportContactsFragment fragment = new ExportContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_export_contacts, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mExportButton = view.findViewById(R.id.export);
        mExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show progressBar and run thread also disable export button
                showProgressState();
            }
        });

        mProgressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    private void showProgressState() {

        mProgressBar.setVisibility(View.VISIBLE);
        mExportButton.setVisibility(View.GONE);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 200) {
                    progressStatus = doSomeWork();
                    handler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    public void run() {
                        // ---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
                        mExportButton.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);

                        //sucessful message and navigate out
                        Toasty.success(getContext(), "Export successful").show();
//                        Navigation.findNavController(getView()).popBackStack();
                    }
                });
            }

            private int doSomeWork() {
                try {
                    // ---simulate doing some work---
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++progress; //increments of 1
            }
        }).start();
    }


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
