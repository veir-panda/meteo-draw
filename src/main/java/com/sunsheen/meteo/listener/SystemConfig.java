package com.sunsheen.meteo.listener;

import com.sunsheen.meteo.draw.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/20 15:23
 */
@Slf4j
@Component
public class SystemConfig implements ApplicationListener<ContextRefreshedEvent> {
    private static final String UPLOAD_DATA_DIR = "data/upload";
    private static final String OUT_DATA_DIR = "data/out";

    private String workspace;
    @Value("${data.upload.dir:}")
    private String uploadDir;
    @Value("${data.out.dir:}")
    private String outDir;
    private String shpFile;
    private static String SHP_DIR = "shpfile/china";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationHome home = new ApplicationHome(SystemConfig.class);
        workspace = home.getDir().getPath();

        if (StringUtil.isEmpty(uploadDir)){
            uploadDir = Paths.get(workspace, UPLOAD_DATA_DIR).toString();
        }

        if (StringUtil.isEmpty(outDir)){
            outDir = Paths.get(workspace, OUT_DATA_DIR).toString();
        }

        File upload = new File(uploadDir);
        if (!upload.exists()){
            upload.mkdirs();
        }
        File out = new File(outDir);
        if (!out.exists()){
            out.mkdirs();
        }

        log.info("upload dir set to: {}", uploadDir);
        log.info("output dir set to: {}", outDir);

//        String shpFile = "shp/china/全国界面.shp";
        Path tmp = Paths.get(workspace, SHP_DIR);
        SHP_DIR = tmp.toString();
        if (Files.notExists(tmp)){
            tmp.toFile().mkdirs();
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Arrays.stream(resolver.getResources("/shp/**/*.*"))
                    .filter(Resource::exists)
                    .forEach(resource -> {
                        try {
                            String target = genTargetCopyFile(resource);
                            IOUtils.copy(resource.getInputStream(), new FileOutputStream(target));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        shpFile = Paths.get(SHP_DIR, "全国界面.shp").toString();
        log.info("shp file path: {}", shpFile);

    }

    private String genTargetCopyFile(Resource resource){
        return Paths.get(SHP_DIR, resource.getFilename()).toString();
    }


    public String getWorkspace() {
        return workspace;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public String getOutDir() {
        return outDir;
    }

    public String getShpFile() {
        return shpFile;
    }
}
