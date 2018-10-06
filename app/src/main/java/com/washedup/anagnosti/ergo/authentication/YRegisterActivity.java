package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.washedup.anagnosti.ergo.R;

import java.util.HashMap;
import java.util.Map;

public class YRegisterActivity extends Activity implements View.OnClickListener{

    public static final String EMAIL_KEY = "email";
    public static final String FIRST_NAME_KEY = "firstName";
    public static final String NICKNAME_KEY = "nickname";
    public static final String LAST_NAME_KEY = "lastName";
    public static final String ADDRESS_KEY = "address";
    public static final String PHONE_NUMBER_KEY = "phoneNumber";
    public static final String PASSWORD_KEY = "password";

    EditText register_et_email, register_et_first_name, register_et_nickname, register_et_last_name, register_et_address, register_et_phone_number, register_et_password, register_et_confirm_password;
    ProgressBar register_pb;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_et_email = findViewById(R.id.register_et_email);
        register_et_first_name = findViewById(R.id.register_et_first_name);
        register_et_nickname = findViewById(R.id.register_et_nickname);
        register_et_last_name = findViewById(R.id.register_et_last_name);
        register_et_address = findViewById(R.id.register_et_address);
        register_et_phone_number = findViewById(R.id.register_et_phone_number);
        register_et_password = findViewById(R.id.register_et_pasword);
        register_et_confirm_password = findViewById(R.id.register_et_confirm_password);
        register_pb = findViewById(R.id.register_pb);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.register_btn_submit).setOnClickListener(this);
        findViewById(R.id.register_btn_back_to_login).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.register_btn_submit:

                registerUser();

                break;

            case R.id.register_btn_back_to_login:
                
                startActivity(new Intent(this, YLoginActivity.class));
                finish();

                break;
        }
    }

    private void registerUser() {
        final String email = register_et_email.getText().toString().trim();
        final String firstName = register_et_first_name.getText().toString().trim();
        final String nickname = register_et_nickname.getText().toString().trim();
        final String lastName = register_et_last_name.getText().toString().trim();
        final String address = register_et_address.getText().toString().trim();
        final String phoneNumber = register_et_phone_number.getText().toString().trim();
        final String password = register_et_password.getText().toString().trim();
        final String confirmPassword = register_et_confirm_password.getText().toString().trim();

        if (email.isEmpty()) {
            register_et_email.setError("Email is necessary");
            register_et_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_et_email.setError("Please enter a valid email");
            register_et_email.requestFocus();
            return;
        }
        if (firstName.isEmpty()) {
            register_et_first_name.setError("First name is necessary");
            register_et_first_name.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            register_et_last_name.setError("Last name is necessary");
            register_et_last_name.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            register_et_address.setError("Address is necessary");
            register_et_address.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()) {
            register_et_phone_number.setError("Phone number is necessary");
            register_et_phone_number.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            register_et_password.setError("Password is necessary");
            register_et_password.requestFocus();
            return;
        }
        if (password.length() < 7) {
            register_et_password.setError("Minimum length of password should be 7");
            register_et_password.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            register_et_confirm_password.setError("Password confirmation is necessary");
            register_et_confirm_password.requestFocus();
            return;
        }
        if (!confirmPassword.matches(password)) {
            register_et_confirm_password.setError("Password confirmation must match the password");
            register_et_confirm_password.requestFocus();
            return;
        }

        register_pb.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                register_pb.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    //Toast.makeText(YRegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    databaseUser(email,firstName,nickname,lastName,address,phoneNumber);
                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"This email is already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"[YERROR]: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
                

    }

    private void databaseUser(final String email, String firstName,String nickname, String lastName, String address, String phoneNumber) {
        Map<String, Object> unauthenticatedUser = new HashMap<>();
        unauthenticatedUser.put(EMAIL_KEY,email);
        unauthenticatedUser.put(FIRST_NAME_KEY,firstName);
        unauthenticatedUser.put(NICKNAME_KEY,nickname);
        unauthenticatedUser.put(LAST_NAME_KEY,lastName);
        unauthenticatedUser.put(ADDRESS_KEY,address);
        unauthenticatedUser.put(PHONE_NUMBER_KEY,phoneNumber);
        //unauthenticatedUser.put(PASSWORD_KEY,password);
        FirebaseFirestore.setLoggingEnabled(true);

        CollectionReference dbUnauthenticatedUsers = db.collection("Users");
        dbUnauthenticatedUsers.document(email).set(unauthenticatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(YRegisterActivity.this, "User successfully registered!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(YRegisterActivity.this,YHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(YRegisterActivity.this, "[YERROR]: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
