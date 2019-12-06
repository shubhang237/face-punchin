package com.example.student_attendance_app.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Model.SubjectAttendance;
import com.example.student_attendance_app.Student.Model.TokenData;
import com.example.student_attendance_app.Student.Model.credential;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;

import java.security.Permission;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText emailId;
    EditText password;
    EditText baseUrl;
    CheckBox showPassword;
    Button login;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailId = findViewById(R.id.emailID);
        password = findViewById(R.id.password);
        showPassword = findViewById(R.id.showPassword);
        login = findViewById(R.id.login);
        baseUrl = findViewById(R.id.baseUrl);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        checkPermission(123);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(SignInActivity.this);
                progressDoalog.setMessage("Signing In....");
                progressDoalog.show();
                final String base = baseUrl.getText().toString();
                GetDataService service = RetrofitClientInstance.getRetrofitInstance(base).create(GetDataService.class);
                Call<TokenData> call = service.getToken(new credential(emailId.getText().toString(),password.getText().toString()));
                Log.d("TAG",call.request().url().toString());
                call.enqueue(new Callback<TokenData>() {
                    @Override
                    public void onResponse(Call<TokenData> call, Response<TokenData> response) {
                        progressDoalog.dismiss();
                        try {
                            TokenData tokenData = response.body();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("access_token",tokenData.getToken());
                            editor.putString("username",tokenData.getUserData().getInfodata().getFirstName()+" "+tokenData.getUserData().getInfodata().getLastName());
                            editor.putString("email",tokenData.getUserData().getInfodata().getEmail());
                            editor.putString("role",tokenData.getType());
                            editor.putString("base_url",base);
                            editor.putString("mode","ageit");
                            editor.putBoolean("signedin",true);
                            editor.commit();
                            if(!tokenData.getType().equals("admin")){
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e){
                            progressDoalog.dismiss();
                            Toast.makeText(getBaseContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenData> call, Throwable t) {
                        progressDoalog.dismiss();
                    }
                });
            }
        });
    }

    public void checkPermission(int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(
                            SignInActivity.this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                           Manifest.permission.READ_EXTERNAL_STORAGE,
                                           Manifest.permission.CAMERA},
                            requestCode);
        }
    }
}
