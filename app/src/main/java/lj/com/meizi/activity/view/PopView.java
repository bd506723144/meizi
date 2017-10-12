package lj.com.meizi.activity.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.facebook.drawee.view.SimpleDraweeView;

import lj.com.meizi.R;

/**
 * Created by LJ on 2016/10/7.
 */
public class PopView extends PopupWindow {
    Context context;
    View conentView;
    SimpleDraweeView img;


    public PopView(Activity context, int layout) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(layout, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(w);
        this.setHeight(LayoutParams.WRAP_CONTENT);// 设置SelectPicPopupWindow弹出窗体的高

        this.setFocusable(true); // 设置SelectPicPopupWindow弹出窗体可点击
        this.setOutsideTouchable(true);
        this.update(); // 刷新状态
        ColorDrawable dw = new ColorDrawable(); // 实例化一个ColorDrawable颜色为半透明
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        img = (SimpleDraweeView) conentView.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismisss();
            }
        });

        conentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    public void showPopupWindow(View parent, String url, int showGravity) {
        if (!this.isShowing()) {
            img.setImageURI(url);
            this.showAtLocation(parent, showGravity, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void dismisss() {
        this.dismiss();
    }
}
