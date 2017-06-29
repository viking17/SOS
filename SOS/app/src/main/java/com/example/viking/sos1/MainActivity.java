package com.example.viking.sos1;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
//---------------------------------

    final String TAG = MainActivity.class.getSimpleName();
    final int REQUEST_CODE_PICK_CONTACTS = 1;
    Uri uriContact;
    String contactID;

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


       // contacts unique ID

   public void onClickSelectContact(View btnSelectContact) {

       boolean b = isNetworkAvailable();
       LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
       boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

       if(b == false)
       {
           Toast.makeText(this, "No Internet, Connect to internet first", Toast.LENGTH_LONG).show();
       }
       if(statusOfGPS == false)
       {
           Toast.makeText(this, "GPS is diabled, enable gps.", Toast.LENGTH_LONG).show();
       }

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
       if(b == true && statusOfGPS == true)
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();


            retrieveContactNumber();



        }
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();




        //Intent displayActivityIntent = new Intent(this, MapsActivity.class);

        Intent displayActivityIntent = new Intent(this, MapsActivity.class);
        displayActivityIntent.putExtra("MESSAGE_KEY",contactNumber);
        startActivity(displayActivityIntent);



    }
//-----------------------------------------
public void openPolice(View view)
{
    boolean b = isNetworkAvailable();


    if(b == false)
    {
        Toast.makeText(this, "No Internet, Connect to internet first", Toast.LENGTH_LONG).show();
    }
    else {
        String urlString = "https://www.google.co.in/search?q=nearest+police+station&oq=nearest+police+&aqs=chrome.0.0j69i57j69i60j0l3.4742j0j7&sourceid=chrome&ie=UTF-8";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed and open Kindle Browser
            intent.setPackage("com.amazon.cloud9");
            startActivity(intent);
        }
    }
}

    public void openCall(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }
}



