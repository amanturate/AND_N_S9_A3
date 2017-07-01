package com.example.turate.and_n_s9_a3;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.turate.and_n_s9_a3.R.id.parent;

public class MainActivity extends AppCompatActivity {

    ListView lv1;
    LinearLayout ll1;
    ProgressDialog pd;
    ArrayList<String> contacts;
    String phNumber, ph;
    String selectedFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv1 = (ListView) findViewById(R.id.lv);
        ll1 = (LinearLayout) findViewById(R.id.linearLayout1);

        LoadContactsAyscn lca = new LoadContactsAyscn();
        lca.execute();
        registerForContextMenu(lv1);

    }

    private class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(MainActivity.this, "Loading Contacts",
                    "Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            contacts = new ArrayList<>();

            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contacts.add(contactName + "\n" + phNumber);
            }
            c.close();
            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            pd.cancel();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.text, contacts);

            lv1.setAdapter(adapter);
//            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    TextView textView = (TextView) view.findViewById(R.id.tv1);
//                    selectedFromList = (String) lv1.getItemAtPosition(position);
//                    Toast.makeText(MainActivity.this, selectedFromList, Toast.LENGTH_SHORT).show();
//
//                }});
        }


    }



    public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        TextView textView = (TextView) v.findViewById(R.id.tv1);
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }



    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +  ph));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this, "Please give permission for Call", Toast.LENGTH_SHORT).show();
            }
            startActivity(callIntent);
        }
        else if(item.getItemId() == R.id.sms){
            Toast.makeText(getApplicationContext(),selectedFromList,Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }


}
