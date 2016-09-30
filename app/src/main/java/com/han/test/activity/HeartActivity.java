package com.han.test.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.han.test.R;
import com.han.test.widget.HeartView;
import com.han.test.widget.RiseLove;

/**
 * Created by hdz on 16/9/29.
 */
public class HeartActivity extends Activity {



    private HeartView hv;
    private RiseLove rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        hv = (HeartView) findViewById(R.id.heart);
        heartbeatAnima();
        hv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showLove();
            }
        });

        rl = (RiseLove) findViewById(R.id.rise_love);

    }

    private void heartbeatAnima() {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(hv, "scaleX", 1.0f, 1.5f);
        anim1.setRepeatCount(-1);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(hv, "scaleY", 1.0f, 1.5f);
        anim2.setRepeatCount(-1);

        AnimatorSet set = new AnimatorSet();
        set.play(anim1).with(anim2);
        set.setDuration(2000);
        set.start();

        anim1.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                showLove();
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                showLove();
            }

            @Override
            public void onAnimationEnd(Animator arg0) {

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
            }
        });
    }

    private void showLove() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                rl.addHeart();
            }
        });
    }


}
