package com.example.bensmithinventoryapp.display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bensmithinventoryapp.ClickListener;
import com.example.bensmithinventoryapp.display.item.Item;
import com.example.bensmithinventoryapp.display.item.ItemRecyclerView;
import com.example.bensmithinventoryapp.notification.NotificationSettingActivity;
import com.example.bensmithinventoryapp.R;
import com.example.bensmithinventoryapp.database.DatabaseManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * This activity displays all the @Item in the Items db.
 */
public class DisplayAllActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ItemRecyclerView adapter;
    private RecyclerView recyclerView;
    private List<Item> listItem;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);

        extras = getIntent().getExtras();

        listItem = DatabaseManager.getInstance(getApplicationContext()).readItems();

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new ItemRecyclerView(listItem, getApplication(), createListenerItem());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayAllActivity.this));

        FloatingActionButton addFab = findViewById(R.id.add_fab);
        addFab.setOnClickListener(listener -> startAddFabIntent());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.displayAll);
        bottomNav.setOnItemSelectedListener(this::handleBottomNav);

    }

    /**
     * Creates a ClickListener object.
     * @return the ClickListener object
     */
    private ClickListener createListenerItem(){
        return index -> {
            Intent intent = new Intent(getApplicationContext(), DisplayOneActivity.class);
            intent.putExtra("item_name", listItem.get(index).getItemName());
            intent.putExtra("item_quantity", listItem.get(index).getItemQuantity());
            addUserToIntent(intent);
            startActivity(intent);
        };
    }

    /**
     * Navigates to the @DisplayOneActivity
     */
    private void startAddFabIntent(){
        Intent intent = new Intent(getApplicationContext(), DisplayOneActivity.class);
        addUserToIntent(intent);
        startActivity(intent);
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
     * Adds the current user to the Intent.
     * @param intent - the intent
     */
    private void addUserToIntent(Intent intent) {
        if (extras != null) {
            String username = extras.getString("username");
            intent.putExtra("username", username);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}