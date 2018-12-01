package com.nanamare.mac.count_circle_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private boolean b = false;
    private CircleProgressView circleProgressView;
    private int[] res = new int[]{R.color.material_black, R.color.btn_negative_guide_popup, R.color.md_yellow500, R.color.main_theme_red,
            R.color.md_pink600, R.color.material_cyanA400, R.color.material_lightgreenA400, R.color.material_cyan700,
            R.color.md_amberA700, R.color.material_indigo800, R.color.md_tealA400, R.color.md_purpleA700};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleProgressView = findViewById(R.id.cp_view);

        circleProgressView.setMode(CircleProgressView.EXERCISE_MODE);
        circleProgressView.setExerciseCnt(5);
        circleProgressView.setExerciseTimeMax(60);
        circleProgressView.setExerciseTime(35);

        circleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!b) {
                    circleProgressView.setMode(CircleProgressView.REST_MODE);
                } else {
                    circleProgressView.setMode(CircleProgressView.EXERCISE_MODE);
                }
                b = !b;
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int cnt = 0;
            @Override
            public void run() {
                cnt++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circleProgressView.setExerciseCnt(cnt);
                        circleProgressView.setExerciseTime(cnt);

                        int idx;
                        idx = new Random().nextInt(res.length);
                        circleProgressView.setCircleLineColor(res[idx]);

                        idx = new Random().nextInt(res.length);
                        circleProgressView.setCircleBackgroundColor(res[idx]);

                        idx = new Random().nextInt(res.length);
                        circleProgressView.setExerciseCntColor(res[idx]);

                        circleProgressView.setCircleLineStrokeSize(15);
                    }
                });
            }
        }, 0, 1000);


    }
}
