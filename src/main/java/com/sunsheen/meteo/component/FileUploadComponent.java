package com.sunsheen.meteo.component;

import com.sunsheen.meteo.listener.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/4/14 22:04
 */
@Slf4j
@Component
public class FileUploadComponent {
    @Value("#{'${data.upload.suffix:*}'.split('(, *)+')}")
    private Set<String> suffixs;

    @Resource
    private SystemConfig config;

    public List<File> save(MultipartFile[] files) {
        List<String> forbidden = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename)
                .filter(this::isNotValidFile).collect(Collectors.toList());
        if (!forbidden.isEmpty()) {
            throw new IllegalStateException("不允许上传的文件类型：" + String.join(",", forbidden));
        }
        return Arrays.stream(files)
                .map(this::save)
                .collect(Collectors.toList());
    }

    public File save(MultipartFile file) {
        if (isNotValidFile(file.getOriginalFilename())){
            throw new IllegalStateException("不允许上传的文件类型：" + file.getOriginalFilename());
        }
        String originalName = file.getOriginalFilename();
        String storePath = genName(originalName);
        File target = new File(storePath);
        try (InputStream in = file.getInputStream()) {
            FileUtils.copyInputStreamToFile(in, target);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件上传失败", e);
        }

        return target;
    }

    public String genName(String filename) {
        String extension = FilenameUtils.getExtension(filename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return Paths.get(config.getUploadDir(), uuid + "." + extension).toString();
    }

    public boolean isValidFile(String filename) {
        return suffixs.contains("*") || suffixs.contains(FilenameUtils.getExtension(filename).toLowerCase());
    }

    public boolean isNotValidFile(String filename) {
        return !isValidFile(filename);
    }
}
