package lj.com.meizi.activity;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {
    public static String imageUrl = "http://gank.io/api/data/福利/";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
