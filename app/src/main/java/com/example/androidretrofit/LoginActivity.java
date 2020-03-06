package com.example.androidretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidretrofit.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    private EditText userEmail, userPassword;
    private TextView toRegister;
    private Button loginBtn;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }



    private void initViews()
    {
        mDialog = new ProgressDialog(this);

        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        toRegister = findViewById(R.id.to_register);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                verifyUserInputs();

            }
        });

        toRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }


    private void verifyUserInputs()
    {
        mDialog.setTitle("Login");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();


        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (!email.isEmpty())
        {
            if (!password.isEmpty())
            {
                loginUser(email,password);
            }
            else
            {
                mDialog.dismiss();

                userPassword.setError("Password is required");
                userPassword.requestFocus();
            }
        }
        else
        {
            mDialog.dismiss();

            userEmail.setError("Email is required");
            userEmail.requestFocus();
        }
    }



    private void loginUser(String email, String password)
    {

        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().loginUser(email, password);
        call.enqueue(new Callback<LoginResponse>()
        {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
            {
               LoginResponse loginResponse = response.body();

               if (!loginResponse.isError())
               {
                   mDialog.dismiss();
                   Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
               }
               else
               {
                   mDialog.dismiss();
                   Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
               }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t)
            {
                mDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
