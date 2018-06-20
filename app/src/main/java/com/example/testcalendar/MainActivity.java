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
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
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

    public static final String[] projection = new String[]{
            Calendars._ID,
            Calendars.NAME,
            Calendars.ACCOUNT_NAME,
            Calendars.ACCOUNT_TYPE
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

            /*ContentResolver cr = getContentResolver();
            Uri uri = Calendars.CONTENT_URI;
            Log.d("content Uri", uri.toString());
            String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[]{"saezzt@live.it", "it.live",
                    "saezzt@live.it"};
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);*/

            cur = getContentResolver().query(Calendars.CONTENT_URI,
                    projection,
                    Calendars.VISIBLE + " = 1",
                    null,
                    Calendars._ID + " ASC");

            int num = cur.getCount(); //cur.getColumnCount();
            if (num == 0) {
                Toast.makeText(this, "Query vuota", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "colonne non vuote: " + cur.getColumnCount(), Toast.LENGTH_SHORT).show();
            Log.d("valori in cur", num + "");
            // Use the cursor to step through the returned records
            cur.moveToFirst();
            Log.d("cursor", cur.toString());
            for (int i = 0; i < num; i++) {
                Toast.makeText(this, "SONO nel For", Toast.LENGTH_SHORT).show();
                Log.d("SONO NEL FOR", "sono dentro prova " + i);
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                Log.d("SONO NEL FOR - CAL ID ", "cal id : " + calID);
                Log.d("SONO NEL FOR - dispname", displayName);
                Log.d("SONO NEL FOR - accName", accountName);
                Log.d("SONO NEL FOR - ownName ", ownerName);

                //ora provo a fare delle query nel calendario


                cur.moveToNext();

            }
            query();
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

    public void query() {
        String[] projection = {CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events._ID,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.RDATE,
                CalendarContract.Events.EVENT_LOCATION}; //TODO: impo gestisci la location per traffico e meteo
        // Get a Cursor over the Events Provider.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Long now = new Date().getTime();
        String whereSelection = CalendarContract.Events.DTSTART + ">?";
        String[] whereSelectionArgs = new String[] {"1526205600000"};
        //String whereSelection = CalendarContract.Events.RRULE + "!=?";
        //String[] whereSelectionArgs = new String[] {""};
        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, projection, whereSelection, whereSelectionArgs,
                null);
        // Get the index of the columns.
        int idCal = cursor.getColumnIndexOrThrow(CalendarContract.Events.CALENDAR_ID);
        int nameIdx = cursor
                .getColumnIndexOrThrow(CalendarContract.Events.TITLE);
        int idIdx = cursor.getColumnIndexOrThrow(CalendarContract.Events._ID);
        int idDtStart = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART);
        int idDtEnd = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND);
        int idRRule = cursor.getColumnIndexOrThrow(CalendarContract.Events.RRULE);
        int idRDate = cursor.getColumnIndexOrThrow(CalendarContract.Events.RDATE);
        // Initialize the result set.
        String[] result = new String[cursor.getCount()];
        // Iterate over the result Cursor.
        while (cursor.moveToNext()) {
            // Extract the name.
            String name = cursor.getString(nameIdx);
            // Extract the unique ID.
            String id = cursor.getString(idIdx);
            Long dtStart = cursor.getLong(idDtStart);
            Long dtEnd = cursor.getLong(idDtEnd);
            String rrule = cursor.getString(idRRule);
            String rdate = cursor.getString(idRDate);
            String calId = cursor.getString(idCal);
            result[cursor.getPosition()] = name + "(" + id + ")";
            Toast.makeText(this, name + "(" + id + ")", Toast.LENGTH_SHORT)
                    .show();
            Log.d("EVENTS QUERY", calId + " " + id + " " + name + " " + dtStart
                + " " + dtEnd + " " + rrule + " " + rdate);
        }

        // Close the Cursor.
        cursor.close();

        // Insert Event
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, now + (1000 * 60 * 60 * 9));
        values.put(CalendarContract.Events.DTEND, now + (1000 * 60 * 60 * 10));
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, "Walk The Dog");
        values.put(CalendarContract.Events.DESCRIPTION, "My dog is bored, so we're going on a really long walk!");
        values.put(CalendarContract.Events.CALENDAR_ID, 1); //questo dipende dal calendario sul quale voglio inserirlo!
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        // Retrieve ID for new event
        String eventID = uri.getLastPathSegment();



        //recupero l'id dell'evento che ho inserito
        Long changeId ;
        whereSelection = CalendarContract.Events.DTSTART + "=?";
        whereSelectionArgs = new String[] {Long.toString(now + (1000 * 60 * 60 * 9))};
        //String whereSelection = CalendarContract.Events.RRULE + "!=?";
        //String[] whereSelectionArgs = new String[] {""};
        cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, projection, whereSelection, whereSelectionArgs,
                null);

        cursor.moveToFirst();
        changeId = cursor.getLong(idIdx);
        Log.d("identificativo", changeId.toString());

        // ora provo a modificarlo! -> ci cambio il titolo
        ContentValues val = new ContentValues();
        val.put(CalendarContract.Events.TITLE, "Some new title");
        val.put(CalendarContract.Events.EVENT_LOCATION, "A new location");
        String[] selArgs =
                new String[]{changeId.toString()};
        int updated =
                getContentResolver().
                        update(
                                CalendarContract.Events.CONTENT_URI,
                                val,
                                CalendarContract.Events._ID + " =? ",
                                selArgs);


        String[] projectionAlert = new String[] {
                CalendarContract.CalendarAlerts._ID,
                CalendarContract.CalendarAlerts.ALARM_TIME,
                CalendarContract.CalendarAlerts.DTSTART,
                CalendarContract.CalendarAlerts.HAS_ALARM
        };

        //ora provo a vedere i reminder/ allarmi ecc..
        Cursor alertCursor = getContentResolver().query(
                CalendarContract.CalendarAlerts.CONTENT_URI, projectionAlert, null, null,
                null);

        idIdx = alertCursor.getColumnIndexOrThrow(CalendarContract.CalendarAlerts._ID);
        int alrmTimeId = alertCursor.getColumnIndexOrThrow(CalendarContract.CalendarAlerts.ALARM_TIME);
        int alrmDtStartId = alertCursor.getColumnIndexOrThrow(CalendarContract.CalendarAlerts.DTSTART);
        int hasAlarmId = alertCursor.getColumnIndexOrThrow(CalendarContract.CalendarAlerts.HAS_ALARM);

        while(alertCursor.moveToNext()){
            Log.d("Alert" , alertCursor.getLong(idIdx)
                + " " + alertCursor.getLong(alrmDtStartId) + " " + alertCursor.getInt(hasAlarmId));
        }

        String[] projectionReminder = new String[] {
                CalendarContract.Reminders._ID,
                CalendarContract.Reminders.EVENT_ID,
                CalendarContract.Reminders.MINUTES
        };
        Cursor reminderCursor = getContentResolver().query(
                CalendarContract.Reminders.CONTENT_URI, projectionReminder, null, null,
                null);

        idIdx = reminderCursor.getColumnIndexOrThrow(CalendarContract.Reminders._ID);
        //int descriptioReminder = alertCursor.getColumnIndexOrThrow(CalendarContract.Reminders.DESCRIPTION);
        //int titleReminder = alertCursor.getColumnIndexOrThrow(CalendarContract.Reminders.TITLE);
        //int dtStratReminder = reminderCursor.getColumnIndexOrThrow(CalendarContract.Reminders.DTSTART);
        int eventIdReminder = reminderCursor.getColumnIndexOrThrow(CalendarContract.Reminders.EVENT_ID);
        int timeBeforEventReminder = reminderCursor.getColumnIndexOrThrow(CalendarContract.Reminders.MINUTES);

        while (reminderCursor.moveToNext()){
            Log.d("REMINDERS", reminderCursor.getLong(idIdx) +  " " + reminderCursor.getLong(eventIdReminder) + " "
                    + reminderCursor.getLong(timeBeforEventReminder));
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
