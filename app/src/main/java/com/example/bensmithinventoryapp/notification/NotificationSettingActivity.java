package com.example.bensmithinventoryapp.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bensmithinventoryapp.R;
import com.example.bensmithinventoryapp.database.DatabaseManager;
import com.example.bensmithinventoryapp.display.DisplayAllActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

/**
 * This activity allows the user to opt into notifications.
 */
public class NotificationSettingActivity extends AppCompatActivity {

    private TextInputLayout txtInputPhoneNumber;
    private Switch acceptSwitch;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        extras = getIntent().getExtras();

        txtInputPhoneNumber = findViewById(R.id.notificationSettingPhone);

        acceptSwitch = findViewById(R.id.notificationSettingSwitch);
        acceptSwitch.setChecked(DatabaseManager.getInstance(getApplicationContext()).getUserNotify(getUserName()) > 0 ? true : false);
        acceptSwitch.setOnClickListener(listener -> handleAcceptSwitch());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.notificationSetting);
        bottomNav.setOnItemSelectedListener(this::handleBottomNav);
    }

    /**
     * This method handles the accept switch on click listener.
     */
    private void handleAcceptSwitch(){
        String phone = txtInputPhoneNumber.getEditText().getText().toString();
        DatabaseManager.getInstance(getApplicationContext()).updateUser(getUserName(), null, phone, acceptSwitch.isChecked() ? 1 : 0);
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
                return true;
            case R.id.displayAll:
                startDisplayAllIntent();
                return true;
        }
        return true;
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

}