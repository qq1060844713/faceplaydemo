/*
 * Created by Jose Flavio on 2/5/18 7:50 PM.
 * Copyright (c) 2017 JoseFlavio.
 * All rights reserved.
 */

package com.dilusense.faceplaydemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.jflavio1.wificonnector.WifiConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * WifiListRv
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since 5/2/17
 */
public class WifiListRvAdapter extends RecyclerView.Adapter<WifiListRvAdapter.WifiItem> {

    private List<ScanResult> scanResultList = new ArrayList<>();
    private WifiConnector wifiConnector;
    private WifiItemListener wifiItemListener;
    private String wifiName;
    private boolean isShow;
    private String ssid;
    private Context mCxt;
    public WifiListRvAdapter(Context mCxt,String wifiName,WifiConnector wifiConnector, WifiItemListener wifiItemListener) {
        this.wifiConnector = wifiConnector;
        this.wifiItemListener = wifiItemListener;
        this.wifiName = wifiName;
    }

    public void setScanResultList(List<ScanResult> scanResultList) {
        this.scanResultList = scanResultList;
        notifyDataSetChanged();
    }

    @Override
    public WifiItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WifiItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wifi_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final WifiItem holder, final int position) {
        holder.fill(scanResultList.get(position), wifiConnector.getCurrentWifiSSID());
        if (wifiName.equals(scanResultList.get(position).SSID)) {
            holder.wifiResult.setVisibility(View.VISIBLE);
            holder.wifiIntensity.setText("(点击断开)");
        }
//        if (wifiConnector.isConnectedToBSSID(scanResultList.get(position).BSSID)) {
//            holder.wifiResult.setVisibility(View.VISIBLE);
//            holder.wifiIntensity.setText("(点击断开)");
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiConnector.isConnectedToBSSID(scanResultList.get(position).BSSID)) {

                } else {
                    wifiItemListener.onWifiItemClicked(scanResultList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.scanResultList.size();
    }

    public void changetShowDelImage(String wifiName) {
        this.wifiName=wifiName;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(WifiItem holder) {
        super.onViewRecycled(holder);
    }

    static class WifiItem extends RecyclerView.ViewHolder {

        private TextView wifiName;
        private TextView wifiIntensity;
        private ImageView wifiResult;

        public WifiItem(View itemView) {
            super(itemView);
            wifiName = itemView.findViewById(R.id.tv_name);
            wifiIntensity = itemView.findViewById(R.id.disconnent);
            wifiResult = itemView.findViewById(R.id.lianjiejieguo);
        }

        @SuppressLint("SetTextI18n")
        public void fill(ScanResult scanResult, String currentSsid) {
            if (scanResult.SSID.equals(currentSsid)) {
//                wifiName.setTextColor(Color.GREEN);
            }
            wifiName.setText(scanResult.SSID);
//            wifiIntensity.setText(WifiManager.calculateSignalLevel(scanResult.level, 100) + "%");
        }

    }

    public interface WifiItemListener {
        void onWifiItemClicked(ScanResult scanResult);

    }

}
