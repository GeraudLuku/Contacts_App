package com.geraudluku.contactsapp.Interfaces;

import com.geraudluku.contactsapp.Models.Contact;

public interface SharedContactsImportContactsInterface {
    //    void onContactSelected(Contact contact, boolean isChecked);
    void onContactSelected(Contact contact, int position, boolean isChecked);
}
