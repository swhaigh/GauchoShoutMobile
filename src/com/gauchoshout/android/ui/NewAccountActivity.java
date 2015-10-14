package com.gauchoshout.android.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gauchoshout.android.R;
import com.gauchoshout.android.data.RegisterResponse;
import com.gauchoshout.android.http.WebServiceTask;
import com.gauchoshout.android.util.JsonParser;
import com.gauchoshout.android.util.SessionStore;

public class NewAccountActivity extends SherlockFragmentActivity implements OnClickListener{
	
	private Button mAcceptButton;
	private RadioButton mMaleRadioButton;
	private RadioButton mFemaleRadioButton;
	private EditText mPassword1;
	private EditText mPassword2;
	private TextView mEmail;
	private TextView mUsername;
	private TextView mDetails;
	private CheckBox mSubscribe;
	private Button mCancel;
	private boolean registerDisabled = false;
	
	// Here we have a nice regex pattern to verify a user at least made an effort to use a real email.
	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
	      );
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		
		mAcceptButton = (Button)findViewById(R.id.new_user_accept_btn);
		mAcceptButton.setOnClickListener(this);
		mMaleRadioButton = (RadioButton) findViewById(R.id.male_gender);
		mMaleRadioButton.setOnClickListener(this);
		mFemaleRadioButton = (RadioButton) findViewById(R.id.female_gender);
		mFemaleRadioButton.setOnClickListener(this);
		mPassword1= (EditText) findViewById(R.id.first_password);
		mPassword2= (EditText) findViewById(R.id.password_match_atempt);
		mEmail=(TextView) findViewById(R.id.new_user_email);
		mUsername = (TextView) findViewById(R.id.desired_username);
		mDetails = (TextView) findViewById(R.id.about_you);
		mSubscribe = (CheckBox) findViewById(R.id.receive_email_checkbox);
		mCancel = (Button) findViewById(R.id.to_login_screen);
		mCancel.setOnClickListener(this);
		

	}
	
	
	
	@Override
	public void onClick(View v) {				
		switch (v.getId()) {
		//submit button just finished the activity. does nothing for now
		case R.id.new_user_accept_btn:
			if (passwordCheck() && emailCheck() && maxLengthCheck() && !registerDisabled && !badCharacters()){
				registerDisabled = true;
				mAcceptButton.setEnabled(false);
				mCancel.setEnabled(false);
				authorize();
			}
			//finish();
			break;
		case R.id.to_login_screen:
			finish();
			break;
		//so both gender boxes can't be checked at the same time
		case R.id.female_gender:
			if(mMaleRadioButton.isChecked()==true){
				mMaleRadioButton.setChecked(false);	
			}
			break;
		case R.id.male_gender:
			if(mFemaleRadioButton.isChecked()==true){
				mFemaleRadioButton.setChecked(false);		
			}
			break;
			
			
		}
	}
    private void authorize() {
        String username = mUsername.getText().toString();
        String password = mPassword1.getText().toString();
        String email = mEmail.getText().toString();
		String details = mDetails.getText().toString();
		String gender = (mMaleRadioButton.isChecked()) ? "m" : "f";
		boolean subscribe = mSubscribe.isChecked();
		// A whole lot of POST data for the serve.
        NameValuePair usernameParam = new BasicNameValuePair("username", username);
        NameValuePair passwordParam = new BasicNameValuePair("password", password);
        NameValuePair password2Param = new BasicNameValuePair("password2", password);
        NameValuePair emailParam = new BasicNameValuePair("email", email);
        NameValuePair detailsParam = new BasicNameValuePair("details", details);
        NameValuePair genderParam = new BasicNameValuePair("gender", gender);
        // if a user "subscribes" they will get a nice Welcome email from the web server.
        NameValuePair subscribedParam = new BasicNameValuePair("subscribed", (subscribe) ? "1" : "0");
        
        
        RegisterTask registerTask = new RegisterTask();
        registerTask.execute(usernameParam, passwordParam, password2Param, emailParam, detailsParam,
        		genderParam, subscribedParam);
        
    }

    private void handleLogin(RegisterResponse registerResponse) {
        switch (registerResponse.getErrorCode()) {
        case INVALID_JSON:
        	DialogHelper.showErrorDialog(this, "Invalid JSON respnse. Uh-oh!");
            break;
        case NO_ERROR:
            handleSuccessfulLogin(registerResponse);
            Toast.makeText(NewAccountActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            break;
        case USERNAME_TAKEN:
        	DialogHelper.showErrorDialog(this, "Username taken.");
            break;
        case EMAIL_TAKEN:
        	DialogHelper.showErrorDialog(this, "Email taken.");
            break;
        case PASSWORD_MISMATCH:
        	DialogHelper.showErrorDialog(this, "Password mismatch.");
        	break;
        case MISSING_INFORMATION:
        	DialogHelper.showErrorDialog(this, "Missing information.");
            break;
        }
        registerDisabled = false;
		mAcceptButton.setEnabled(true);
		mCancel.setEnabled(true);
        
      }
      
      private void handleSuccessfulLogin(RegisterResponse response) {
          SessionStore.saveRegister(response, this);
          Intent i = new Intent(NewAccountActivity.this, HomeActivity.class);
          startActivity(i);
          finish();
      }

      private class RegisterTask extends WebServiceTask {
          
          private static final String REGISTER_URL = "https://gauchoshout.herokuapp.com/register.php?action=register";
          
          private RegisterResponse response;

          public RegisterTask() {
              super(REGISTER_URL, NewAccountActivity.this);
          }
          
          @Override
          protected Boolean doInBackground(NameValuePair... pairs) {
              boolean success = super.doInBackground(pairs);
              if (success) {
                  response = JsonParser.parseNewAccount(raw_json);
              }
              
              return success;
          }
          
          @Override
          protected void onPostExecute(Boolean success) {
              if (success) {
                  handleLogin(response);
              } else {
            	  DialogHelper.showErrorDialog(NewAccountActivity.this, "Connection Failure");
              }
          }
          
      }
	
	public boolean passwordCheck()
	{
		// Makes most sense to do this client side.
		if(! mPassword1.getText().toString().equals(mPassword2.getText().toString()))
		{
			DialogHelper.showErrorDialog(this, "Password Mismatch");
			return false;
		}
		return true;
	}
	
	
	private boolean emailCheck() {
		Matcher matcher;
		String email = mEmail.getText().toString();
		matcher = EMAIL_ADDRESS_PATTERN.matcher(email);
        if (!matcher.matches()){
        	// verifying email looks like an email. 
        	// ie asdf@qwerty.abc
        	DialogHelper.showErrorDialog(this, "Email invalid");
        	return false;
        }
        return true;
	}
	private boolean maxLengthCheck(){
		// Database limitations for user registration info.
		// Verify it client side so important stuff doesn't get truncated.
		int detailsLength =  mDetails.getText().toString().length();
		int usernameLength = mUsername.getText().toString().length();
		int emailLength = mEmail.getText().toString().length();
		int pass1Len = mPassword1.getText().toString().length();
		int pass2Len = mPassword2.getText().toString().length();
		if (detailsLength > 50) {
			DialogHelper.showErrorDialog(this, "About you must be less than 50 characters");
			return false;
		} else if (usernameLength > 25) {
			DialogHelper.showErrorDialog(this, "Username must be less than 25 characters");
			return false;
		} else if (emailLength > 50) {
			DialogHelper.showErrorDialog(this,"Email must be less than 50 characters");
			return false;
		}
		if (detailsLength == 0){
			DialogHelper.showErrorDialog(this, "Missing details field");
			return false;
		} else if (usernameLength == 0){
			DialogHelper.showErrorDialog(this, "Missing username");
			return false;
		} else if (emailLength == 0) {
			DialogHelper.showErrorDialog(this, "Missing email");
			return false;
		} else if (pass1Len == 0) {
			DialogHelper.showErrorDialog(this, "Please enter a password");
			return false;
		} else if (pass2Len == 0) {
			DialogHelper.showErrorDialog(this, "Please retype your password");
			return false;
		}
		return true;
	}
	public void refresh()
	{
		
		
		
	}
	
	public boolean badCharacters(){
		String username = mUsername.getText().toString();
		String email = mEmail.getText().toString();
		String details = mDetails.getText().toString();
		if (username.contains("'")) {
			DialogHelper.showErrorDialog(this, "Username cannot contain a single quote");
			return true;
		}
		if (email.contains("'")){
			DialogHelper.showErrorDialog(this, "Email cannot contain a single quote");
			return true;
		}
		if (details.contains("'")){
			return true;
		}
		return false;
	}
	
	
}