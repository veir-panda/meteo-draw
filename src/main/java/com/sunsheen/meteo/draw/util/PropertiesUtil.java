package com.sunsheen.meteo.draw.util;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2019/7/3 11:54
 */
public class PropertiesUtil {

	private PropertiesUtil() {
    }

    /**
     * 读取properties文件
     *
     * @param file 文件路径
     * @return 返回properties 对象
     */
    public static Properties fromFile(String file) {
        InputStream stream = null;
        Reader reader = null;
        try {
            stream = new FileInputStream(new File(file));
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            return fromReader(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(reader, stream);
        }
    }

    /**
     * 读取properties文件
     *
     * @param file 文件路径
     * @return 返回properties 对象
     */
    public static Properties fromClasspath(String file) {
        InputStream stream = null;
        Reader reader = null;
        try {
            Thread.currentThread().getContextClassLoader().getResource(file);
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            return fromReader(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(reader, stream);
        }
    }

    /**
     * convert stream  to properties
     *
     * @param stream InputStream
     * @return Properties
     * @throws IOException
     */
    public static Properties fromStream(InputStream stream) throws IOException {
        Properties dest = new Properties();
        Properties src = new Properties();
        src.load(stream);
        return strip(src, dest);
    }

    public static Properties fromReader(Reader reader) throws IOException {
        Properties dest = new Properties();
        Properties src = new Properties();
        src.load(reader);
        return strip(src, dest);
    }

    public static Properties strip(Properties src, Properties dest){


        // 如果key value为字符串，需要trim一下
        for (Map.Entry<Object, Object> entry : src.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Object newKey = key;
            Object newValue = value;
            if (newKey instanceof String) {
                newKey = key.toString().trim();
            }

            if (newValue instanceof String) {
                newValue = value.toString().trim();
            }

            dest.put(newKey, newValue);
        }
        return dest;
    }

    /**
     * dispose stream
     *
     * @param streams InputStream
     */
    private static void close(Closeable ... streams) {
        if (streams != null && streams.length > 0) {
            try {
                for (Closeable stream : streams) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
