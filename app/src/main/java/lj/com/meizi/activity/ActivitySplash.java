package lj.com.meizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import lj.com.meizi.R;
import me.wangyuwei.particleview.ParticleView;

public class ActivitySplash extends ActivityMain {

    TextView textView;

    ImageView imageView;
    ParticleView mParticleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.icon);
        mParticleView = (ParticleView) findViewById(R.id.mParticleView);
        mParticleView.startAnim();
        mParticleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ActivitySplash.this, MainActivity.class));
                        finish();
                    }
                }, 2000);
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };


}
