package com.geraudluku.contactsapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.geraudluku.contactsapp.Adapters.ContactSpinnerAdapter;
import com.geraudluku.contactsapp.Adapters.ShareContactsRecyclerAdapter;
import com.geraudluku.contactsapp.Interfaces.ShareContactsRecyclerInterface;
import com.geraudluku.contactsapp.Interfaces.SharedContactsImportContactsInterface;
import com.geraudluku.contactsapp.Models.Contact;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class ShareContactsFragment extends Fragment implements ShareContactsRecyclerInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String mFilterCategory;

    private LinearLayout mSearchView;
    private CheckBox mSelectAllBox;
    private EditText mSearchBar;

    private ArrayList<Contact> mContactsList = new ArrayList<>();
    private ShareContactsRecyclerAdapter mShareContactsRecyclerAdapter;

    private Contact mSelectedContact;
    private ArrayList<Contact> mSelectedContactToShare = new ArrayList<>();
    private InputMethodManager mInputMethodManager;


    public ShareContactsFragment() {
        // Required empty public constructor
    }


    public static ShareContactsFragment newInstance(String param1, String param2) {
        ShareContactsFragment fragment = new ShareContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_share_contacts, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //search view toggle
        mSearchView = view.findViewById(R.id.search);

        //init search view items
        mSearchBar = view.findViewById(R.id.search_bar);
        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    // code to hide the soft keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchBar.getApplicationWindowToken(), 0);
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
                //filter here
                mShareContactsRecyclerAdapter.getFilter().filter(s.toString());
            }
        });

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

        //recycler view
        RecyclerView shareContactsRecyclerView = view.findViewById(R.id.share_contacts);

        //contacts recycler view settings
        mShareContactsRecyclerAdapter = new ShareContactsRecyclerAdapter(view.getContext(), mContactsList, this);
        shareContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shareContactsRecyclerView.setAdapter(mShareContactsRecyclerAdapter);


        //load dummy data
        prepareMovieData();


        //INIT contact select spinner
        //spinner
        Spinner spinner = view.findViewById(R.id.spinner2);
        //initialize spinner array adapter
        ContactSpinnerAdapter spinnerAdapter = new ContactSpinnerAdapter(view.getContext(), new ArrayList<>(mContactsList));
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

        //select all checkbox
        mSelectAllBox = view.findViewById(R.id.checkBox);
        mSelectAllBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //select all items in recyclerview
                    mShareContactsRecyclerAdapter.selectAll();
                    //get contacts from adapter
                    mShareContactsRecyclerAdapter.getSelectedContacts();
                } else {
                    //unselect all items in recycler view
                    mShareContactsRecyclerAdapter.unselectall();
                    //remove all elements from selected contacts array list
                    mSelectedContactToShare.clear();
                }
            }
        });

        //share button
        Button shareContactsButton = view.findViewById(R.id.import_contacts);
        shareContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedContactToShare.size() > 0) {
                    if (mSelectedContact == null)
                        Toasty.info(v.getContext(), "To whom are we sending them to?").show();
                    // Continue with delete operation
                    Toasty.success(v.getContext(), mSelectedContactToShare.size() + " Contacts successfully shared with - " + mSelectedContact.getUsername()).show();
                    //don't forget to clear selected contacts list
                    mSelectedContactToShare.clear();
                    //un select checkbox
                    mSelectAllBox.setChecked(false);
                } else {
                    Toasty.info(v.getContext(), "No Contact(s) Selected for sharing").show();
                }
            }
        });


        return view;
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

    private void requestSearchBarFocus() {
        mSearchBar.setFocusableInTouchMode(true);
        mSearchBar.setFocusable(true);
        mSearchBar.requestFocus();
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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

        mShareContactsRecyclerAdapter.notifyDataSetChanged();
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

    @Override
    public void onContactSelected(Contact contact, boolean isChecked) {
        //you can only add something that doesn't exist or remove if it exists
        if (isChecked) {
            mSelectedContactToShare.add(contact);
        } else {
            mSelectedContactToShare.remove(contact);
        }
    }

    @Override
    public void getContacts(ArrayList<Contact> contactArrayList) {
        mSelectedContactToShare.addAll(contactArrayList);
    }
}
