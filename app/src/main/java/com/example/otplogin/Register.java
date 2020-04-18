package com.example.otplogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;
    private Button sendotpbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));
        editText = findViewById(R.id.editTextPhone);
        sendotpbtn = findViewById(R.id.sendoptbtn);
        sendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = editText.getText().toString().trim();
                if(number.isEmpty() || number.length()<10){
                    editText.setError("Please provide valid number");
                    editText.requestFocus();
                    return;
                }
                String phonenumber = "+" + code + number;
                Intent intent = new Intent(Register.this,Otp.class);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(Register.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
