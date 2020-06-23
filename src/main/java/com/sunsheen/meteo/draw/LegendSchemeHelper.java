package com.sunsheen.meteo.draw;

import com.sunsheen.meteo.draw.color.ColorCode;
import com.sunsheen.meteo.draw.color.Reader;
import org.meteoinfo.legend.ColorBreak;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.LegendType;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.shape.ShapeTypes;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/19 16:15
 */
public class LegendSchemeHelper {
    private InputStream is;

    public LegendSchemeHelper(String colorCodeConfigPath){
        try {
            this.is = new FileInputStream(colorCodeConfigPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public LegendSchemeHelper(InputStream is) {
        this.is = is;
    }

    public LegendScheme getLegendSchemeByAlgType(String colorName){
        ColorCode cc = Reader.ReadColorCode(is,
                colorName);
        double[] cnlevs = cc.getValues();
        Color[] colors = cc.getColors();

        List<ColorBreak> breaks = new ArrayList<>();
        LegendScheme legendScheme = new LegendScheme();
        for (int i = 0; i < colors.length; i++) {
            breaks.add(generateColorBreak(String.valueOf(cnlevs[i]), cnlevs[i], cnlevs[i+1], colors[i]));
        }
        legendScheme.setMinValue(cnlevs[0]);
        legendScheme.setMaxValue(cnlevs[cnlevs.length - 1]);
        legendScheme.setLegendBreaks(breaks);
        legendScheme.setShapeType(ShapeTypes.Image);
        legendScheme.setLegendType(LegendType.GraduatedColor);
        legendScheme.setFieldName("图例");
        legendScheme.setUndefValue(999999);
        return legendScheme;
    }

    private ColorBreak generateColorBreak(String level, double start, double end, Color color){
        PolygonBreak cb = new PolygonBreak();
        cb.setCaption(level);
        cb.setStartValue(start);
        cb.setEndValue(end);
        cb.setColor(color);
        cb.setNoData(false);
        cb.setDrawOutline(false);
        cb.setDrawFill(false);
        return cb;
    }
}
