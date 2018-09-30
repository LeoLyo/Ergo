package com.washedup.anagnosti.ergo.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.washedup.anagnosti.ergo.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends Activity {

    public static final String EMAIL_KEY = "Email";
    public static final String FIRST_NAME_KEY = "First_Name";
    public static final String LAST_NAME_KEY = "Last_Name";
    public static final String ADDRESS_KEY = "Address";
    public static final String PHONE_NUMBER_KEY = "Phone_Number";
    public static final String PASSWORD_KEY = "Password";

    EditText register_et_email, register_et_first_name, register_et_last_name, register_et_address, register_et_phone_number, register_et_password, register_et_confirm_password;
    ProgressBar register_pb;
    Button register_btn_submit;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_et_email = findViewById(R.id.register_et_email);
        register_et_first_name = findViewById(R.id.register_et_first_name);
        register_et_last_name = findViewById(R.id.register_et_last_name);
        register_et_address = findViewById(R.id.register_et_address);
        register_et_phone_number = findViewById(R.id.register_et_phone_number);
        register_et_password = findViewById(R.id.register_et_pasword);
        register_et_confirm_password = findViewById(R.id.register_et_confirm_password);
        register_btn_submit = findViewById(R.id.register_btn_submit);
        register_pb = findViewById(R.id.register_pb);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);*/

        register_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = register_et_email.getText().toString().trim();
                final String firstName = register_et_first_name.getText().toString().trim();
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

                 firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        register_pb.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Just before unauthenticator", Toast.LENGTH_SHORT).show();
                        if(task.isSuccessful()){
                            registerUser(email,firstName,lastName,address,phoneNumber,password);

                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"This email is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });




               //firebaseAuth.signOut();


                /*firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        register_pb.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            final FirebaseUser current_user = firebaseAuth.getCurrentUser();
                            if (current_user != null) {
                                //Inputting user info into database
                                Map<String, Object> user = new HashMap<>();
                                user.put(EMAIL_KEY, email);
                                user.put(FIRST_NAME_KEY, firstName);
                                user.put(LAST_NAME_KEY, lastName);
                                user.put(ADDRESS_KEY, address);
                                user.put(PHONE_NUMBER_KEY, phoneNumber);
                                user.put(PASSWORD_KEY, password);

                                db.collection("users").document(current_user.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "User" + current_user.getUid() + "Registered successfully", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User successfully written in database!");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing user", e);
                                        Toast.makeText(getApplicationContext(), "User" + current_user.getUid() + "not registered." + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Toast.makeText(getApplicationContext(), "User Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });*/


            }
        });

    }

    private void registerUser(String email, String firstName, String lastName, String address, String phoneNumber, String password) {
        //Inputting user info into database
        Toast.makeText(getApplicationContext(), "Entered unauthenticator", Toast.LENGTH_SHORT).show();
        Map<String, Object> unauthenticatedUser = new HashMap<>();
        unauthenticatedUser.put(EMAIL_KEY,email);
        unauthenticatedUser.put(FIRST_NAME_KEY,firstName);
        unauthenticatedUser.put(LAST_NAME_KEY,lastName);
        unauthenticatedUser.put(ADDRESS_KEY,address);
        unauthenticatedUser.put(PHONE_NUMBER_KEY,phoneNumber);
        unauthenticatedUser.put(PASSWORD_KEY,password);
        FirebaseFirestore.setLoggingEnabled(true);

        Toast.makeText(getApplicationContext(),"Just before db.collection",Toast.LENGTH_SHORT).show();
        db.collection("UnauthenticatedUsers").document(email)
                .set(unauthenticatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "[INSIDE] You have successfully registered your account!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User successfully written in database!");
                        firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "[QQ]  Verification email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "[EE] Error in sending verification email. The entered email is not registered in the database.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "[INSIDE] Ops! An error has occurred while registering your account!", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error writing new user", e);
                    }
                });

                        /*db.collection("Users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG,"Error adding document",e);
                            }
                        });*/
        //Informing user that the registration was successful
        Toast.makeText(getApplicationContext(), "[LAST] User has been successfully registered!", Toast.LENGTH_SHORT).show();

    }
}
