package com.sunsheen.meteo.draw;

import com.sunsheen.meteo.draw.source.StationDataReader;
import com.sunsheen.meteo.draw.source.impl.CSVStationDataReader;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.StationData;
import org.meteoinfo.legend.LegendScheme;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/19 16:53
 */
public class MeteoColorFigureTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
//        File file = new File(MeteoColorFigure.class.getClassLoader().getResource("1.csv").getFile());
        File file = new File("D:\\workspace\\pycharm\\cipas-alg\\out\\1.csv");
        /*StationData stationData = new StationData();
        try (BufferedReader reader = new BufferedReader(new FileReader(file));){
            String line = reader.readLine();
            if (line == null){
                throw new RuntimeException("CSV文件第一行（标题行）必须存在");
            }
            while ((line = reader.readLine()) != null){
                String[] cols = line.split("(, *)+");
                String stationId = cols[0];
                double lon = Double.parseDouble(cols[1]);
                double lat = Double.parseDouble(cols[2]);
                double value = Double.parseDouble(cols[3]);
                stationData.addData(stationId, lon, lat, value);
            }
        }*/
        StationDataReader reader = new CSVStationDataReader(file);
        StationData stationData = reader.read();
        stationData.missingValue = 999999;

//        colorCodeConfigPath = "D:/workspace/hearken_ee/WEBCIPAS/resources/MeteoAnalysis/algorithm/src/com/sunsheen/meteo/analyse/product/data/LegendConfig.xml";
//        String shpFile = "D:\\workspace\\veir\\java\\meteo-draw\\src\\main\\resources\\shp\\china\\全国界面.shp";
        String shpFile = "shp/china/全国界面.shp";
        URL url = MeteoColorFigure.class.getClassLoader().getResource(shpFile);

        ColorFigureSetting setting = new ColorFigureSetting();
//        setting.setShowLabel(true);
//        setting.setDrawRectangle(true);
        setting.setTitle("全国降水量实况图");
        setting.setSubTitle("2020-06-17日 00时");
        MeteoColorFigure meteoColorFigure = new MeteoColorFigure(new File(url.toURI()).getPath(), setting);
        GridData gridData = meteoColorFigure.interpolation(stationData);

        InputStream is = MeteoColorFigure.class.getClassLoader().getResourceAsStream("static/LegendConfig.xml");
        LegendScheme scheme = new LegendSchemeHelper(is).getLegendSchemeByAlgType("tem");
        meteoColorFigure.draw(gridData, scheme, "out/tem1.png");
    }
}
