package com.gauchoshout.android.ui;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gauchoshout.android.R;
import com.gauchoshout.android.data.LoginResponse;
import com.gauchoshout.android.http.WebServiceTask;
import com.gauchoshout.android.util.JsonParser;
import com.gauchoshout.android.util.SessionStore;

public class LoginActivity extends SherlockFragmentActivity implements OnClickListener, OnEditorActionListener {
    
    EditText username_et;
    EditText password_et;
    Button login_btn;
    Button newuser_btn;
    private boolean loginDisabled = false;
    private ProgressBar progBar;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_login);
        
        if (SessionStore.isValidSession(this)) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        initLayout();
    }
    
    private void initLayout() {
        username_et = (EditText) findViewById(R.id.login_username_et);
        password_et = (EditText) findViewById(R.id.login_password_et);
        password_et.setOnEditorActionListener(this);
        progBar = (ProgressBar) findViewById(R.id.progressBar);
        // Initially hide the loading bar. Nothing is loading why would it be there?
        progBar.setVisibility(View.GONE);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        newuser_btn = (Button) findViewById(R.id.newuser_btn);
        newuser_btn.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_btn:
        	if (!loginDisabled){
        		loginDisabled = true;
        		login_btn.setEnabled(false);
        		newuser_btn.setEnabled(false);
        		progBar.setVisibility(View.VISIBLE);
        		// Hey we're thinking, make it look like it with a progress bar
        		authorize();
        	}
            break;
        case R.id.newuser_btn:
        	Intent i = new Intent(LoginActivity.this, NewAccountActivity.class);
        	startActivity(i);
            break;
        }
    }

    private void authorize() {
        String username = username_et.getText().toString();
        String password = password_et.getText().toString();
        if (username.length() == 0 || password.length() == 0){
        	Toast.makeText(LoginActivity.this, "Missing username or password", Toast.LENGTH_SHORT).show();
        	// Glorious error message for the user.
        	progBar.setVisibility(View.GONE);
        	loginDisabled = false;
        	login_btn.setEnabled(true);
        	newuser_btn.setEnabled(true);
        	return;
        }
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
        // These NameValuePairs are used to send POST data to the web server.
        // So this can be handled by the PHP with $_POST['username'] and $_POST['password']
        NameValuePair usernameParam = new BasicNameValuePair("username", username);
        NameValuePair passwordParam = new BasicNameValuePair("password", password);
        
        LoginTask loginTask = new LoginTask();
        loginTask.execute(usernameParam, passwordParam);
    }
    
    private void handleLogin(LoginResponse loginResponse) {
      switch (loginResponse.getErrorCode()) {
      case INVALID_JSON:
          Toast.makeText(LoginActivity.this, "invalid json", Toast.LENGTH_SHORT).show();
          break;
      case NO_ERROR:
          handleSuccessfulLogin(loginResponse);
          break;
      case WRONG_CREDENTIALS:
    	  DialogHelper.showErrorDialog(this, "Wrong credentials.");
          break;
      case USER_NOT_FOUND:
    	  DialogHelper.showErrorDialog(this, "User not found.");
          break;
      case MISSING_INFORMATION:
          Toast.makeText(LoginActivity.this, "missing information", Toast.LENGTH_SHORT).show();
      }
    }
    
    private void handleSuccessfulLogin(LoginResponse response) {
        SessionStore.save(response, this);
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private class LoginTask extends WebServiceTask {
        // Use you https for great good.
    	// originally we had no https and SSL, so passwords were sent in plaintext to the web server
    	// Nobody likes that. On with SSL
        private static final String LOGIN_URL = "https://gauchoshout.herokuapp.com/login.php?action=login";
        
        private LoginResponse response;

        public LoginTask() {
            super(LOGIN_URL,LoginActivity.this);
        }
        
        @Override
        protected Boolean doInBackground(NameValuePair... pairs) {
            boolean success = super.doInBackground(pairs);
            if (success) {
                response = JsonParser.parseLoginResponse(raw_json);
            }
            
            return success;
        }
        
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                handleLogin(response);
            } else {
                Toast.makeText(LoginActivity.this, "epic failure", Toast.LENGTH_SHORT).show();
                // Something went wrong in the response from the web server. Something horribly wrong.
            }
            loginDisabled = false;
    		login_btn.setEnabled(true);
    		newuser_btn.setEnabled(true);
    		progBar.setVisibility(View.GONE);
        }
        
    }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_GO:
			authorize();
			return true;
		default:
			return false;
		}
	}
}
