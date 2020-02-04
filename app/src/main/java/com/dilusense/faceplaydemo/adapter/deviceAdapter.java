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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dilusense.faceplaydemo.R;

import java.util.List;

/**
 * 设备列表适配器
 */
public class deviceAdapter extends BaseAdapter{

    private List<ScanResult> mModels;
    private Context mCtx;
    private WifiScanAdapterItemClickListener cracl;
    public deviceAdapter(Context ctx, List<ScanResult> mModels, WifiScanAdapterItemClickListener cracl) {
        this.mModels = mModels;
        this.mCtx = ctx;
        this.cracl = cracl;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.layout_wifi_result_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ScanResult scanResult = mModels.get(position);
        Log.i("WifiDemo", scanResult.SSID);
        holder.tvName.setText(scanResult.SSID);
//        holder.tvName.setText("\n地址" + scanResult.BSSID + "\n设备名字" + scanResult.SSID +
//                "\n加密方式" + scanResult.capabilities + "\n接入频率" + scanResult.frequency + "\n信号强度" +
//                scanResult.level + "\n");
        holder.wifi_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cracl.onClick(scanResult);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        LinearLayout wifi_item;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            wifi_item = (LinearLayout) view.findViewById(R.id.wifi_item);
        }
    }
}
