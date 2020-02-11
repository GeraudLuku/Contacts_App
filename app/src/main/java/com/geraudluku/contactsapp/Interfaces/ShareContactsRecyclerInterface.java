package com.geraudluku.contactsapp.Interfaces;

import com.geraudluku.contactsapp.Models.Contact;

import java.util.ArrayList;

public interface ShareContactsRecyclerInterface {
    void onContactSelected(Contact contact, boolean isChecked);
    void getContacts(ArrayList<Contact> contactArrayList);
}
