package com.washedup.anagnosti.ergo.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.washedup.anagnosti.ergo.R;

public class LoginActivity extends Activity {

    private FirebaseAuth firebaseAuth;

    private Handler handler;
    private Runnable runnable;

    private RelativeLayout log_in_rellay1, log_in_rellay2;
    private EditText log_in_et_email, log_in_et_password;
    private Button log_in_btn_log_in, log_in_btn_sign_up, log_in_btn_forgot_password;
    private ProgressBar log_in_pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_in_et_email = findViewById(R.id.log_in_et_email);
        log_in_et_password = findViewById(R.id.log_in_et_password);
        log_in_btn_log_in = findViewById(R.id.log_in_btn_log_in);
        log_in_btn_sign_up = findViewById(R.id.log_in_btn_sign_up);
        log_in_btn_forgot_password = findViewById(R.id.log_in_btn_forgot_password);
        log_in_pb = findViewById(R.id.log_in_pb);

        //TESTING THE SCROLL FROM STACKOVERFLOW
       /* final View root = findViewById(R.id.log_in_content);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            Rect r = new Rect();
            {
                root.getWindowVisibleDisplayFrame(r);
            }
            @Override
            public void onGlobalLayout() {
                Rect r2 = new Rect();
                root.getWindowVisibleDisplayFrame(r2);
                int keyboardHeight = r.height()-r2.height();
                if(keyboardHeight>100){
                    root.scrollTo(0,keyboardHeight);
                }else{
                    root.scrollTo(0,0);
                }
            }
        });*/

        firebaseAuth = FirebaseAuth.getInstance();

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


        log_in_btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        log_in_pb.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        log_in_btn_sign_up.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        log_in_btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
