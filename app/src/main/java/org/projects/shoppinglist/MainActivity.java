package org.projects.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.OnPositiveListener {


    //ArrayAdapter<Product> adapter;
    FirebaseListAdapter mAdapter;
    ListView listView;
    //ArrayList<Product> bag = new ArrayList<Product>();
    Product backup;
    static Context context;
    MyDialogFragment dialog;
    String name;
    private static final String TAG = "projects.shoppinglist";

    // Firebase db instance reference
    FirebaseUser user;
    DatabaseReference mRootRef;
    DatabaseReference mBagRef;


    EditText productName, productQuantity;

    public FirebaseListAdapter getMyAdapter()
    {
        return mAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        productName = (EditText) findViewById(R.id.editProductText);
        productQuantity = (EditText) findViewById(R.id.editProductQuantityText);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate");
        name = MyPreferenceFragment.getName(this);

        // Setup firebase references
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mBagRef = mRootRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("items");

        // FirebaseUI Setup
        // Getting our listiew - you can check the ID in the xml to see that it
        // is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);

        // here we create a new firebase adapter
        mAdapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_1, mBagRef) {
            @Override
            protected void populateView(View v, Product model, int position) {
                ((TextView) v.findViewById(android.R.id.text1)).setText(model.toString());
            }
        };


        //setting the adapter on the listview
        listView.setAdapter(mAdapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Set long click listener for single cell delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {


                // Set our backup property
                backup = getItem(position);

                Snackbar snackbar = Snackbar
                        .make(parent, backup.toString() + " removed.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //This code will ONLY be executed in case that
                                //the user has hit the UNDO button
                                //get backup and insert it into its last position
                                mBagRef.push().setValue(new Product(backup.getName(), backup.getQuantity()));
                                getMyAdapter().notifyDataSetChanged();

                                Snackbar snackbar = Snackbar.make(parent, "Restored!", Snackbar.LENGTH_SHORT);

                                //Show the user we have restored the name - but here
                                //on this snackbar there is NO UNDO - so no SetAction method is called
                                snackbar.show();
                            }
                        });

                // Remove the product
                getMyAdapter().getRef(position).removeValue();
                getMyAdapter().notifyDataSetChanged();

                // Display the snackbar
                snackbar.show();

                return false;
            }
        });

        // Add to bag button functionality
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the Product Name and Quantity fields are valid
                if(productName.getText() != null && productName.getText().toString().length() > 2 && productQuantity.getText() != null && Integer.parseInt(productQuantity.getText().toString()) > 0) {
                    // Push new product to firebase
                    mBagRef.push().setValue(new Product(productName.getText().toString(), Integer.parseInt(productQuantity.getText().toString())));
                    //bag.add(new Product(productName.getText().toString(), Integer.parseInt(productQuantity.getText().toString())));

                    // Clear the Product Name and Quantity Inputs
                    productName.setText("");
                    productQuantity.setText("");
                    productQuantity.clearFocus();
                }
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

        //add some stuff to the list so we have something
        // to show on app startup
        //mBagRef.push().setValue(new Product("Bananas", 1));
        //mBagRef.push().setValue(new Product("Eggs", 10));
        //mBagRef.push().setValue(new Product("Tomatoes", 2));
        //mBagRef.push().setValue(new Product("Cucumber", 1));
    }

    public Product getItem(int index) {
        return (Product) getMyAdapter().getItem(index);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mAdapter.cleanup();
    }

    //This method is called before our activity is destoryed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD - To be nice!
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

		// Here we put code now to save the state
        outState.putString("ProductName", productName.getText().toString());
        outState.putString("ProductQuantity", productQuantity.getText().toString());
        //outState.putParcelableArrayList("ProductBag", bag);
    }

    //this is called when our activity is recreated, but
    //AFTER our onCreate method has been called
    //EXTREMELY IMPORTANT DETAIL
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        //MOST UI elements will automatically store the information
        //if we call the super.onRestoreInstaceState
        //but other data will be lost.
        super.onRestoreInstanceState(savedState);
        Log.d(TAG, "onRestoreInstanceState");

		/*Here we restore any state
		for(Parcelable p : savedState.getParcelableArrayList("ProductBag")) {
            getMyAdapter().add(p);
        }*/

		// Restore input fields if they were filled
        productName.setText(savedState.getString("ProductName"));
        productQuantity.setText(savedState.getString("ProductQuantity"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1) //the code means we came back from settings
        {
            name = MyPreferenceFragment.getName(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Start our settingsactivity and listen to result - i.e.
            //when it is finished.
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivityForResult(intent,1);
            //notice the 1 here - this is the code we then listen for in the
            //onActivityResult
        }

        if (id == R.id.action_clearList) {

            // Display Alert Dialog before deleting the whole list
            dialog = new MyDialogFragment();
            dialog.show(getFragmentManager(), "MyDialogFragment");
        }

        if (id == R.id.action_shareAsText) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain"); //MIME type

            // Build our message
            String msg = "";

            // Add username if it's not null
            if ( name != null ) {
                msg += name + " has shared a shopping list:\n";
            }

            // Add our products to list
            for(int i = 0; i < getMyAdapter().getCount(); i++) {
                // counter for numeration
                int counter = i + 1;
                msg += counter + ". " + getItem(i).toString() + "\n";
            }

            intent.putExtra(Intent.EXTRA_TEXT, msg); //add the text to t
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveClicked() {

        // Remove all data from the list
        for (int i = 0; i < getMyAdapter().getCount(); i++) {
            getMyAdapter().getRef(i).removeValue();
        }

        // Notify for changes
        getMyAdapter().notifyDataSetChanged();

        Toast toast = Toast.makeText(context,
                "New shopping list started", Toast.LENGTH_SHORT);
        toast.show();
    }
}
