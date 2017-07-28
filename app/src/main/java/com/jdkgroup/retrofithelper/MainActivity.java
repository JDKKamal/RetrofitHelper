package com.jdkgroup.retrofithelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jdkgroup.retrofitlib.APICall;
import com.jdkgroup.retrofitlib.APIListener;
import com.jdkgroup.retrofitlib.ApiClient;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, APIListener {

    String TAG = "Retrofit Example";
    APICall networkCall;

    public static final String BASE_URL = "";

    private AppCompatEditText edtAppUserEmail, edtAppUsername;
    private TextInputLayout inputUserName, inputUserEmail;
    private AppCompatButton btn_add, btn_get, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUserName = (TextInputLayout) findViewById(R.id.input_layout_userName);
        inputUserEmail = (TextInputLayout) findViewById(R.id.input_layout_userEmail);

        edtAppUsername = (AppCompatEditText) findViewById(R.id.edtAppUsername);
        edtAppUserEmail = (AppCompatEditText) findViewById(R.id.edtAppUserEmail);

        btn_add = (AppCompatButton) findViewById(R.id.btn_add);
        btn_get = (AppCompatButton) findViewById(R.id.btn_get);
        btn_update = (AppCompatButton) findViewById(R.id.btn_update);

        btn_add.setOnClickListener(this);
        btn_get.setOnClickListener(this);
        btn_update.setOnClickListener(this);

        ApiClient.setBaseUrl(BASE_URL);
        ApiClient.setNetworkErrorMessage("Network Error");
        ApiClient.showNetworkErrorMessage(false);
        networkCall = new APICall(this);

    }

    public void addUser() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user[email]", edtAppUserEmail.getText().toString());
        queryParams.put("user[name]", edtAppUsername.getText().toString());

        networkCall.APIRequest(APICall.Method.POST, "user", UserDetails.class, null, queryParams, 1, "Adding");
    }

    public void getUserDetails() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user[email]", edtAppUserEmail.getText().toString());

        networkCall.APIRequest(APICall.Method.GET, "user", UserDetails.class, null, queryParams, 2, "Loading");
    }

    public void updateUser() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user[email]", edtAppUserEmail.getText().toString());
        queryParams.put("user[name]", edtAppUsername.getText().toString());

        networkCall.APIRequest(APICall.Method.PUT, "user", UserDetails.class, null, queryParams, 3, "Updating");
    }


    @Override
    public void onSuccess(int from, Response response, Object res) {
        System.out.println("TAG" + response +""+ res);
        switch (from) {
            case 1: {
                UserDetails userDetails = (UserDetails) res;

                if (userDetails.getDetails() != null) {

                    Log.d(TAG, "User ID: " + userDetails.getDetails().getId());
                    Toast.makeText(getApplicationContext(), "Successfully Added!", Toast.LENGTH_SHORT).show();
                    edtAppUserEmail.setText("");
                    edtAppUsername.setText("");
                    inputUserEmail.setErrorEnabled(false);
                    inputUserName.setErrorEnabled(false);
                    requestFocus(edtAppUserEmail);
                } else {
                    Log.d(TAG, "Something missing");
                }
            }
            break;
            case 2: {
                UserDetails userDetails = (UserDetails) res;

                if (userDetails.getDetails() != null) {
                    Log.d(TAG, "User ID: " + userDetails.getDetails().getId());
                    edtAppUsername.setText(userDetails.getDetails().getName());
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User details does not exist");
                }
            }
            break;
            case 3: {
                UserDetails userDetails = (UserDetails) res;
                if (userDetails.getDetails() != null) {
                    Log.d(TAG, "User ID: " + userDetails.getDetails().getId());
                    Toast.makeText(getApplicationContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                    requestFocus(edtAppUserEmail);
                    Log.d(TAG, "Something missing");
                }
            }
            break;
        }
    }

    @Override
    public void onFailure(int from, Throwable t) {
        Log.e(TAG, t.toString());
    }

    @Override
    public void onNetworkFailure(int from) {
        Log.e(TAG, String.valueOf(from));
    }

    private boolean validateText(EditText etText, TextInputLayout inputlayout) {
        if (etText.getText().toString().trim().isEmpty()) {
            inputlayout.setError("Enter valid text");
            requestFocus(etText);
            return false;
        } else {
            inputlayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail(EditText etText, TextInputLayout inputlayout) {
        String email = etText.getText().toString().trim();

        if (!isValidEmail(email)) {
            inputlayout.setError("Enter valid Mail ID");
            requestFocus(etText);
            return false;
        } else {
            inputlayout.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_add:
                if (validateText(edtAppUsername, inputUserName) && validateEmail(edtAppUserEmail, inputUserEmail))
                    addUser();
                break;

            case R.id.btn_get:
                if (validateEmail(edtAppUserEmail, inputUserEmail))
                    getUserDetails();
                break;

            case R.id.btn_update:
                if (validateText(edtAppUsername, inputUserName) && validateEmail(edtAppUserEmail, inputUserEmail))
                    updateUser();
                break;
        }
    }
}
