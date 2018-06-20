package com.example.testcalendar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Permission is not granted
            // Should we show an explanation? READ_CALENDAR
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CALENDAR is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Toast.makeText(this, "permesso CONCESSO! (normal flow)", Toast.LENGTH_SHORT).show();
            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = Calendars.CONTENT_URI;
            String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[]{"saezzt@live.it", "it.live",
                    "saezzt@live.it"};
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

            if(cur.getColumnCount()==0){
                Toast.makeText(this, "Query vuota", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "colonne non vuote", Toast.LENGTH_SHORT).show();
            // Use the cursor to step through the returned records
            while (cur.moveToNext()) {
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                // Do something with the values...
                Toast.makeText(this, "SONO nel WHILE", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // task you need to do.
                    Toast.makeText(this, "permesso CONCESSO! (onRequest)", Toast.LENGTH_SHORT).show();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "ERRORE permessi", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
