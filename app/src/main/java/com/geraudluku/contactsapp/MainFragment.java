package com.geraudluku.contactsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.geraudluku.contactsapp.Adapters.ContactsRecyclerAdapter;
import com.geraudluku.contactsapp.Adapters.FavouriteContactsRecyclerAdapter;
import com.geraudluku.contactsapp.Interfaces.FavoriteContactsRecyclerInterface;
import com.geraudluku.contactsapp.Interfaces.ContactsRecyclerInterface;
import com.geraudluku.contactsapp.Models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainFragment extends Fragment implements FavoriteContactsRecyclerInterface, ContactsRecyclerInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Contact> mContactsList = new ArrayList<>();
    private RecyclerView mContactsRecyclerView;
    private ContactsRecyclerAdapter mContactsRecyclerAdapter;

    private ArrayList<Contact> mFavouriteContactsList = new ArrayList<>();
    private RecyclerView mFavouriteContactsRecyclerView;
    private FavouriteContactsRecyclerAdapter mFavouriteContactsRecyclerAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        mContactsRecyclerView = view.findViewById(R.id.contacts);
        mFavouriteContactsRecyclerView = view.findViewById(R.id.favorite_contacts);

        //favourite contacts recycler adapter settings
        mFavouriteContactsRecyclerAdapter = new FavouriteContactsRecyclerAdapter(view.getContext(), mFavouriteContactsList, this);
        mFavouriteContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFavouriteContactsRecyclerView.setAdapter(mFavouriteContactsRecyclerAdapter);

        //contacts recycler view settings
        mContactsRecyclerAdapter = new ContactsRecyclerAdapter(view.getContext(), mContactsList, this);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactsRecyclerView.setAdapter(mContactsRecyclerAdapter);


        //get floating action button
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(mOnclickListener);

        //create contacts layout
        LinearLayout createContact = view.findViewById(R.id.create_contact);
        createContact.setOnClickListener(mOnclickListener);

        //load dummy data
        if (mContactsList.isEmpty())
            loadContacts();

        return view;
    }

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floatingActionButton:
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addContactsFragment);
                    break;
                case R.id.create_contact:
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addContactsFragment);
                    break;
            }
        }
    };

    private void loadContacts() {

        //create dummy favourite contacts
        mFavouriteContactsList.add(new Contact("Fon Ndikum", "Ndikum@bassnectar.com", "223444000888", "https://picsum.photos/id/237/400"));
        mFavouriteContactsList.add(new Contact("Godlove Fonzenyuy", "Fonzenyuy@gmail.com", "223444000888", "https://picsum.photos/id/1001/400"));

        //create dummy normal contacts
        mContactsList.add(new Contact("Geraud", "lukugeraud@yahoo.com", "237674753811", "https://picsum.photos/id/1002/400"));
        mContactsList.add(new Contact("Frankie", "frankieflash@yahoo.com", "237677333222", "https://picsum.photos/id/1003/400"));
        mContactsList.add(new Contact("Esther", "Estherfree@gmail.com", "222666444111", "https://picsum.photos/id/1040/400"));
        mContactsList.add(new Contact("Augustine", "flowersbloom@outlook.com", "223444000888", "https://picsum.photos/id/1054/400"));
        mContactsList.add(new Contact("Janiver", "Janiver@outlook.com", "223444000888", "https://picsum.photos/id/1066/400"));
        mContactsList.add(new Contact("Nganju Christopher", "Christopher@gmail.com", "223444000888", "https://picsum.photos/id/10/400"));
        mContactsList.add(new Contact("Nkaime Lovelyne", "Lovelyne@gmail.com", "223444000888", "https://picsum.photos/id/11/400"));

        mContactsRecyclerAdapter.notifyDataSetChanged();
        mFavouriteContactsRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFavouriteContactCheckListener(final Contact contact, final int position) {

        android.os.Handler mHandler = getActivity().getWindow().getDecorView().getHandler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //put in a runnable to bypass Cannot call this method while RecyclerView is computing a layout or scrolling androidx.recyclerview.widget.RecyclerView
                mContactsRecyclerAdapter.addItemAt(contact, mContactsRecyclerAdapter.getItemCount());
            }
        });
    }

    @Override
    public void onContactCheckListener(final Contact contact, final int position) {

        android.os.Handler mHandler = getActivity().getWindow().getDecorView().getHandler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //put in a runnable to bypass Cannot call this method while RecyclerView is computing a layout or scrolling androidx.recyclerview.widget.RecyclerView
                mFavouriteContactsRecyclerAdapter.addItemAt(contact, mFavouriteContactsRecyclerAdapter.getItemCount());
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                //navigate to settings fragment
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_contactsSettingsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
