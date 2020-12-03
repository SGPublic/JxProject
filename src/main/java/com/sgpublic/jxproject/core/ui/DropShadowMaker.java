package com.sgpublic.jxproject.core.ui;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class DropShadowMaker {
    private final double offsetX;
    private final double offsetY;
    private final Color color;

    public DropShadowMaker(Color color){
        this.offsetX = 0;
        this.offsetY = 0;
        this.color = color;
    }

    public DropShadowMaker(double offsetX, double offsetY, Color color){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.color = color;
    }

    public DropShadow build(double radius){
        return new DropShadow(radius, offsetX, offsetY, color);
    }
}
