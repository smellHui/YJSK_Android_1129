package com.tepia.arcgismap.layer.poly;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.tepia.arcgismap.layer.MapLayout;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.core.IPoly;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.esri.arcgisruntime.loadable.LoadStatus.LOADED;

/**
 * Author:xch
 * Date:2019/7/26
 * Description:折线，多边形绘制类。
 */
public class PolyPlotter implements IPoly {

    private static final String markImgUri = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/Recreation/FeatureServer/0/images/e82f744ebb069bb35b234b3fea46deae";
    private Context mContext;
    private IMap map;
    private SpatialReference spatialReference;
    private GraphicsOverlay mGraphicsOverlay;

    private float pictureWidth = 18f;
    private float pictureHeight = 18f;

    private int polyColor = Color.BLUE;
    private float polyWidth = 3.0f;
    private SimpleLineSymbol.Style style = SimpleLineSymbol.Style.SOLID;

    public PolyPlotter(IMap map) {
        this.mContext = map.getContext();
        this.spatialReference = map.getSpatialReference();
        this.mGraphicsOverlay = new GraphicsOverlay();
        map.getGraphicsOverlays().add(mGraphicsOverlay);
    }

    public GraphicsOverlay getmGraphicsOverlay() {
        return mGraphicsOverlay;
    }

    @Override
    public void createPicturePointGraphics(Point point) {
        this.createPicturePointGraphics(point, markImgUri);
    }

    @Override
    public void createPicturePointGraphics(Point point, @DrawableRes int imgPath) {
        this.createPicturePointGraphics(point, imgPath, 18, 18);
    }

    @Override
    public void createPicturePointGraphics(Point point, @DrawableRes int imgPath, float width, float height) {
        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, imgPath);
            if (bitmapDrawable == null) return;
            ListenableFuture<PictureMarkerSymbol> pictureMarkerSymbolListenableFuture = PictureMarkerSymbol.createAsync(bitmapDrawable);
            PictureMarkerSymbol pointSymbol = pictureMarkerSymbolListenableFuture.get();
            pointSymbol.setWidth(width);
            pointSymbol.setHeight(height);
            //可以设置图片的偏移，使图片符号的基点与几何点对齐
            pointSymbol.setOffsetY(11);
            pictureMarkerSymbolListenableFuture.addDoneListener(() -> {
                Graphic pointGraphic = new Graphic(point, pointSymbol);
                mGraphicsOverlay.getGraphics().add(pointGraphic);
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPicturePointGraphics(Point point, String url) {
        this.createPicturePointGraphics(point, url, pictureWidth, pictureHeight);
    }

    @Override
    public void createPicturePointGraphics(Point point, String url, float width, float height) {
        PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(url);
        pointSymbol.setHeight(height);
        pointSymbol.setWidth(width);
        pointSymbol.loadAsync();
        pointSymbol.addDoneLoadingListener(() -> {
            if (pointSymbol.getLoadStatus() == LOADED) {
                Graphic pointGraphic = new Graphic(point, pointSymbol);
                mGraphicsOverlay.getGraphics().add(pointGraphic);
            } else {
                //TODO 待处理
            }
        });
    }

    /**
     * 划点
     *
     * @param point
     */
    @Override
    public void createSimplePointGraphics(Point point) {
        this.createSimplePointGraphics(point, SimpleMarkerSymbol.Style.CIRCLE, Color.rgb(226, 119, 40), 10.0f);
    }

    @Override
    public void createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, int color, float size) {
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(style, color, size);
        pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic pointGraphic = new Graphic(point, pointSymbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    @Override
    public void createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, Map<String, Object> attributes, int color, float size) {
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(style, color, size);
        pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic pointGraphic;
        if (attributes == null) {
            pointGraphic = new Graphic(point, pointSymbol);
        } else {
            pointGraphic = new Graphic(point, attributes, pointSymbol);
        }
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    /**
     * 划折线
     *
     * @param points
     */
    @Override
    public void createPolylineGraphics(Iterable<Point> points) {
        this.createPolylineGraphics(points, style, polyColor, polyWidth);
    }

    @Override
    public void createPolylineGraphics(Iterable<Point> points, int color) {
        this.createPolylineGraphics(points, style, color, polyWidth);
    }

    @Override
    public void createPolylineGraphics(Iterable<Point> points, float width) {
        this.createPolylineGraphics(points, style, polyColor, width);
    }

    @Override
    public void createPolylineGraphics(Iterable<Point> points, int color, float width) {
        this.createPolylineGraphics(points, style, color, width);
    }

    @Override
    public void createPolylineGraphics(Iterable<Point> points, SimpleLineSymbol.Style style, int color, float width) {
        PointCollection polylinePoints = new PointCollection(points, this.spatialReference);
        Polyline polyline = new Polyline(polylinePoints);
        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(style, color, width);
        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
        mGraphicsOverlay.getGraphics().add(polylineGraphic);
    }

    /**
     * 划多边形
     *
     * @param points
     */
    @Override
    public void createPolygonGraphics(Iterable<Point> points) {
        PointCollection polygonPoints = new PointCollection(points, this.spatialReference);
        Polygon polygon = new Polygon(polygonPoints);
        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.rgb(226, 119, 40),
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic polygonGraphic = new Graphic(polygon, polygonSymbol);
        mGraphicsOverlay.getGraphics().add(polygonGraphic);
    }

}
