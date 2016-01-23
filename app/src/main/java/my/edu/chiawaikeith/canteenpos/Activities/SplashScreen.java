package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import my.edu.chiawaikeith.canteenpos.R;


public class SplashScreen extends Activity {

    private ProgressBar progressBar;
    int progressStatus = 0;
    TextView textView1, textView2;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //textView2 = (TextView) findViewById(R.id.load_per);
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 40)
                {
                    progressStatus += 1;
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            progressBar.setProgress(progressStatus);
                            //textView2.setText(progressStatus + "%");
                        }
                    });
                    try
                    {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (progressStatus==40)
                {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }).start();
    }}
