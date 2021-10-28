package algonquin.cst2335.yin00043;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        Log.d( "MainActivity", "In onCreate() - Loading Widgets" );

        Button loginButton = findViewById(R.id.loginButton);
        EditText emailEditText = findViewById(R.id.editTextEmail);

        loginButton.setOnClickListener(clk-> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra( "EmailAddress", emailEditText.getText().toString() );
            startActivity(nextPage);


        } );

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String defaultValue = "VariableName";
        prefs.getString("VariableName", "");
        String emailAddress = prefs.getString("LoginName", "");
        emailEditText.setText(emailAddress);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("StringName", "");
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d( "MainActivity", "In onStart() -  The application is now visible on screen." );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d( "MainActivity", "In onResume() - The application is now responding to user input." );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d( "MainActivity", "In onPause() - The application no longer responds to user input." );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d( "MainActivity", "In onStop() - The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( "MainActivity", "In onDestroy() - Any memory used by the application is freed." );
    }
}