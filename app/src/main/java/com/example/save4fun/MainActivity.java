package com.example.save4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.save4fun.db.DBUsersHelper;
import com.example.save4fun.model.User;
import com.example.save4fun.util.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 100;
    EditText editTextMainUsername, editTextMainPassword;
    Button buttonLogin;
    String DBNAME = Constant.DBNAME;
    TextView textViewGoToSignUp;
    DBUsersHelper dbUsersHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To remove db
        // clearDBAndSharedPreferences();
        processCopyDB();

        editTextMainUsername = findViewById(R.id.editTextMainUsername);
        editTextMainPassword = findViewById(R.id.editTextMainPassword);

        buttonLogin = findViewById(R.id.buttonLogin);

        textViewGoToSignUp = findViewById(R.id.textViewGoToSignUp);

        dbUsersHelper = new DBUsersHelper(MainActivity.this);

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
        boolean hasLoggedIn = sharedPreferences.getBoolean(Constant.HAS_LOGGED_IN, false);

        if(hasLoggedIn) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextMainUsername.getText().toString();
                String password = editTextMainPassword.getText().toString();
                User user = new User(username, password);

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                }
                boolean res = dbUsersHelper.authenticate(username, password);
                if (res) {
                    Toast.makeText(MainActivity.this, "Signed in successfully", Toast.LENGTH_LONG).show();

                    SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constant.HAS_LOGGED_IN, true);
                    editor.putString(Constant.USERNAME, username);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clearDBAndSharedPreferences() {
        MainActivity.this.deleteDatabase(DBNAME);

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
        sharedPreferences.edit().remove(Constant.HAS_LOGGED_IN).apply();
        sharedPreferences.edit().remove(Constant.USERNAME).apply();
    }

    private void processCopyDB() {
        File dbFile = getDatabasePath(DBNAME);
        if (!dbFile.exists()) {
            try {
                CopyDatabaseFromAsset();
                Toast.makeText(this, "Successfully copied from assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + Constant.DB_PATH_SUFFIX + DBNAME;
    }

    private void CopyDatabaseFromAsset() {
        try {
            InputStream input;
            input = getAssets().open(DBNAME);
            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + Constant.DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }

            OutputStream output = new FileOutputStream(outFileName);
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            output.write(buffer);

            output.flush();
            output.close();
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}