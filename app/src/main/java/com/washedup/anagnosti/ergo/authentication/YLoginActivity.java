package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.washedup.anagnosti.ergo.R;

public class YLoginActivity extends Activity implements View.OnClickListener{

    FirebaseAuth mAuth;

    private Handler handler;
    private Runnable runnable;

    private RelativeLayout log_in_rellay1, log_in_rellay2;

    private EditText log_in_et_email, log_in_et_password;
    private ProgressBar log_in_pb;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toast.makeText(this, "THIS IS CREATED", Toast.LENGTH_SHORT).show();
        log_in_et_email = findViewById(R.id.log_in_et_email);
        log_in_et_password = findViewById(R.id.log_in_et_password);
        log_in_pb = findViewById(R.id.log_in_pb);
        log_in_pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);
        findViewById(R.id.log_in_btn_sign_up).setOnClickListener(this);
        findViewById(R.id.log_in_btn_log_in).setOnClickListener(this);
        findViewById(R.id.log_in_btn_forgot_password).setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                log_in_rellay1.setVisibility(View.VISIBLE);
                log_in_rellay2.setVisibility(View.VISIBLE);
            }
        };

        log_in_rellay1 = findViewById(R.id.log_in_rellay1);
        log_in_rellay2 = findViewById(R.id.log_in_rellay2);

        handler.postDelayed(runnable, 1500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "THIS IS RESUMED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            //Toast.makeText(this, "NOT NULL USER", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, YHomeActivity.class));
            finish();

        }else{
            //Toast.makeText(this, "NULL USER HERE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.log_in_btn_sign_up:

                startActivity(new Intent(this,YRegisterActivity.class));
                finish();

                break;

            case R.id.log_in_btn_log_in:

                userLogin();

                break;

            case R.id.log_in_btn_forgot_password:

                startActivity(new Intent(this, YForgotPasswordActivity.class));
                finish();

                break;
        }
    }

    private void userLogin(){
        String email = log_in_et_email.getText().toString().trim();
        String password = log_in_et_password.getText().toString().trim();

        if (email.isEmpty()) {
            log_in_et_email.setError("Email is necessary");
            log_in_et_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            log_in_et_email.setError("Please enter a valid email");
            log_in_et_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            log_in_et_password.setError("Password is necessary");
            log_in_et_password.requestFocus();
            return;
        }
        if (password.length() < 7) {
            log_in_et_password.setError("Minimum length of password should be 7");
            log_in_et_password.requestFocus();
            return;
        }

        log_in_pb.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                log_in_pb.setVisibility(View.GONE);
                //Toast.makeText(YLoginActivity.this, "JEBO MATER VISE", Toast.LENGTH_SHORT).show();
                if(task.isSuccessful()){

                    Toast.makeText(YLoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(YLoginActivity.this,YHomeActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else {
                    //Toast.makeText(YLoginActivity.this, "QI", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
            
            
        });
    }
}
