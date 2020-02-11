package com.geraudluku.contactsapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


public class ContactsSettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ContactsSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsSettingsFragment newInstance(String param1, String param2) {
        ContactsSettingsFragment fragment = new ContactsSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_contacts_settings, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set onclick listener for listview
        ListView listView = view.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_addableAsContactFragment);
                        break;
                    case 1:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_blockContactsFragment);
                        break;
                    case 2:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_deleteContactsFragment);
                        break;
                    case 3:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_reportContactsFragment);
                        break;
                    case 4:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_exportContactsFragment);
                        break;
                    case 5:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_importContactsFragment);
                        break;
                    case 6:
                        Navigation.findNavController(view).navigate(R.id.action_contactsSettingsFragment_to_shareContactsFragment);
                        break;
                    default:
                        Toasty.error(getContext(), "Nothing selected").show();
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
