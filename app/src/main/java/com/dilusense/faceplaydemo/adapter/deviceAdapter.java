package com.dilusense.faceplaydemo.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.wifi.ScanResult;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.jflavio1.wificonnector.WifiConnector;

import java.util.List;

/**
 * 设备列表适配器
 */
public class deviceAdapter extends BaseAdapter {

    private List<ScanResult> mModels;
    private Context mCtx;
    private WifiConnector wifiConnector;
    private WifiScanAdapterItemClickListener cracl;
    private boolean flog;
    private String ssid;

    public deviceAdapter(Context ctx, String ssid,WifiConnector wifiConnector, boolean flog, List<ScanResult> mModels, WifiScanAdapterItemClickListener cracl) {
        this.mModels = mModels;
        this.mCtx = ctx;
        this.cracl = cracl;
        this.wifiConnector = wifiConnector;
        this.flog = flog;
        this.ssid = ssid;
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = null; //禁用缓存机制
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.layout_wifi_result_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ScanResult scanResult = mModels.get(position);
        holder.tvName.setText(scanResult.SSID);
        if (flog) {
            if (ssid.equals(mModels.get(position).SSID)) {
                holder.lianjiejieguo.setVisibility(View.VISIBLE);
                holder.disconnent.setText("(点击断开)");
                mModels.set(0,scanResult);
            }
        }
        holder.wifi_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cracl.onClick(scanResult);
            }
        });
        return convertView;
    }
    public void changetShowDelImage(boolean flog) {
        this.flog=flog;
        notifyDataSetChanged();
    }
    static class ViewHolder {
        TextView tvName;
        TextView disconnent;
        ImageView lianjiejieguo;
        LinearLayout wifi_item;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            lianjiejieguo = (ImageView) view.findViewById(R.id.lianjiejieguo);
            wifi_item = (LinearLayout) view.findViewById(R.id.wifi_item);
            disconnent = (TextView) view.findViewById(R.id.disconnent);
        }
    }
}
