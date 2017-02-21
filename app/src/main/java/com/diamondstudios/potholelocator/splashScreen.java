package com.diamondstudios.potholelocator;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import static java.lang.Thread.sleep;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //we are wasting the users time here. but this is for testing. should be removed later on
        //SystemClock.sleep(1000);
        //intent to start the main activity
        //Intent intent = new Intent(this, MainActivity.class);
       // startActivity(intent);
       // finish(); //finish this activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //TODO: we need to use threads...

  /*      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                Intent mapIntent = new Intent(getApplicationContext(), mainMapsActivity.class);
                startActivity(mapIntent);
                finish(); //finish this activity
            }
        },DELAY_LENGTH);*/
    }

    public void viewMap(View view) {

        Intent intent = new Intent(this, mainMapsActivity.class);
        startActivity(intent);
    }

    public void newPt (View view){
        //nothing
    }

}
