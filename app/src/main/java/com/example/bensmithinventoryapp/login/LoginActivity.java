package com.example.bensmithinventoryapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.bensmithinventoryapp.R;
import com.example.bensmithinventoryapp.database.DatabaseManager;
import com.example.bensmithinventoryapp.display.DisplayAllActivity;
import com.google.android.material.textfield.TextInputLayout;

/**
 * This class contains functionality related to the LoginActivity.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout txtInputUsername, txtInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtInputUsername = findViewById(R.id.textInputUsername);
        txtInputPassword = findViewById(R.id.textInputPassword);

        Button btnLogin = findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(listener -> handleLogin());

        Button btnSignup = findViewById(R.id.signupButton);
        btnSignup.setOnClickListener(listener -> handleSignup());
    }

    /**
     * This method handles the login button on click listener.
     */
    private void handleLogin() {
        String user = txtInputUsername.getEditText().getText().toString();
        String pass = txtInputPassword.getEditText().getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            authenticate(user, pass);
        }
    }

    /**
     * This method handles the signup button on click listener.
     */
    private void handleSignup() {
        String user = txtInputUsername.getEditText().getText().toString();
        String pass = txtInputPassword.getEditText().getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            checkUsername(user, pass);
        }
    }

    /**
     * Authenticates the user's credentials.
     * @param username - the username
     * @param password - the password
     */
    private void authenticate(String username, String password){
        Boolean exists = DatabaseManager.getInstance(getApplicationContext()).authenticate(username, password);
        if (exists) {
            Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
            startDisplayAllIntent(username);
        } else {
            Toast.makeText(LoginActivity.this, "Invalid login information", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if username already exists. Will create new user if username does not exist.
     * @param username - the username
     * @param password - the password
     */
    private void checkUsername(String username, String password){
        if (!DatabaseManager.getInstance(getApplicationContext()).usernameExists(username)) {
            createUser(username, password);
        } else {
            Toast.makeText(LoginActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates new user.
     * @param username - the username
     * @param password - the password
     */
    private void createUser(String username, String password){
        Boolean created = DatabaseManager.getInstance(getApplicationContext()).createUser(username, password);
        if (created) {
            Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
            startDisplayAllIntent(username);
        } else {
            Toast.makeText(LoginActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigates to the @DisplayAllActivity
     * @param username - the username
     */
    private void startDisplayAllIntent(String username){
        Intent intent = new Intent(getApplicationContext(), DisplayAllActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}