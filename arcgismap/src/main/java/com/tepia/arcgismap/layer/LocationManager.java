package com.tepia.arcgismap.layer;

import android.content.Context;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Author:xch
 * Date:2019/7/25
 * Description:
 */
public class LocationManager {

    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

   public LocationManager(MapView mapView){
       this.mMapView = mapView;
       mLocationDisplay = mapView.getLocationDisplay();

   }


}
