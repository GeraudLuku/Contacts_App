package com.geraudluku.contactsapp;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geraudluku.contactsapp.Adapters.WazapayUserContactsRecyclerAdapter;
import com.geraudluku.contactsapp.Interfaces.WazapayUsersRecyclerInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.ViewModels.AddContactsSharedViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WazapayUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WazapayUsersFragment extends Fragment implements WazapayUsersRecyclerInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Contact> mContactsList = new ArrayList<>();
    private RecyclerView mWazapayContactsRecyclerView;
    private WazapayUserContactsRecyclerAdapter mWazapayContactsRecyclerAdapter;

    private AddContactsSharedViewModel mAddContactsSharedViewModel;

    //used to initiate the view model
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddContactsSharedViewModel = new ViewModelProvider(getActivity()).get(AddContactsSharedViewModel.class);
        //observe selected contacts object
        mAddContactsSharedViewModel.getSearchKey().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //get search string
                mWazapayContactsRecyclerAdapter.getFilter().filter(s);
            }
        });
    }

    public WazapayUsersFragment() {
        // Required empty public constructor
    }

    public static WazapayUsersFragment newInstance(String param1, String param2) {
        WazapayUsersFragment fragment = new WazapayUsersFragment();
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
        View view = inflater.inflate(R.layout.fragment_wazapay_users, container, false);

        //recycler view
        mWazapayContactsRecyclerView = view.findViewById(R.id.wazapay_users);

        //contacts recycler view settings
        mWazapayContactsRecyclerAdapter = new WazapayUserContactsRecyclerAdapter(view.getContext(), mContactsList, this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        mWazapayContactsRecyclerView.setLayoutManager(mLayoutManager2);
        mWazapayContactsRecyclerView.setAdapter(mWazapayContactsRecyclerAdapter);

        //load dummy data
        prepareMovieData();


        return view;
    }

    private void prepareMovieData() {
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

        mWazapayContactsRecyclerAdapter.notifyDataSetChanged();
    }


    private ArrayList<Contact> mSelectedContacts = new ArrayList<>();

    @Override
    public void onContactSelected(Contact contact, boolean isChecked) {
        if (isChecked) {
            mSelectedContacts.add(contact);
        } else {
            mSelectedContacts.remove(contact);
        }

        //send it now to parent fragment
        mAddContactsSharedViewModel.setSelectedContacts(mSelectedContacts);
    }


}
