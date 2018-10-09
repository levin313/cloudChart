
package com.levin.cloud.contour.Global;

/**
 * Extent class
 *
 * @author levin
 * @version 1.0.0
 */
public class Extent {

    public double xMin;

    public double yMin;

    public double xMax;

    public double yMax;

    public Extent() {

    }


    public Extent(double minX, double maxX, double minY, double maxY) {
        xMin = minX;
        xMax = maxX;
        yMin = minY;
        yMax = maxY;
    }


    public boolean Include(Extent bExtent) {
        if (xMin <= bExtent.xMin && xMax >= bExtent.xMax && yMin <= bExtent.yMin && yMax >= bExtent.yMax)
            return true;
        else
            return false;
    }
}
