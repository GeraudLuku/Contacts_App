package com.geraudluku.contactsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import java.io.File;

import es.dmoral.toasty.Toasty;


public class FromFileImportContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mImportButton;
    private ProgressBar mProgressBar;
    private EditText mFileNameView;

    private File mFilePath;

    private static int progress;
    private int progressStatus = 0;
    private Handler handler = new Handler();


    public FromFileImportContactsFragment() {
        // Required empty public constructor
    }

    public static FromFileImportContactsFragment newInstance(String param1, String param2) {
        FromFileImportContactsFragment fragment = new FromFileImportContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_from_file_import_contacts, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mFileNameView = view.findViewById(R.id.filename);

        mImportButton = view.findViewById(R.id.btn_upload);
        mImportButton.setOnClickListener(mOnClickListener);

        ImageButton mChooseFileButton = view.findViewById(R.id.choose_button);
        mChooseFileButton.setOnClickListener(mOnClickListener);

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_upload:
                    //show progressBar and run thread also disable export button
                    showProgressState();
                    break;
                case R.id.choose_button:
                    //open file manager to choose file
                    //first check for permission
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
                        if (!checkIfAlreadyhavePermission()) {
                            requestForSpecificPermission();
                        } else {
                            chooseFile();
                        }
                    break;
            }
        }
    };

    //open choose file dialog
    private void chooseFile() {

        DialogProperties properties = new DialogProperties();

        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        properties.show_hidden_files = false;

        FilePickerDialog filePickerDialog = new FilePickerDialog(getContext(), properties);
        filePickerDialog.setTitle("Select a File");
        filePickerDialog.show();

        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length == 1) {
                    //get filename
                    mFilePath = new File(files[0]);
                    mFileNameView.setText(mFilePath.getName());
                }
            }
        });
    }

    private boolean checkIfAlreadyhavePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private final int REQUEST_CODE = 101;

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    private void showProgressState() {

        mProgressBar.setVisibility(View.VISIBLE);
        mImportButton.setVisibility(View.GONE);

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
                        mImportButton.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);

                        //sucessful message and navigate out
                        Toasty.success(getContext(), mFilePath.getName() + " Imported successfully").show();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    chooseFile();
                } else {
                    //not granted
                    Toasty.info(getContext(), "App Needs Permission").show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
