package lj.com.meizi.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lj.com.meizi.R;
import lj.com.meizi.activity.been.ItemData;

/**
 * Created by LJ on 2016/9/9.
 */
public class RecvAdapter extends RecyclerView.Adapter<RecvAdapter.MyViewHolder> implements View.OnLongClickListener {
    ArrayList<ItemData> itemDatas = new ArrayList<>();
    Context mContext;
    private List<Integer> mHeight;
    private int[] heith = {200, 300, 400, 500, 600, 260, 360, 460};
    private MyOnLongClickListener mOnlongLis = null;

    public interface MyOnLongClickListener {
        void setOnItemLongClick(View view, String tag);
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnlongLis != null) {
            //注意这里使用getTag方法获取数据
            mOnlongLis.setOnItemLongClick(view, (String) view.getTag());
        }
        return false;
    }

    public void setOnLongClickListener(MyOnLongClickListener listener) {
        this.mOnlongLis = listener;
    }


    public RecvAdapter(Context context, ArrayList<ItemData> itemDatass) {
        this.mContext = context;
        this.itemDatas = itemDatass;
        mHeight = new ArrayList<>();
        for (int i = 0; i < itemDatas.size(); i++) {
            mHeight.add(200 + (heith[new Random().nextInt(7) + 1]));
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);
        inflate.setOnLongClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = mHeight.get(position);
        holder.imageView.setLayoutParams(layoutParams);

        holder.imageView.setImageURI(itemDatas.get(position).getUrl());
        holder.itemView.setTag(String.valueOf(holder.getLayoutPosition()));

    }

    public void addList(List<ItemData> data) {
        for (int i = 0; i < data.size(); i++) {
            mHeight.add(200 + (heith[new Random().nextInt(7) + 1]));
        }
        itemDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void removeDate(int pos) {
        itemDatas.remove(pos);
        notifyItemRemoved(pos);
    }


    @Override
    public int getItemCount() {
        return itemDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView imageView;
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.title);
            imageView = (SimpleDraweeView) view.findViewById(R.id.img);

        }
    }
}
