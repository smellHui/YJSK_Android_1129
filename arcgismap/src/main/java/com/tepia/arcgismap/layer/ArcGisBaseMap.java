package com.tepia.arcgismap.layer;

import android.content.Context;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.tepia.arcgismap.googlemap.GoogleTiteLayer;
import com.tepia.arcgismap.layer.core.IArcGis;
import com.tepia.arcgismap.layer.impl.ArcGisImpl;
import com.tepia.arcgismap.listener.MapLoadingListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public class ArcGisBaseMap {

    private Context context;
    private MapView mapView;
    private IArcGis map;
    private MapLoadingListener listener = null;

    public ArcGisBaseMap(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        map = new ArcGisImpl();
        this.mapView.setMap(map.getArcGISMap());

        //去掉水印
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud7416273699,none,4N5X0H4AH7AH6XCFK036");
        this.setAttributionTextVisible(false);
    }

    public void setAttributionTextVisible(boolean visible) {
        this.mapView.setAttributionTextVisible(visible);
    }

    public void setMapLoadingListener(MapLoadingListener listener) {
        this.listener = listener;
    }

    public void setBaseMap(Layer layer) {
        layer.loadAsync();
        final Layer finalLayer = layer;
        layer.addDoneLoadingListener(() -> {
            if (finalLayer.getLoadStatus() == LoadStatus.LOADED) {
                map.setBasemap(new Basemap(finalLayer));
                if (listener != null) {
                    listener.loadingSuccess();
                }
            } else {
                if (listener != null) {
                    listener.loadingFailed(finalLayer.getLoadStatus());
                }
            }
        });
    }

    public void addOperationalLayer(Layer layer) {
        map.addOperationalLayer(layer);
    }

    public void addOperationalLayers(Layer... layers) {
        if (layers == null || layers.length == 0) return;
        map.addOperationalLayers(Arrays.asList(layers));
    }


}
