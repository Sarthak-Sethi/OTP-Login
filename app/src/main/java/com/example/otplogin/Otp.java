package com.example.otplogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    private String verificationid;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private Button signinbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        signinbtn = findViewById(R.id.buttonSignIn);
        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendverificationcode(phonenumber);
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString().trim();
                if ((code.isEmpty() || code.length() < 6)){
                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifycode(code);
            }
        });

    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Otp.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(Otp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendverificationcode(String phonenumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationid = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressBar.setVisibility(View.VISIBLE );
                verifycode(code);
            }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Otp.this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
