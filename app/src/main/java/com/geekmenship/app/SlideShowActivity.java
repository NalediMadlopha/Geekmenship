package com.geekmenship.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SlideShowActivity extends Activity {

    // Button declaration(s)
    private Button signUp;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        signUp = (Button) findViewById(R.id.signUpButton);
        signIn = (Button) findViewById(R.id.signInButton);

        signUp.setOnClickListener(mGlobal_OnClickLister);
        signIn.setOnClickListener(mGlobal_OnClickLister);
    }

    final View.OnClickListener mGlobal_OnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                startActivity(new Intent(SlideShowActivity.this, SignUpActivity.class));
                break;
            case R.id.signInButton:
                AlertDialog.Builder signInDialog = new AlertDialog.Builder(SlideShowActivity.this);
                final LayoutInflater inflater = SlideShowActivity.this.getLayoutInflater();

                signInDialog.setView(inflater.inflate(R.layout.dialog_sign_in, null))
                    .setPositiveButton(R.string.sign_In, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(SlideShowActivity.this, MainActivity.class));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                signInDialog.create();
                signInDialog.show();
                break;
        }
        }
    };
}
