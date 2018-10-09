
package com.levin.cloud.contour.Global;

import com.levin.cloud.contour.Contour;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Polygon class
 *
 * @author Levin
 * @version 1.0.0
 */
public class Polygon {
    /**
     * If is border contour polygon
     */
    public boolean IsBorder;
    /**
     * If there is only inner border
     */
    public boolean IsInnerBorder = false;
    /**
     * Start value
     */
    public double LowValue;
    /**
     * End value
     */
    public double HighValue;
    /**
     * If clockwise
     */
    public boolean IsClockWise;

    public int StartPointIdx;

    public boolean IsHighCenter;

    public Extent Extent = new Extent();

    public double Area;

    public PolyLine OutLine = new PolyLine();

    public List<PolyLine> HoleLines = new ArrayList<>();

    public int HoleIndex;


    /**
     * Clone the object
     *
     * @return cloned Polygon object
     */
    public Object Clone() {
        Polygon aPolygon = new Polygon();
        aPolygon.IsBorder = IsBorder;
        aPolygon.LowValue = LowValue;
        aPolygon.HighValue = HighValue;
        aPolygon.IsClockWise = IsClockWise;
        aPolygon.StartPointIdx = StartPointIdx;
        aPolygon.IsHighCenter = IsHighCenter;
        aPolygon.Extent = Extent;
        aPolygon.Area = Area;
        aPolygon.OutLine = OutLine;
        aPolygon.HoleLines = new ArrayList<>(HoleLines);
        aPolygon.HoleIndex = HoleIndex;

        return aPolygon;
    }


    public boolean HasHoles() {
        return (HoleLines.size() > 0);
    }

    public void AddHole(Polygon aPolygon) {
        HoleLines.add(aPolygon.OutLine);
    }


    public void AddHole(List<PointD> pList) {
        if (Contour.isClockwise(pList)) {
            Collections.reverse(pList);
        }

        PolyLine aLine = new PolyLine();
        aLine.PointList = pList;
        HoleLines.add(aLine);
    }
}
