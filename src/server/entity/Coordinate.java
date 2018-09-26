package server.entity;

import java.io.Serializable;

/**
 * 坐标数据类
 */
public class Coordinate implements Serializable{
    private double coX ; //x坐标
    private double coY ; //y坐标

    public Coordinate(double coX, double coY) {
        this.coX = coX;
        this.coY = coY;
    }

    public void setCoX(double coX) {
        this.coX = coX;
    }

    public void setCoY(double coY) {
        this.coY = coY;
    }

    public double getCoX() {
        return coX;

    }

    public double getCoY() {
        return coY;
    }
}
