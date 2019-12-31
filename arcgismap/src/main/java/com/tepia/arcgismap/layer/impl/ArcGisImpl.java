package com.tepia.arcgismap.layer.impl;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.tepia.arcgismap.layer.core.IArcGis;

import java.util.Collection;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public class ArcGisImpl implements IArcGis {

    private ArcGISMap arcGISMap;

    public ArcGisImpl(){
        this.arcGISMap = new ArcGISMap();
    }

    @Override
    public ArcGISMap getArcGISMap() {
        return arcGISMap;
    }

    @Override
    public boolean addOperationalLayer(Layer layer) {
        return getBaseLayers().add(layer);
    }

    @Override
    public boolean addOperationalLayers(Collection<? extends Layer> layers) {
        return getBaseLayers().addAll(layers);
    }

    @Override
    public Basemap getBasemap() {
        return arcGISMap.getBasemap();
    }

    @Override
    public void setBasemap(Basemap basemap) {
        arcGISMap.setBasemap(basemap);
    }

    @Override
    public LayerList getBaseLayers() {
        return getBasemap().getBaseLayers();
    }


}
