package com.example.taller2app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ContactsAdapter extends CursorAdapter {

    private static final int CONTACT_ID_INDEX = 0;
    private static final int DISPLAY_NAME_INDEX = 1;

    public ContactsAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context)
        .inflate(R.layout.contact_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView cntId = view.findViewById(R.id.cntId);
        TextView cntNm = view.findViewById(R.id.cntNm);
        int idnum = cursor.getInt(CONTACT_ID_INDEX);
        String name = cursor.getString(DISPLAY_NAME_INDEX);
        cntId.setText(Integer.toString(idnum));
        cntNm.setText(name);
    }
}
