
package com.levin.cloud.contour.Global;

/**
 * PointD class
 *
 * @author Levin
 * @version 1.0.0
 */
public class PointD {

    public double X;

    public double Y;

    public PointD() {
        X = 0.0;
        Y = 0.0;
    }


    public PointD(double x, double y) {
        X = x;
        Y = y;
    }

    @Override
    public Object clone() {
        return new PointD(X, Y);
    }
}
