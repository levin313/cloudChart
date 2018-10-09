
package com.levin.cloud.contour.Global;

/**
 * BorderPoint class
 *
 * @author levin
 * @version 1.0.0
 */
public class BorderPoint {

    public int Id;

    public int BorderIdx;

    public int BInnerIdx;

    public PointD Point = new PointD();

    public double Value;


    @Override
    public Object clone() {
        BorderPoint aBP = new BorderPoint();
        aBP.Id = Id;
        aBP.BorderIdx = BorderIdx;
        aBP.BInnerIdx = BInnerIdx;
        aBP.Point = Point;
        aBP.Value = Value;

        return aBP;
    }
}
