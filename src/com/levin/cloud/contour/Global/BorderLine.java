
package com.levin.cloud.contour.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * BorderLine class
 *
 * @author levin
 * @version 1.0.0
 */
public class BorderLine {

        public double area;

        public Extent extent = new Extent();

        public boolean isOutLine;

        public boolean isClockwise;

        public List<PointD> pointList = new ArrayList<PointD>();

        public List<IJPoint> ijPointList = new ArrayList<IJPoint>();
}
