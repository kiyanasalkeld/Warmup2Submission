package edu.berkeley.kiyana.salkeld.Warm_Up_1;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONException;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.os.AsyncTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;




public class MyActivity extends Activity {
    TextView loginScreenMessage;
    TextView errorMessages;
    EditText usernameInputBox;
    EditText passwordInputBox;
    Button loginButton;
    Button addUserButton;
    String serverURL = "http://calm-peak-6076.herokuapp.com";
    String loginURL = "/users/login";
    String addUserURL = "/users/add";
    String debugTag = "HTTP response";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loginScreenMessage = (TextView) findViewById(R.id.pleaseEnterYourCredentials);
        errorMessages = (TextView) findViewById(R.id.errorMessage);
        usernameInputBox = (EditText) findViewById(R.id.userNameInput);
        passwordInputBox = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button) findViewById(R.id.login_button) ;
        addUserButton = (Button) findViewById(R.id.add_user_button);



    }

    private class AccessServerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
             //doInBackground calls downloadURL
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                System.err.println(result);
                JSONObject userAndPasswordInformation = new JSONObject(result);
                if ((Integer)userAndPasswordInformation.get("errCode") == 1 ) {
                    //switch to LoginScreen Activity
                    Intent openLoginScreenIntent = new Intent(MyActivity.this, LoginScreen.class);
                    openLoginScreenIntent.putExtra("username", usernameInputBox.getText().toString());
                    openLoginScreenIntent.putExtra("count", ((Integer)userAndPasswordInformation.get("count")).toString());
                    startActivity(openLoginScreenIntent);

                }
                if ((Integer)userAndPasswordInformation.get("errCode") == -1){
                    //Cannot find the user/password pair in the database (for login only)
                    //update TextView to read "Invalid username and password combination. Please try again."
                    loginScreenMessage.setVisibility(View.INVISIBLE);
                    errorMessages.setText("Invalid username and password combination. Please try again.");
                    errorMessages.setVisibility(View.VISIBLE);

                }
                if ((Integer)userAndPasswordInformation.get("errCode") == -2){
                    //trying to add a user that already exists (for add only)
                    //update TextView to read "This username already exists. Please try again."
                    loginScreenMessage.setVisibility(View.INVISIBLE);
                    errorMessages.setText("This username already exists. Please try again.");
                    errorMessages.setVisibility(View.VISIBLE);


                }
                if ((Integer)userAndPasswordInformation.get("errCode") == -3){
                    //invalid user name
                    //update TextView to read "Invalid username and password combination. Please try again."
                    loginScreenMessage.setVisibility(View.INVISIBLE);
                    errorMessages.setText("This username already exists. Please try again.");
                    errorMessages.setVisibility(View.VISIBLE);

                }
                if ((Integer)userAndPasswordInformation.get("errCode") == -4){
                  //invalid password name
                  //update TextView to read "Invalid username and password combination. Please try again."
                    loginScreenMessage.setVisibility(View.INVISIBLE);
                    errorMessages.setText("This username already exists. Please try again.");
                    errorMessages.setVisibility(View.VISIBLE);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




        }
    }
   //I wrote the following code from code I adapted from https://developer.android.com/training/basics/network-ops/connecting.html
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            System.err.println(myurl);
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.connect();



            //Take input to EditText widgets and create a JSON object from this input
            //I adapted JSON object code from this Stack Overflow post: http://stackoverflow.com/questions/17810044/android-create-json-array-and-json-object
            String username = usernameInputBox.getText().toString();
            String password = passwordInputBox.getText().toString();
            JSONObject userAndPassword = new JSONObject();
            try {
                userAndPassword.put("user", username);
                userAndPassword.put("password", password);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //I adapted JSON object code from this Stack Overflow post: http://stackoverflow.com/questions/17810044/android-create-json-array-and-json-object
            DataOutputStream myDataOutputStream = new DataOutputStream(conn.getOutputStream());
            myDataOutputStream.writeBytes(userAndPassword.toString());
            myDataOutputStream.flush();
            myDataOutputStream.close();


            int response = conn.getResponseCode();
            Log.d(debugTag, "The response is: " + response);

            is = conn.getInputStream();
            StringBuilder myStringBuilder = new StringBuilder();
            BufferedReader myBufferedReader= new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"utf-8"));
            String line = null;
            while ((line = myBufferedReader.readLine()) != null) {
                myStringBuilder.append(line);
            }
            //downloadURL returns to postExecute
            return myStringBuilder.toString();


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    public void loginButtonClicked(View v){
     //The user clicks the Login Button
        //execute() calls doInBackground()
        this.new AccessServerTask().execute(serverURL+loginURL);






    }

    public void addUserButtonClicked(View v){
        //the user clicks the Add User Button
        //execute() calls doInBackground()
        this.new AccessServerTask().execute(serverURL+addUserURL);





    }

}
