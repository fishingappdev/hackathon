package com.example.dmiadmin.hackathonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dmiadmin.hackathonapp.util.Constants;

/**
 * Created by pooja on 1/20/2017.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mEmailEdit,mPasswordEdit;
    private Button mLoginBtn;
    private TextInputLayout mEmailInput,mPasswordInput;
    private String mEmail,mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailEdit=(EditText)findViewById(R.id.email_edit);
        mPasswordEdit=(EditText)findViewById(R.id.password_edit);
        mLoginBtn=(Button)findViewById(R.id.log_in_button);
        mEmailInput=(TextInputLayout)findViewById(R.id.email_input);
        mPasswordInput=(TextInputLayout)findViewById(R.id.password_input);

        mLoginBtn.setOnClickListener(this);

        mEmailInput.setError(null);
        mPasswordInput.setError(null);

        mEmailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPasswordInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void setEmailError(String errorString) {
        mEmailInput.setError(errorString);
    }

    public void setPasswordError(String errorString) {
        mPasswordInput.setError(errorString);
    }

    public boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void validateEmailPassword(){
        mEmail = mEmailEdit.getText().toString().trim();
        mPassword = mPasswordEdit.getText().toString().trim();
        if (mEmail.isEmpty()) {
            setEmailError((getString(R.string.empty_email)));
            return;
        } else if (mPassword.isEmpty()) {
            setPasswordError(getString(R.string.empty_password));
            return;
        }
        else if (!validateEmail(mEmail)) {
            setEmailError(getString(R.string.invalid_email));
            return;
        }else{
            doLogin();
        }


    }
    /*Method to logggedin and move to next screen*/
    private void doLogin(){
        if(mEmail.equalsIgnoreCase(Constants.ADMIN_EMAIL) && mPassword.equalsIgnoreCase(Constants.ADMIN_PASSWORD)){
            startMainActivity();
        }else if(mEmail.equalsIgnoreCase(Constants.USER_EMAIL) && mPassword.equalsIgnoreCase(Constants.USER_PASSWORD)){
            startMainActivity();
        }else{
            Toast.makeText(LoginActivity.this, R.string.invalid_email_password,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        validateEmailPassword();
    }

    private void startMainActivity(){
        startActivity(new Intent(LoginActivity.this,MainActionActivity.class));
    }
}
