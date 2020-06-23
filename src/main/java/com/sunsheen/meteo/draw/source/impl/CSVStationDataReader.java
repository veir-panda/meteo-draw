package com.sunsheen.meteo.draw.source.impl;

import com.sunsheen.meteo.draw.source.StationDataReader;
import org.meteoinfo.data.StationData;
import org.meteoinfo.data.meteodata.MeteoDataInfo;

import java.io.File;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/19 17:35
 */
public class CSVStationDataReader implements StationDataReader {
    private String csvFilePath;

    public CSVStationDataReader(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public CSVStationDataReader(File csvFile) {
        this.csvFilePath = csvFile.getPath();
    }

    @Override
    public StationData read() {
        MeteoDataInfo meteoDataInfo = new MeteoDataInfo();
        meteoDataInfo.openLonLatData(csvFilePath);
        return meteoDataInfo.getStationData();
    }
}
