package com.weilun.uniplugin_beLocation;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.weilun.uniplugin_beLocation.utils.NoticeUtils;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.feature.uniapp.ui.component.UniComponent;

/**
 * @author 李浩
 * @version 1.0
 * @description: 高德地图
 * @date 2023/4/12 16:45
 */
public class AmapViewModule extends UniComponent<MapView> {

    private MapView mapView;
    private List<Marker> markerList = new ArrayList<>();
    private AMap map;
    private Context mContext;

    public AmapViewModule(UniSDKInstance instance, AbsVContainer parent, AbsComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected MapView initComponentHostView(@NonNull Context context) {
        this.mContext = context;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mapView = new MapView(context);
        mapView.onCreate(null);
        mapView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        if (mapView != null) {
            if (map == null) {
                map = mapView.getMap();
            }
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
//        map.getUiSettings().setTiltGesturesEnabled(false);
        //map.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        map.clear();
        return mapView;
    }

    @UniJSMethod(uiThread = false)
    public void initMap() {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.clear();
        for (Marker item : markerList
        ) {
            if (item != null) {
                item.destroy();
            }
        }
        markerList.clear();
    }

    //    {
//        "driverLngLats": [
//        {
//            "name": "name",
//                "lngLat": [
//            117,
//                    31
//            ]
//        },
//        {
//            "name": "name",
//                "lngLats": [
//            117,
//                    31
//            ]
//        }
//    ],
//        "customerLngLat": {
//        "name":"name",
    //         "address":"address",
    //      "lngLat":[
    //        117,
//                31
//    ]
//        }
//   }
    @UniJSMethod(uiThread = false)
    public void drawMarker(String jsonLngLat) {
        if (mapView == null && map == null) {
            return;
        }
        map.clear();
        for (Marker item : markerList
        ) {
            if (item != null) {
                item.destroy();
            }
        }
        markerList.clear();
        //客户坐标
        LatLng latLng = null;
        if (jsonLngLat != null && !jsonLngLat.equals("")) {
            JSONObject jsonObject = JSONObject.parseObject(jsonLngLat);
            //绘制客户位置
            JSONObject customerLngLat = jsonObject.getJSONObject("customerLngLat");
            if (customerLngLat != null) {
                String name = customerLngLat.getString("name");
                String address = customerLngLat.getString("address");
                JSONArray lngLat = customerLngLat.getJSONArray("lngLat");
                if (lngLat != null && lngLat.size() > 1) {
                    latLng = new LatLng(lngLat.getDouble(1), lngLat.getDouble(0));
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(name));
//                    marker.setAnchor(lngLat.getFloat(1),lngLat.getFloat(0));
                    markerList.add(marker);
//                    marker.showInfoWindow();
                    map.setMyLocationEnabled(false);
                }
            }
            //绘制司机
            JSONArray driverLngLats = jsonObject.getJSONArray("driverLngLats");
            if (driverLngLats != null && driverLngLats.size() > 0) {
                for (int i = 0; i < driverLngLats.size(); i++) {
                    JSONObject jsonObjectd = driverLngLats.getJSONObject(i);
                    String name = jsonObjectd.getString("name");
                    JSONArray lngLat = jsonObjectd.getJSONArray("lngLats");
                    if (lngLat != null && lngLat.size() > 1) {
                        LatLng latLngDriver = new LatLng(lngLat.getDouble(1), lngLat.getDouble(0));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLngDriver);
                        markerOptions.title(name);
                        //计算客户和司机的位置
                        if (latLng != null) {
                            float  distance = AMapUtils.calculateLineDistance(latLng, latLngDriver);
                            if (distance>=1000){
                                float v1 = distance / 1000;
                                float v2 = Math.round(v1*100)/100f;
                                markerOptions.snippet("距离你："+v2+"千米");
                            } else {
                                float v = Math.round(distance * 100) / 100f;
                                markerOptions.snippet("距离你："+v+"米");
                            }
                        }
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(mContext.getResources(), R.drawable.cheliangweizhi)));
                        Marker marker = map.addMarker(markerOptions);
//                        marker.setAnchor(lngLat.getFloat(1),lngLat.getFloat(0));
                        markerList.add(marker);
                        marker.showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngDriver, 15));
                    }
                }
            }
        }
    }

    @UniJSMethod(uiThread = false)
    public void onResume() {
        onActivityResume();
    }

    @UniJSMethod(uiThread = false)
    public void onPause() {
        onActivityPause();
    }

    @UniJSMethod(uiThread = false)
    public void onDestroy() {
        onActivityDestroy();
    }

    @Override
    public void onActivityResume() {
        if (mapView != null) {
            //在activity执行onPause时执行mMapView.onResume ()，重新绘制加载地图
            mapView.onResume();
        }
        super.onActivityResume();
    }

    @Override
    public void onActivityPause() {
        if (mapView != null) {
            //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
            mapView.onPause();
        }
        super.onActivityPause();
    }

    @Override
    public void onActivityDestroy() {
        if (mapView != null) {
            //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
            mapView.onDestroy();
            mapView = null;
            map = null;
        }
        super.onActivityDestroy();
    }
}
