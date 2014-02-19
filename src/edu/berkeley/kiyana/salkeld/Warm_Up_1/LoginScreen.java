package edu.berkeley.kiyana.salkeld.Warm_Up_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginScreen extends Activity{
    TextView  welcomeTextView;
    TextView  usernamePlaceHolderTextView;
    TextView  youHaveLoggedInTextView;
    TextView  numberOfLoginsPlaceHolderTextView;
    TextView  timesTextView;
    Button    logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String numberOfLogins = intent.getStringExtra("count");
        welcomeTextView = (TextView) findViewById(R.id.welcome);
        usernamePlaceHolderTextView = (TextView) findViewById(R.id.usernamePlaceHolder);
        usernamePlaceHolderTextView.setText(username);
        youHaveLoggedInTextView = (TextView) findViewById(R.id.youHaveLoggedIn);
        numberOfLoginsPlaceHolderTextView = (TextView) findViewById(R.id.numberOfLoginsPlaceHolder);
        numberOfLoginsPlaceHolderTextView.setText(numberOfLogins);
        timesTextView = (TextView) findViewById(R.id.times);
        logoutButton = (Button) findViewById(R.id.logout_button);

    }



    public void logoutButtonClicked(View v){
        Intent returnToLogin = new Intent(this, MyActivity.class);
        startActivity(returnToLogin);
    }
}
