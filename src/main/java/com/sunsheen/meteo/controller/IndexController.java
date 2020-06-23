package com.sunsheen.meteo.controller;

import com.sunsheen.meteo.common.AlgType;
import com.sunsheen.meteo.common.ReturnT;
import com.sunsheen.meteo.component.FileUploadComponent;
import com.sunsheen.meteo.draw.ColorFigureSetting;
import com.sunsheen.meteo.draw.LegendSchemeHelper;
import com.sunsheen.meteo.draw.MeteoColorFigure;
import com.sunsheen.meteo.draw.source.StationDataReader;
import com.sunsheen.meteo.draw.source.impl.CSVStationDataReader;
import com.sunsheen.meteo.draw.util.StringUtil;
import com.sunsheen.meteo.listener.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.StationData;
import org.meteoinfo.legend.LegendScheme;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/20 15:06
 */
@Slf4j
@RequestMapping("/api/v1")
@RestController
public class IndexController {

    @Resource
    private FileUploadComponent uploadComponent;

    @Resource
    private SystemConfig config;


    @PostMapping("/draw")
    public ResponseEntity<?> draw(HttpServletRequest request,
                                  @RequestParam("attachment") MultipartFile file,
                                  @RequestParam Map<String, String> args){
        log.info("开始画图，参数:{}", args);
        if (file == null || file.isEmpty()){
            return buildMsg(ReturnT.fail("附件不能为空"));
        }
        if (args == null || args.isEmpty()){
            return buildMsg(ReturnT.fail("参数不能为空"));
        }
        String algTypeStr = args.get("algType");
        if (StringUtil.isEmpty(algTypeStr)){
            return buildMsg(ReturnT.fail("缺少参数:algType"));
        }

        String colorName = "";
        if("COMPOUND".equals(algTypeStr)){
            String outType = args.get("outType");
            String var = args.get("var");
            if (StringUtil.isEmpty(outType)){
                return buildMsg(ReturnT.fail("参数outType不能为空"));
            }
            if (StringUtil.isEmpty(var)){
                return buildMsg(ReturnT.fail("参数var不能为空"));
            }
            String[] varArr = var.split("_", 2);
            if(varArr.length != 2){
                return buildMsg(ReturnT.fail("参数var错误"));
            }
            if ("anomaly".equals(outType)){
                if (varArr[1].toLowerCase().contains("pre")){
                    colorName = AlgType.COMPOUND_PRE_DEPARTURE.getColorname();
                }else if (varArr[1].toLowerCase().contains("tem")){
                    colorName = AlgType.COMPOUND_TEM_DEPARTURE.getColorname();
                }
            }else{
                if (varArr[1].toLowerCase().contains("pre")){
                    colorName = AlgType.COMPOUND_PRE_ORIGINAL.getColorname();
                }else if (varArr[1].toLowerCase().contains("tem")){
                    colorName = AlgType.COMPOUND_TEM_ORIGINAL.getColorname();
                }
            }
        }else if("CORR".equals(algTypeStr)){
            colorName = AlgType.CORR.getColorname();
        }
        if (StringUtil.isEmpty(colorName)){
            throw new RuntimeException("没有找到合适的图例");
        }
        String title = args.get("title");
        String subTitle = args.get("subTitle");


        try {
            File csvFile = uploadComponent.save(file);
            StationDataReader reader = new CSVStationDataReader(csvFile);
            StationData stationData = reader.read();
            stationData.missingValue = 999999;

            ColorFigureSetting setting = new ColorFigureSetting();
            if (StringUtil.isNotEmpty(title)){
                setting.setTitle(title);
            }
            if (StringUtil.isNotEmpty(subTitle)){
                setting.setSubTitle(subTitle);
            }
            MeteoColorFigure meteoColorFigure = new MeteoColorFigure(config.getShpFile(), setting);
            GridData gridData = meteoColorFigure.interpolation(stationData);

            String legendPath = "static/LegendConfig.xml";
            InputStream is = new ClassPathResource(legendPath).getInputStream();
            LegendScheme scheme = new LegendSchemeHelper(is).getLegendSchemeByAlgType(colorName);

            String outputImage = genName();
            meteoColorFigure.draw(gridData, scheme, outputImage);

            log.info("画图完成，图片位置：{}", outputImage);
            return downloadResponse(outputImage, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 将数据流返回到response
     */
    protected ResponseEntity<org.springframework.core.io.Resource> downloadResponse(String outputImage,
                                                                                    HttpServletRequest request) throws IOException {
        File file = new File(outputImage);
        String fileName = file.getName();

        org.springframework.core.io.Resource resource = new UrlResource(file.toURI());
        if (!resource.exists()){
            throw new RuntimeException("数据不存在或已被删除");
        }
        String contentType = "application/octet-stream";

        String header = request.getHeader("User-Agent").toUpperCase();
        try {
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replace("+", "%20");    // IE下载文件名空格变+号问题
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {}

        String disposition = "attachment; filename="+fileName;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.toString())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .body(resource);
    }

    private ResponseEntity<ReturnT<?>> buildMsg(ReturnT<?> returnT){
        return ResponseEntity.ok(returnT);
    }

    private String genName(){
        String randomText = UUID.randomUUID().toString().replace("-", "");
        return Paths.get(config.getOutDir(), randomText + ".png").toString();
    }
}
