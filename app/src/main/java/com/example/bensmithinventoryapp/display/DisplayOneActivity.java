package com.example.bensmithinventoryapp.display;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bensmithinventoryapp.notification.NotificationSettingActivity;
import com.example.bensmithinventoryapp.R;
import com.example.bensmithinventoryapp.database.DatabaseManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

/**
 * This activity displays one of the @Item in the Items db.
 */
public class DisplayOneActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private TextInputLayout txtInputItemName;
    private TextView txtViewItemQuantity;
    private int intCount = 0;
    private Bundle extras;
    private String strPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_one);

        extras = getIntent().getExtras();

        txtInputItemName = findViewById(R.id.textInputItemName);
        txtViewItemQuantity = findViewById(R.id.textInputQuantity);

        populateExistingValues();

        ImageButton btnIncrement = findViewById(R.id.incrementButton);
        btnIncrement.setOnClickListener(listener -> handleIncrement());

        ImageButton btnDecrement = findViewById(R.id.decrementButton);
        btnDecrement.setOnClickListener(listener -> handleDecrement());

        Button btnUpdate = findViewById(R.id.updateButton);
        btnUpdate.setOnClickListener(listener -> handleUpdate());

        Button btnDelete = findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(listener -> handleDelete());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.displayAll);
        bottomNav.setOnItemSelectedListener(this::handleBottomNav);
    }

    /**
     * This method populates values from the selected item.
     */
    private void populateExistingValues(){
        if (extras != null) {
            String name = extras.getString("item_name");
            intCount = extras.getInt("item_quantity", 0);

            if (name != null) {
                txtInputItemName.getEditText().setText(name);
            }

            txtViewItemQuantity.setText("" + intCount);

        } else {
            txtViewItemQuantity.setText("" + intCount);
        }
    }

    /**
     * This method handles the increment button on click listener.
     */
    private void handleIncrement() {
        intCount++;
        txtViewItemQuantity.setText("" + intCount);
    }

    /**
     * This method handles the decrement button on click listener.
     */
    private void handleDecrement() {
        if (intCount > 0) {
            intCount--;
        }
        txtViewItemQuantity.setText("" + intCount);
    }

    /**
     * This method handles the update button on click listener.
     */
    private void handleUpdate() {
        String name = txtInputItemName.getEditText().getText().toString();
        Integer quantity = Integer.parseInt(txtViewItemQuantity.getText().toString());

        if (name.isEmpty() || quantity < 0) {
            Toast.makeText(DisplayOneActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            Boolean exists = DatabaseManager.getInstance(getApplicationContext()).itemExists(name);
            if (exists) {
                Boolean updated = DatabaseManager.getInstance(getApplicationContext()).updateItem(name, quantity);
                if (updated) {
                    Toast.makeText(DisplayOneActivity.this, "Updated item", Toast.LENGTH_SHORT).show();
                    if (quantity == 0) {
                        strPhone = DatabaseManager.getInstance(getApplicationContext()).getUserPhone(getUserName());
                        sendSMSMessage();
                    }
                    startDisplayAllIntent();
                } else {
                    Toast.makeText(DisplayOneActivity.this, "Error updating item", Toast.LENGTH_SHORT).show();
                }
            } else {
                Boolean created = DatabaseManager.getInstance(getApplicationContext()).createItem(name, quantity);
                if (created) {
                    Toast.makeText(DisplayOneActivity.this, "Added item", Toast.LENGTH_SHORT).show();
                    startDisplayAllIntent();
                } else {
                    Toast.makeText(DisplayOneActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * This method handles the delete button on click listener.
     */
    private void handleDelete() {
        String name = txtInputItemName.getEditText().getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(DisplayOneActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            Boolean deleted = DatabaseManager.getInstance(getApplicationContext()).deleteItem(name);
            if (deleted) {
                Toast.makeText(DisplayOneActivity.this, "Deleted item", Toast.LENGTH_SHORT).show();
                startDisplayAllIntent();
            } else {
                Toast.makeText(DisplayOneActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
            }

        }
        Toast.makeText(DisplayOneActivity.this, "deleteBtnClick", Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates the bottom navigation menu.
     * @param item - the current menu item
     * @return returns true
     */
    private boolean handleBottomNav(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.notificationSetting:
                startNotificationSettingsIntent();
                return true;
            case R.id.displayAll:
                startDisplayAllIntent();
                return true;
        }
        return true;
    }

    /**
     * Navigates to the @NotificationSettingActivity
     */
    private void startNotificationSettingsIntent(){
        Intent intent = new Intent(getApplicationContext(), NotificationSettingActivity.class);
        addUserToIntent(intent);
        startActivity(intent);
    }

    /**
     * Navigates to the @DisplayAllActivity
     */
    private void startDisplayAllIntent(){
        Intent intent = new Intent(getApplicationContext(), DisplayAllActivity.class);
        addUserToIntent(intent);
        startActivity(intent);
    }

    /**
     * Adds the current user to the Intent.
     * @param intent - the intent
     */
    private void addUserToIntent(Intent intent) {
        if (extras != null) {
            String username = extras.getString("username");
            intent.putExtra("username", username);
        }
    }

    /**
     * Gets the current username.
     * @return the current username
     */
    private String getUserName(){
        if (extras != null) {
            return extras.getString("username");
        } else {
            return null;
        }
    }

    protected void sendSMSMessage(){
        if (ContextCompat.checkSelfPermission(DisplayOneActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DisplayOneActivity.this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(DisplayOneActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            Toast.makeText(getApplicationContext(), "SMS fail",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage
                            (strPhone, null, "Inventory Low",
                                    null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}