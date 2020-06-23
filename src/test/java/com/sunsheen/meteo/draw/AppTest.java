package com.sunsheen.meteo.draw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/20 16:30
 */
@SpringBootTest
public class AppTest {

    @Test
    public void test01(){
        String shpFile = "shp/china/全国界面.shp";
        ClassPathResource shpFileResource = new ClassPathResource(shpFile);

        System.out.println(shpFileResource);
    }
}
