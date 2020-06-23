package com.sunsheen.meteo.draw.util;

import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @Author: Veir
 * @Despriction:
 * @Date: Created at 2018/9/21 16:10
 * @Modify by:
 */
public class StringUtil {
    public static boolean isNull(String data){
        return data == null;
    }
    public static boolean isEmpty(String data){
        return data == null || "".equals(data);
    }

    /**
     * 存在空值
     * @param datas
     * @return
     */
    public static boolean isEmpty(String... datas){
        return Stream.of(datas).anyMatch(new Predicate<String>() {
			@Override
			public boolean test(String t) {
				return StringUtil.isEmpty(t);
			}
		});
    }
    /**
     * 全部为空值
     * @param datas
     * @return
     */
    public static boolean allEmpty(String... datas){
        return Stream.of(datas).allMatch(new Predicate<String>() {
			@Override
			public boolean test(String t) {
				return StringUtil.isEmpty(t);
			}
		});
    }

    public static boolean isNotEmpty(String data){
        return !isEmpty(data);
    }

    /**
     * 格式化byte数为可读的数据大小
     * @param bytes byte数
     * @param precisiom 精度，也即保留小数位
     * @return
     */
    public static String formatBytes(long bytes, int precisiom){
        int index = 0;
        long newValue = bytes;
        while (newValue > 1024){
            newValue = newValue / 1024;
            index ++;
        }
        String unit = null;
        switch (index){
            case 0:
                unit = "B";
                break;
            case 1:
                unit = "KB";
                break;
            case 2:
                unit = "MB";
                break;
            case 3:
                unit = "GB";
                break;
            case 4:
                unit = "TB";
                break;
        }
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(precisiom);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(newValue) + " " + unit;
    }

    /**
     * 全部不为空值
     * @param datas
     * @return
     */
    public static boolean isNotEmpty(String ... datas){
        return Stream.of(datas).allMatch(new Predicate<String>() {
			@Override
			public boolean test(String t) {
				return StringUtil.isNotEmpty(t);
			}
		});
    }

    public static String requireNonNull(String str, String message){
        if (isEmpty(str)){
            throw new NullPointerException(message);
        }
        return str;
    }
    public static String requireNonNull(String str){
        if (isEmpty(str)){
            throw new NullPointerException();
        }
        return str;
    }

    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    public static String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }

    /**
     * "cn.fh.lightning" -> "cn/fh/lightning"
     * @param name
     * @return
     */
    public static String dotToSplash(String name) {
        return name.replaceAll("\\.", "/");
    }

    public static String splashToDot(String name) {
        return name.replace("/", ".");
    }

    /**
     * "Apple.class" -> "Apple"
     */
    public static String trimExtension(String name) {
        int pos = name.indexOf('.');
        if (-1 != pos) {
            return name.substring(0, pos);
        }

        return name;
    }

    /**
     * /application/home -> /home
     * @param uri
     * @return
     */
    public static String trimURI(String uri) {
        String trimmed = uri.substring(1);
        int splashIndex = trimmed.indexOf('/');

        return trimmed.substring(splashIndex);
    }

    public static void main(String[] args) {
        System.out.println(formatBytes(1024 * 1024 * 30, 0));
    }
}
