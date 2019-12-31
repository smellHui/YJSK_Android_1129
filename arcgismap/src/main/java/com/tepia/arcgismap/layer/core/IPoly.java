package com.tepia.arcgismap.layer.core;

import android.support.annotation.DrawableRes;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.Map;

/**
 * Author:xch
 * Date:2019/7/30
 * Description:
 */
public interface IPoly {

    GraphicsOverlay getmGraphicsOverlay();

    void createPicturePointGraphics(Point point);

    //通过APP资源创建picture marker symbol
    void createPicturePointGraphics(Point point, @DrawableRes int imgPath);

    void createPicturePointGraphics(Point point, @DrawableRes int imgPath, float width, float height);

    //放网络图标
    void createPicturePointGraphics(Point point, String url);

    void createPicturePointGraphics(Point point, String url, float width, float height);

    //放色块
    void createSimplePointGraphics(Point point);

    void createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, int color, float size);

    void createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, Map<String, Object> attributes, int color, float size);

    //划线
    void createPolylineGraphics(Iterable<Point> points);

    void createPolylineGraphics(Iterable<Point> points, int color);

    void createPolylineGraphics(Iterable<Point> points, float width);

    void createPolylineGraphics(Iterable<Point> points, int color, float width);

    void createPolylineGraphics(Iterable<Point> points, SimpleLineSymbol.Style style, int color, float width);

    void createPolygonGraphics(Iterable<Point> points);
}
