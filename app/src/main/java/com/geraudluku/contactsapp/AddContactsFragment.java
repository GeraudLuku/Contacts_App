package com.geraudluku.contactsapp;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.geraudluku.contactsapp.Models.Contact;
import com.geraudluku.contactsapp.ViewModels.AddContactsSharedViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager mViewPager;
    private EditText mSearchBar;
    private TextView mContactCountDisplay;

    private LinearLayout mSearchView;
    private InputMethodManager mInputMethodManager;

    private String mFilterCategory;

    private AddContactsSharedViewModel mAddContactsSharedViewModel;

    private ArrayList<Contact> mSelectedContacts = new ArrayList<>();

    public AddContactsFragment() {
        // Required empty public constructor
    }

    public static AddContactsFragment newInstance(String param1, String param2) {
        AddContactsFragment fragment = new AddContactsFragment();
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


    //used to initiate the view model
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddContactsSharedViewModel = new ViewModelProvider(getActivity()).get(AddContactsSharedViewModel.class);
        //observe selected contacts object
        mAddContactsSharedViewModel.getSelectedContacts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contactArrayList) {
                //get live selected contacts
                if (contactArrayList.size() > 0) {
                    mSelectedContacts = contactArrayList;
                    //also update text view
                    mContactCountDisplay.setText("Add " + mSelectedContacts.get(0).getUsername() + " and " + (mSelectedContacts.size() - 1) + " others to contacts");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contacts, container, false);
        //init toolbar
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //search view toggle
        mSearchView = view.findViewById(R.id.linear_layout);
        mContactCountDisplay = view.findViewById(R.id.textView3);
        mViewPager = view.findViewById(R.id.viewPager);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //init search view items
        mSearchBar = view.findViewById(R.id.search_bar);
        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    // code to hide the soft keyboard
                    mInputMethodManager.hideSoftInputFromWindow(mSearchBar.getApplicationWindowToken(), 0);
                    return true;
                } else {
                    return false;
                }
            }
        });

        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //send search text to child fragment
                mAddContactsSharedViewModel.setSearchKey(s.toString());
            }
        });

        //force focus on search edit text
//        requestSearchBarFocus();

        Spinner filterOptions = view.findViewById(R.id.filter_spinner);

        filterOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if its first option do nothing
                if (position == 0) {
                    //might want to clear search bar , reload original spinner  , reseat search bar hint
                    disableEditText(mSearchBar, false);
                    mSearchBar.setText("");
                    mSearchBar.setHint("Search by Number, Username or Email");
                } else {
                    disableEditText(mSearchBar, true);
                    requestSearchBarFocus();
                    mFilterCategory = (String) parent.getItemAtPosition(position);
                    mSearchBar.setHint("Search By - " + mFilterCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set tab layout
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.parseColor("#797979"), Color.parseColor("#333333"));

        mViewPager.setAdapter(new AddContactPageAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //add to contacts button
        Button addToContacts = view.findViewById(R.id.button5);
        addToContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedContacts.size() > 0) {
                    //move out of fragment, display success toast message
                    Toasty.success(v.getContext(), " Successfully added " + mSelectedContacts.get(0).getUsername() + " and " + (mSelectedContacts.size() - 1) + " others to contacts").show();
                    Navigation.findNavController(getView()).popBackStack();
                    //clear selected contacts
                    mSelectedContacts.clear();
                    mContactCountDisplay.setText("");
                } else
                    Toasty.info(getContext(), "select contact(s)").show();
            }
        });

        return view;
    }

    private void requestSearchBarFocus() {
        mSearchBar.setFocusableInTouchMode(true);
        mSearchBar.setFocusable(true);
        mSearchBar.requestFocus();
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void disableEditText(EditText editText, boolean enable) {
        if (!enable) {
            editText.setFocusable(false);
            editText.setEnabled(false);
            editText.setCursorVisible(false);
            editText.setBackgroundColor(Color.TRANSPARENT);
        } else {
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.setEnabled(true);
            editText.setCursorVisible(true);
        }
    }

    //for view page adapter
    public class AddContactPageAdapter extends FragmentPagerAdapter {

        public AddContactPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WazapayUsersFragment();
                case 1:
                    //return new ContactsFragment();  for developement usage
                    return new WazapayUsersFragment();  //for testing usage
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "WaZaPAY Users";
                case 1:
                    return "Contacts";
                default:
                    return "Object";
            }
        }


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
                if (!mSelectedContacts.isEmpty())
                    mSelectedContacts.clear();
                mContactCountDisplay.setText("");
                return true;
            case R.id.action_favorite:
                //show search view if its not visible else
                if (mSearchView.getVisibility() == View.VISIBLE)
                    mSearchView.setVisibility(View.GONE);
                else
                    mSearchView.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
