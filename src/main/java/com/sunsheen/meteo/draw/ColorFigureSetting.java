package com.sunsheen.meteo.draw;

/**
 * @author Veir, veir.xw@gmail.com
 * @create 2020/5/19 16:04
 */
public class ColorFigureSetting {
    /**
     * 背景是否透明
     */
    private boolean backgroundTransparent;
    /**
     * 显示shp中的标签名字
     */
    private boolean showLabel;
    /**
     * 绘制图片矩形框
     */
    private boolean drawRectangle;

    private String title;

    private String subTitle;

    public ColorFigureSetting() {
        this.backgroundTransparent = true;
        this.showLabel = false;
        this.drawRectangle = false;
    }

    public ColorFigureSetting setBackgroundTransparent(boolean backgroundTransparent) {
        this.backgroundTransparent = backgroundTransparent;
        return this;
    }

    public ColorFigureSetting setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        return this;
    }

    public boolean isBackgroundTransparent() {
        return backgroundTransparent;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public boolean isDrawRectangle() {
        return drawRectangle;
    }

    public ColorFigureSetting setDrawRectangle(boolean drawRectangle) {
        this.drawRectangle = drawRectangle;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ColorFigureSetting setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public ColorFigureSetting setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }
}
