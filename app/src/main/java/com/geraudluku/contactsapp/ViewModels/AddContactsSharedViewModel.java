package com.geraudluku.contactsapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.geraudluku.contactsapp.Models.Contact;

import java.util.ArrayList;

public class AddContactsSharedViewModel extends ViewModel {
    private MutableLiveData<String> searchKey = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Contact>> selectedContacts = new MutableLiveData<>();

    //set and get search key
    public void setSearchKey(String key) {
        searchKey.setValue(key);
    }

    public LiveData<String> getSearchKey() {
        return searchKey;
    }

    //set and get selected contacts
    public void setSelectedContacts(ArrayList<Contact> selectedContacts) {
        this.selectedContacts.setValue(selectedContacts);
    }

    public LiveData<ArrayList<Contact>> getSelectedContacts() {
        return selectedContacts;
    }
}
