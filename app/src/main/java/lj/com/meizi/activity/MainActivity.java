package lj.com.meizi.activity;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import lj.com.meizi.R;
import lj.com.meizi.activity.adapter.RecvAdapter;
import lj.com.meizi.activity.been.ItemData;
import lj.com.meizi.activity.view.PopView;
import lj.com.meizi.activity.view.SpacesItemDecoration;
import me.wangyuwei.flipshare.FlipShareView;
import me.wangyuwei.flipshare.ShareItem;

public class MainActivity extends ActivityMain
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private ArrayList<ItemData> data = new ArrayList<>();
    private ArrayList<ItemData> datas = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private static int index = 10;
    private static int indexs = 1;
    FlipShareView share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                createPop(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initView();

    }

    private void toa(String str, View view) {
        Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    FlipShareView.OnFlipClickListener lis = new FlipShareView.OnFlipClickListener() {
        @Override
        public void onItemClick(int position) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                    toa("未找到相关账号", toolbar);
                    break;
                default:

                    break;
            }
        }

        @Override
        public void dismiss() {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        toa("请正确使用 观看图片姿势  重压图片", toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    RecyclerView mainView;
    RecvAdapter adapter;
    ProgressDialog progressDialog;

    private void initView() {
        mainView = (RecyclerView) findViewById(R.id.mainView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        toa("客官，刷不出来了", toolbar);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });
        mainView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mainView.addItemDecoration(new SpacesItemDecoration(3));
        mainView.addOnScrollListener(scrol);
        callApi();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中。。。");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    RecyclerView.OnScrollListener scrol = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //判断是否滚动到最后一个子View
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!flag) {
                    callApi();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private boolean flag = false;

    private void callApi() {
        flag = true;
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder().url(App.imageUrl + String.valueOf(index) + "/" + String.valueOf(indexs)).build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
                         @Override
                         public void onFailure(Request request, IOException e) {
                             progressDialog.dismiss();
                             cloase();
                             toa("人品太差 加载不出来", mainView);
                             flag = false;
                         }

                         @Override
                         public void onResponse(final Response response) throws IOException {
                             cloase();
                             progressDialog.dismiss();
                             String htmlStr = response.body().string();
                             try {
                                 JSONObject jsonObject = new JSONObject(htmlStr);
                                 boolean flag = (boolean) jsonObject.get("error");
                                 if (!flag) {
                                     JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                                     if (null != jsonArray && jsonArray.length() > 1) {
                                         indexs++;
                                         for (int i = 0; i < jsonArray.length(); i++) {
                                             JSONObject js = jsonArray.getJSONObject(i);
                                             ItemData ite = new ItemData();
                                             ite.setTitle(js.getString("publishedAt"));
                                             ite.setUrl(js.getString("url"));
                                             if (null != adapter) {
                                                 datas.add(ite);
                                             } else {
                                                 data.add(ite);
                                             }
                                         }
                                     } else {
                                         toa("木有了", toolbar);
                                     }
                                     if (null == adapter) {
                                         ha.sendEmptyMessage(1);
                                     } else {
                                         ha.sendEmptyMessage(2);
                                     }
                                 } else {
                                     toa("木有了", toolbar);
                                 }
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                         }
                     }
        );
    }

    private void cloase() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private int indexss;
    Handler ha = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case 1:
                    adapter = new RecvAdapter(getApplicationContext(), data);
                    mainView.setAdapter(adapter);
                    adapter.setOnLongClickListener(new RecvAdapter.MyOnLongClickListener() {
                        @Override
                        public void setOnItemLongClick(View view, String tag) {
                            indexss = Integer.decode(tag);
                            showPop(data.get(indexss).getUrl());
                        }
                    });
                    break;
                case 2:
                    adapter.addList(datas);
                    datas.clear();
                    break;
            }
            flag = false;
        }
    };

    PopView pop;

    private void showPop(String url) {
        if (null == pop) {
            pop = new PopView(this, R.layout.pop);
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        pop.showPopupWindow(findViewById(R.id.toolbar), url, Gravity.TOP);//如果图片url为空则不能分享朋友圈
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }


    private void createPop(View view) {
        share = new FlipShareView.Builder(MainActivity.this, view)
                .addItem(new ShareItem("新浪", Color.WHITE, 0xff43549C, BitmapFactory.decodeResource(getResources(), R.drawable.sina)))
                .addItem(new ShareItem("微信", Color.WHITE, 0xff4999F0, BitmapFactory.decodeResource(getResources(), R.drawable.weixin)))
                .addItem(new ShareItem("QQ", Color.WHITE, getResources().getColor(R.color.colorAccent), BitmapFactory.decodeResource(getResources(), R.drawable.qq)))
                .addItem(new ShareItem("自定义模式", Color.WHITE, Color.BLACK, BitmapFactory.decodeResource(getResources(), R.drawable.splash_icon)))
                .setBackgroundColor(0x60000000)
                .setItemDuration(500)
                .setSeparateLineColor(0x30000000)
                .setAnimType(FlipShareView.TYPE_SLIDE)
                .create();
        share.setOnFlipClickListener(lis);
    }
}
