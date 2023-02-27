package com.example.bensmithinventoryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bensmithinventoryapp.display.item.Item;

import java.util.ArrayList;

/**
 * This class manages database related activities for the tables in the Inventory.db database.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATA_BASE_NAME = "Inventory.db";
    public static final int VERSION = 1;

    private static DatabaseManager instance;

    /**
     * This method returns the singleton instance of the database manager.
     * @param context - the Context
     * @return - the @DatabaseManager singleton instance
     */
    public static DatabaseManager getInstance(@Nullable Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    private DatabaseManager(@Nullable Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createUserTable());
        sqLiteDatabase.execSQL(createItemsTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP Table IF exists " + UserTable.TABLE);
        sqLiteDatabase.execSQL("DROP Table IF exists " + ItemsTable.TABLE);
    }

    private String createUserTable(){
        return "CREATE Table " + UserTable.TABLE + " ("
                + UserTable.COL_USERNAME + " TEXT primary key, "
                + UserTable.COL_PASSWORD + " TEXT, "
                + UserTable.COL_PHONE + " TEXT, "
                + UserTable.COL_NOTIFY + " INTEGER)";
    }

    private String createItemsTable(){
        return "CREATE Table " + ItemsTable.TABLE + " ("
                + ItemsTable.COL_ITEM_NAME + " TEXT primary key, "
                + ItemsTable.COL_ITEM_COUNT + " INTEGER)";
    }

    /**
     * Determines whether item exists in db.
     * @param itemName - the item name
     * @return true if the item exists, false if the item does not exist
     */
    public Boolean itemExists(String itemName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * "
                + "FROM " + ItemsTable.TABLE
                + " WHERE " + ItemsTable.COL_ITEM_NAME + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[] {itemName});

        return isValidQuery(cursor);
    }

    /**
     * Adds an item to the Items Table.
     * @param itemName - the item name
     * @param itemCount - the item count
     * @return true if the item was successfully added, false if the item was not added
     */
    public Boolean createItem(String itemName, Integer itemCount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long queryResult = sqLiteDatabase.insert(ItemsTable.TABLE, null, createContent(itemName, itemCount));

        return querySuccessful(queryResult);
    }

    /**
     * Returns a list of @Item objects in the Items Table.
     * @return a list of @Item objects.
     */
    public ArrayList<Item> readItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ItemsTable.TABLE, null);

        ArrayList<Item> items = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                items.add(new Item(cursor.getString(0),
                        cursor.getInt(1)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return items;
    }

    /**
     * Updates an item in the Items Table
     * @param name - the name of the item
     * @param itemCount - the item count
     * @return true if the item was updated, false if the item was not updated
     */
    public Boolean updateItem(String name, Integer itemCount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long queryResult = sqLiteDatabase.update(ItemsTable.TABLE, createContent(name, itemCount), ItemsTable.COL_ITEM_NAME + " = ?", new String[] {name});

        return querySuccessful(queryResult);
    }

    /**
     * Deletes an item in the Items Table
     * @param itemName - the item name
     * @return true if the item was deleted, false if the item was not deleted
     */
    public Boolean deleteItem(String itemName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long queryResult = sqLiteDatabase.delete(ItemsTable.TABLE, ItemsTable.COL_ITEM_NAME + " = ?", new String[]{itemName});

        return querySuccessful(queryResult);
    }

    /**
     * Gets the notify value of a user in the User Table
     * @param username - the username
     * @return 0 for do not notify, 1 for notify
     */
    public Integer getUserNotify(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + UserTable.COL_NOTIFY + " FROM " + UserTable.TABLE + " WHERE " + UserTable.COL_USERNAME + " = " + username, null);
        
        Integer notify = 0;

        if (cursor.moveToFirst()) {
            do {
                notify = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        
        return notify;
    }

    /**
     * Gets the user's phone number from the User Table
     * @param username - the username
     * @return a string representing the user's phone number
     */
    public String getUserPhone(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + UserTable.COL_PHONE + " FROM " + UserTable.TABLE + " WHERE " + UserTable.COL_USERNAME + " = " + username, null);

        String phone = null;

        if (cursor.moveToFirst()) {
            do {
                phone = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return phone;
    }

    /**
     * Checks if a username exists in the User Table
     * @param username - the username
     * @return true if the username exists, false if the username does not exist
     */
    public Boolean usernameExists(String username){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * "
                + "FROM " + UserTable.TABLE
                + " WHERE " + UserTable.COL_USERNAME + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[] {username});

        return isValidQuery(cursor);
    }

    /**
     * Authenticate whether user credentials are valid.
     * @param username - the username
     * @param password - the password
     * @return true if authenticated, false if not authenticated
     */
    public Boolean authenticate(String username, String password){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * "
                + "FROM " + UserTable.TABLE
                + " WHERE " + UserTable.COL_USERNAME + " = ?"
                + " AND " + UserTable.COL_PASSWORD + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[] {username, password});

        return isValidQuery(cursor);
    }

    /**
     * Creates a new user in the User Table.
     * @param username - the username
     * @param password - the password
     * @return true if the user was created, false if the user was not created
     */
    public Boolean createUser(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long queryResult = sqLiteDatabase.insert(UserTable.TABLE, null, createContent(username, password));

        return querySuccessful(queryResult);
    }

    /**
     * Updates a user in the User Table.
     * @param username - the username
     * @param password - the password
     * @param phone - the phone number
     * @param notify - the notification indicator
     * @return true if the user was updated, false if the user was not updated
     */
    public Boolean updateUser(String username, @Nullable String password, @Nullable String phone, @Nullable Integer notify){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long queryResult = sqLiteDatabase.update(UserTable.TABLE, createContent(username, password, phone, notify), UserTable.COL_USERNAME + " = ?", new String[] {username});

        return querySuccessful(queryResult);
    }

    private Boolean isValidQuery(Cursor cursor){
        Boolean validQuery = false;
        if (cursor.moveToFirst()) {
            validQuery = true;
        }

        return validQuery;
    }

    private ContentValues createContent(String username, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COL_USERNAME, username);
        contentValues.put(UserTable.COL_PASSWORD, password);
        contentValues.put(UserTable.COL_NOTIFY, 0);

        return contentValues;
    }

    private ContentValues createContent(String username, @Nullable String password, @Nullable String phone, @Nullable Integer notify){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COL_USERNAME, username);

        if (password != null) {
            contentValues.put(UserTable.COL_PASSWORD, password);
        }

        if (phone != null) {
            contentValues.put(UserTable.COL_PHONE, phone);
        }

        if (notify != null) {
            contentValues.put(UserTable.COL_NOTIFY, notify);
        } else {
            contentValues.put(UserTable.COL_NOTIFY, 0);
        }

        return contentValues;
    }

    private ContentValues createContent(String itemName, Integer itemCount){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemsTable.COL_ITEM_NAME, itemName);
        contentValues.put(ItemsTable.COL_ITEM_COUNT, itemCount);

        return contentValues;
    }

    private Boolean querySuccessful(long queryResult){
        Boolean querySuccessful;
        if (queryResult == -1) {
            querySuccessful = false;
        } else {
            querySuccessful = true;
        }

        return querySuccessful;
    }

    private static final class ItemsTable {
        public static final String TABLE = "items";
        public static final String COL_ITEM_NAME = "itemName";
        public static final String COL_ITEM_COUNT= "itemCount";
    }

    private static final class UserTable {
        public static final String TABLE = "users";
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
        public static final String COL_PHONE = "phone";
        public static final String COL_NOTIFY = "notify";
    }
}
