package com.example.androidretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private EditText mEmail, mPassword, mName, mSchool;
    private TextView toLoginText;
    private Button registerNow;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }


    private void initViews()
    {
        mDialog = new ProgressDialog(this);

        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mName = findViewById(R.id.reg_name);
        mSchool = findViewById(R.id.reg_school);
        toLoginText = findViewById(R.id.to_login_text);
        registerNow = findViewById(R.id.sign_up_button);

        registerNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                verifyInputs();

            }
        });

        toLoginText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }



    private void verifyInputs()
    {
        mDialog.setTitle("Register");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        String email= mEmail.getText().toString().trim();
        String password= mPassword.getText().toString().trim();
        String name= mName.getText().toString().trim();
        String school= mSchool.getText().toString().trim();

        if (!email.isEmpty())
        {
            if (!password.isEmpty())
            {
                if (!name.isEmpty())
                {
                    if (!school.isEmpty())
                    {
                       if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
                       {
                           if (mPassword.length() > 6)
                           {
                               registerUser(email, password,name,school);
                           }
                           else
                           {
                               mDialog.dismiss();
                               mPassword.setError("Password should be atleast 6 characters long");
                           }
                       }
                       else
                       {
                           mDialog.dismiss();
                           mEmail.setError("Incorrect Email format");
                           mEmail.requestFocus();
                       }
                    }
                    else
                    {
                        mDialog.dismiss();
                        mSchool.setError("School is required");
                        mSchool.requestFocus();
                    }
                }
                else
                {
                    mDialog.dismiss();
                    mName.setError("Name is required");
                    mName.requestFocus();
                }
            }
            else
            {
                mDialog.dismiss();
                mPassword.setError("Password is required");
                mPassword.requestFocus();
            }
        }
        else
        {
            mDialog.dismiss();
            mEmail.setError("Email is required");
            mEmail.requestFocus();
        }
    }

    private void registerUser(final String email, String password, String name, String school)
    {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().createUser(email,password,name,school);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                String result = null;

                   try
                   {
                       if (response.code() == 201)
                       {
                           result = response.body().string();
                       }
                       else
                       {
                           result = response.errorBody().string();
                       }

                   }
                   catch (IOException e)
                   {
                       e.printStackTrace();
                   }

                   if (result != null)
                   {
                       try
                       {
                           mDialog.dismiss();
                           JSONObject jsonObject = new JSONObject(result);

                           Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                       }
                       catch (JSONException e)
                       {
                           mDialog.dismiss();
                           e.printStackTrace();
                       }

                   }

               }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                mDialog.dismiss();
                String error = t.getMessage();
                Toast.makeText(MainActivity.this,error , Toast.LENGTH_LONG).show();

            }
        });
    }
}
