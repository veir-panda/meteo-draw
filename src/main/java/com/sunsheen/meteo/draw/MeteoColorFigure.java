package com.sunsheen.meteo.draw;

import com.sunsheen.meteo.draw.util.StringUtil;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.StationData;
import org.meteoinfo.data.mapdata.MapDataManage;
import org.meteoinfo.data.meteodata.DrawMeteoData;
import org.meteoinfo.data.meteodata.GridDataSetting;
import org.meteoinfo.geoprocess.analysis.InterpolationMethods;
import org.meteoinfo.geoprocess.analysis.InterpolationSetting;
import org.meteoinfo.layer.LabelSet;
import org.meteoinfo.layer.VectorLayer;
import org.meteoinfo.layout.*;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.MapFrame;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.map.MapView;
import org.meteoinfo.shape.Graphic;
import org.meteoinfo.shape.RectangleShape;

import javax.print.PrintException;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2019/11/26 9:44
 */
public class MeteoColorFigure {
    /**
     * 放大倍数
     */
    private int times = 1;
    /**
     * 图片分辨率
     */
    private int width = 1130;
    private int height = 891;

    private VectorLayer shapeVectorLayer;

    private ColorFigureSetting colorFigureSetting;


    public MeteoColorFigure(String shpFile) {
        this.colorFigureSetting = new ColorFigureSetting();
        init(shpFile);
    }
    public MeteoColorFigure(String shpFile, ColorFigureSetting colorFigureSetting) {
        this.colorFigureSetting = colorFigureSetting;
        init(shpFile);
    }

    private void init(String shpFile) {
        try {
            shapeVectorLayer = MapDataManage.readMapFile_ShapeFile(shpFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*Extent extent = shapeVectorLayer.getExtent();
        double left = extent.minX;
        double right = extent.maxX;
        double top = extent.maxY;
        double bottom = extent.minY;

        width = (int) ((right - left) * 1000);
        height = (int) ((top - bottom) * 1000);

        if (width > 3000) {
            double ratio = (double) (width + 0.1) / 3000;
            width = (int) (width / ratio);
            height = (int) (height / ratio);
        }

        if (height > 3000) {
            double ratio = (double) (height + 0.1) / 3000;
            width = (int) (width / ratio);
            height = (int) (height / ratio);
        }*/

        width = width * times;
        height = height * times;
    }

    private void check(String path) throws IOException {
        Path dir =Paths.get(path).getParent();
        if (Files.notExists(dir)){
            Files.createDirectories(dir);
        }
    }

    public void draw(GridData gridData, LegendScheme legendScheme, String imagePath) throws IOException {
        check(imagePath);

        MapLayout mapLayout = new MapLayout();
//        mapLayout.addText("全国降水量实况图", width / 2, 42, 26);
//        mapLayout.addText("2020-06-17日 00时", width / 2, 70, 18);
        if (StringUtil.isNotEmpty(colorFigureSetting.getTitle())){
            mapLayout.addText(colorFigureSetting.getTitle(), width / 2, 42, "宋体", 26);
        }
        if (StringUtil.isNotEmpty(colorFigureSetting.getSubTitle())){
            mapLayout.addText(colorFigureSetting.getSubTitle(), width / 2, 70, "宋体", 18);
        }

        Color color = colorFigureSetting.isBackgroundTransparent()? new Color(255, 255, 255, 0): new Color(255, 255, 255);
        mapLayout.setPageBackColor(color);
        //边界
        Rectangle _pageBounds = new Rectangle();
        _pageBounds.x = 0;
        _pageBounds.y = 0;
        _pageBounds.width = width;
        _pageBounds.height = height;
        mapLayout.setPageBounds(_pageBounds);

        LayoutMap layoutMap = mapLayout.getActiveLayoutMap();
        layoutMap.setDrawGridLine(false);
        layoutMap.setDrawNeatLine(false);
        layoutMap.setDrawGridLabel(false);
        layoutMap.setDrawGridTickLine(false);
        //创建mapView对象
        MapFrame mapFrame = mapLayout.getActiveMapFrame();

/*
        int cx = 50 * times;
        int cy = 30 * times;
        int cw = width - cx * 2 - 10;
        int ch = height - cy * 2;
*/
        int cx = 0;
        int cy = 0;
        int cw = width;
        int ch = height;

        mapFrame.setActive(true);
        mapFrame.setLayoutBounds(new Rectangle(cx, cy, cw, ch));

        MapView mapView = mapFrame.getMapView();


        //色标配置  图片类型判断
//        LegendScheme legendScheme = LegendManage.createLegendSchemeFromGridData(gridData, LegendType.GraduatedColor, ShapeTypes.Polygon);
        //使用格点数据创建等值面填充图
        VectorLayer shadedLayerGd = DrawMeteoData.createShadedLayer(gridData, legendScheme, "Station_ColorFigure_Layer", "Data", true);
        shadedLayerGd.setMaskout(true);
//        shadedLayerGd.setProjInfo();
        mapView.addLayer(shadedLayerGd);

        if (colorFigureSetting.isShowLabel()){
            //使用shp文件中的地理信息绘制标签
            LabelSet labelSet = shapeVectorLayer.getLabelSet();
            labelSet.setFieldName("NAME");
            labelSet.setLabelColor(Color.black);
            labelSet.setLabelFont(new Font("宋体", Font.PLAIN, 15 * times));
            shapeVectorLayer.addLabels();
        }
        //绘制地图框，形成地图
        PolygonBreak aPGB = (PolygonBreak) shapeVectorLayer.getLegendScheme().getLegendBreaks().get(0);
        aPGB.setDrawFill(false);
        aPGB.setDrawShape(true);
        aPGB.setDrawOutline(true);
        aPGB.setOutlineColor(Color.BLACK);
        aPGB.setOutlineSize(1 * times);
        //把地图层绘制到图片上
        mapView.addLayer(shapeVectorLayer);
        if (colorFigureSetting.isDrawRectangle()){
            //加边框
            RectangleShape rect = new RectangleShape(6, 6, width - 12, 0);
            PolygonBreak pb = new PolygonBreak();
            pb.setOutlineSize(1 * times);
            pb.setDrawFill(false);
            pb.setOutlineColor(Color.black);
            Graphic aGraphic = new Graphic(rect, pb);
            LayoutGraphic layoutGraphic = new LayoutGraphic(aGraphic, mapLayout);
            mapLayout.addElement(layoutGraphic);


            RectangleShape rect1 = new RectangleShape(6, 6, width - 12, height - 12);
            PolygonBreak pb1 = new PolygonBreak();
            pb1.setOutlineSize(1 * times);
            pb1.setDrawFill(false);
            pb1.setOutlineColor(Color.black);
            Graphic aGraphic1 = new Graphic(rect1, pb1);
            LayoutGraphic layoutGraphic1 = new LayoutGraphic(aGraphic1, mapLayout);
            mapLayout.addElement(layoutGraphic1);
        }


        //图例配置
//        int lendx = width * 112 / 136;
//        int lendy = height * 75 / 136;
//        int lendx = width - 120;
//        int lendy = 0;
        int lendx = 0;
        int lendy = height - 120;

        LayoutLegend aLegend = mapLayout.addLegend(lendx, lendy);
        aLegend.setLegendLayer(shadedLayerGd);
        aLegend.setFont(new Font("楷体", Font.PLAIN, 17 * times));
        aLegend.setLegendStyle(LegendStyles.Bar_Horizontal);
        aLegend.setTitle("图例");
        aLegend.setWidth(width);
        aLegend.setHeight(50);


        //开启//加入掩膜
        mapView.getMaskOut().setMask(true);
        mapView.getMaskOut().setMaskLayer(shapeVectorLayer.getLayerName());
//        mapView.zoomToExtent(shapeVectorLayer.getExtent());

        try {
            mapLayout.exportToPicture(imagePath);
        } catch (PrintException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 站点数据插值为格点数据
     * @param stationData 站点数据
     * @return 格点数据
     */
    public GridData interpolation(StationData stationData, InterpolationMethods interpolationMethods) {
        //格点配置
        GridDataSetting gridDataSetting = new GridDataSetting();
        gridDataSetting.dataExtent = shapeVectorLayer.getExtent();
        gridDataSetting.xNum = 223;
        gridDataSetting.yNum = 165;
        //站点插值配置
        InterpolationSetting interpolationSetting = new InterpolationSetting();
        interpolationSetting.setInterpolationMethod(interpolationMethods);
        interpolationSetting.setMinPointNum(3);
        interpolationSetting.setGridDataSetting(gridDataSetting);
        //站点插值成格点数据
        return stationData.interpolateData(interpolationSetting);
    }
    /**
     * 站点数据插值为格点数据, 使用IDW_Neighbors算法
     * @param stationData 站点数据
     * @return 格点数据
     */
    public GridData interpolation(StationData stationData) {
        return interpolation(stationData, InterpolationMethods.IDW_Neighbors);
    }



    public int getTimes() {
        return times;
    }

    public MeteoColorFigure setTimes(int times) {
        this.times = times;
        return this;
    }


}
