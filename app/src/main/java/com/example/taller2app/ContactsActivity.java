package com.example.taller2app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ContactsActivity extends AppCompatActivity {

    private ListView lstCnts;
    private String[] mProjection;
    private Cursor mCursor;
    private ContactsAdapter mContactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        lstCnts = findViewById(R.id.lstVw);

        requestPermission();


    }

    private void requestPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(getApplicationContext(), "Todos los permisos fueron brindados!", Toast.LENGTH_SHORT).show();
                        prepareToLoad();
                        loadContactList();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(), "Faltan Permisos. Funcionalidad Limitada!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Toast.makeText(getApplicationContext(), "Se necesita para acceder a los contactos!", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void loadContactList() {
        mCursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,mProjection,null,null,null);
        mContactsAdapter.changeCursor(mCursor);
    }

    private void prepareToLoad() {
        mProjection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
        };

        mContactsAdapter = new ContactsAdapter(this, null, 0);
        lstCnts.setAdapter(mContactsAdapter);

        mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null);
        mContactsAdapter.changeCursor(mCursor);

    }
}
