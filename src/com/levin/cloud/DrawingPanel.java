package com.levin.cloud;

import com.levin.cloud.dto.AreaHighPointDto;
import com.levin.cloud.dto.PointTempDto;
import com.levin.cloud.dto.TempLabelDto;
import com.levin.cloud.dto.TempSecDto;
import com.levin.cloud.contour.Contour;
import com.levin.cloud.contour.Global.*;
import com.levin.cloud.contour.Global.Polygon;
import com.levin.cloud.contour.Interpolate;
import com.levin.cloud.contour.Legend;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 云图面板
 *
 * @author Levin
 * @date 2018/04/01
 * @since 1.0.0
 */
public class DrawingPanel extends JPanel {
    public void setRoom_type(ROOM_TYPE room_type) {
        this.room_type = room_type;
    }

    public enum PLANE_TYPE {
        /**
         * 正视图（竖直）
         */
        FRONT("正视图截面", "x", "由西向东（单位:米）", "由下向上（单位:米）"),
        /**
         * 俯视图（水平）
         */
        OVER("俯视图截面", "z", "由西向东（单位:米）", "由南向北（单位:米）"),
        /**
         * 侧视图（竖直）
         */
        VISION("侧视图截面", "y", "由北向南（单位:米）", "由下向上（单位:米）");

        private String name;
        private String code;
        private String abscissa;
        private String ordinate;

        PLANE_TYPE(String name, String code, String abscissa, String ordinate) {
            this.name = name;
            this.code = code;
            this.abscissa = abscissa;
            this.ordinate = ordinate;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public String getAbscissa() {
            return abscissa;
        }

        public String getOrdinate() {
            return ordinate;
        }

        public static PLANE_TYPE findByOrdinal(int ordinal) {
            return Arrays.stream(PLANE_TYPE.values()).filter(type -> Objects.equals(type.ordinal(), ordinal)).findFirst().orElse(null);
        }

        public static PLANE_TYPE findByName(String name) {
            return Arrays.stream(PLANE_TYPE.values()).filter(type -> Objects.equals(type.getName(), name)).findFirst().orElse(null);
        }

        public static PLANE_TYPE findByCode(String code) {
            return Arrays.stream(PLANE_TYPE.values()).filter(type -> Objects.equals(type.getCode(), code)).findFirst().orElse(null);
        }
    }

    public enum ROOM_TYPE {
        CYLINDER,//圆筒仓
        BUNGALOW //平房仓
    }

    /**
     * 用于存放插值算法生成的数据
     */
    private double[][] _gridData = null;
    /**
     * 用于存放原始离散数据
     */
    private double[][] _discreteData = null;
    /**
     * 用于存插值坐标的X坐标值
     */
    private double[] _X = null;
    /**
     * 用于存放插值坐标的Y坐标值
     */
    private double[] _Y = null;
    /**
     * 用于存放图列温度数值
     */
    private double[] _CValues = null;
    /**
     * 用于存放分配后的颜色数组
     */
    private Color[] _colors = null;
    /**
     * 用于处理空数据
     */
    private double _undefData = -9999.0;
    /**
     * 起始颜色
     */
    private Color _startColor = Color.blue;
    /**
     * 终止颜色
     */
    private Color _endColor = Color.red;
    /**
     * 图像边界点集合
     */
    private List<Border> _borders = new ArrayList<>();
    /**
     * 等值线集合
     */
    private List<PolyLine> _contourLines = new ArrayList<>();
    /**
     * 平滑后的等值线集合
     */
    private List<PolyLine> _clipContourLines = new ArrayList<>();
    /**
     * 等值线围成的封闭区间集合（待填充的区间集合）
     */
    private List<Polygon> _contourPolygons = new ArrayList<>();
    /**
     * 圆滑后的等值线围成的封闭区间集合
     */
    private List<Polygon> _clipContourPolygons = new ArrayList<>();
    /**
     * 图例位置坐标的点集合
     */
    private List<LPolygon> _legendPolygons = new ArrayList<>();
    private List<List<PointD>> _clipLines = new ArrayList<>();
    /**
     * 画图X方向起始坐标
     */
    private double _minX = 0;
    /**
     * 画图Y方向起始坐标
     */
    private double _minY = 0;
    /**
     * 画图X方向终止坐标
     */
    private double _maxX = 0;
    /**
     * 画图Y方向终止坐标
     */
    private double _maxY = 0;
    /**
     * X方向画图坐标与原始坐标的比例
     */
    private double _scaleX = 1.0;
    /**
     * Y方向画图坐标与原始坐标的比例
     */
    private double _scaleY = 1.0;
    /**
     * 是否画离散点
     */
    private boolean _drawDiscreteData = true;
    /**
     * 是否画插值点
     */
    private boolean _drawGridData = true;
    /**
     * 是否画边界线
     */
    private boolean _drawBorderLine = true;
    /**
     * 是否画等值线
     */
    private boolean _drawContourLine = false;
    /**
     * 是否填充等值线围成的封闭区域
     */
    private boolean _drawContourPolygon = true;
    /**
     * 是否画多边形
     */
    private boolean _drawClipped = false;
    /**
     * 锯齿处理
     */
    private boolean _antiAlias = false;
    /**
     * 是否高亮
     */
    private boolean _highlight = false;
    private int _highlightIdx = 0;
    /**
     * 图例温度值数组
     */
    private double[] legendVavues = null;
    /**
     * 是否画离散点
     */
    private boolean _drawData = true;

    /**
     * 画图精度
     */
    private double precions = 1;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否画标题
     */
    private boolean _drawTile = true;
    /**
     * 总层数
     */
    private int floorNum = 0;
    /**
     * 当前层数
     */
    private int currentFloor = 0;
    /**
     * 图例类型
     */
    private int legendType = 1;

    /**
     * 横坐标
     */
    private String abscissa = "由西向东(单位:米)";

    /**
     * 纵坐标
     */
    private String ordinate = "由南向北(单位:米)";

    /**
     * 检测时刻
     */
    private String checkTime = "";

    /**
     * 需要标记温度值的区域
     */
    private List<PointTempDto> pointTemp = new ArrayList<>();

    /**
     * 需要标记温度值区域的最小面积
     */
    private double minArea = 20;

    /**
     * 是否标记温度值
     */
    private boolean _drawContourValue = true;

    /**
     * 平面示意图类型（正视图、侧视图、俯视图）
     */
    private PLANE_TYPE plane_type = PLANE_TYPE.OVER;


    /**
     * 仓类型：圆筒仓、平房仓
     */
    private ROOM_TYPE room_type = ROOM_TYPE.BUNGALOW;
    private boolean _drawPlane = true;
    private List<AreaHighPointDto> areaHighPoints = new ArrayList<>();

    public DrawingPanel(int legendType) {
        this.legendType = legendType;
    }

    public DrawingPanel() {
        this.legendType = 1;
    }

    /**
     * 清空缓存
     */
    public void ClearObjects() {
        _discreteData = null;
        _gridData = null;
        _borders = new ArrayList<>();
        _contourLines = new ArrayList<>();
        _contourPolygons = new ArrayList<>();
        _clipLines = new ArrayList<>();
        _clipContourLines = new ArrayList<>();
        _clipContourPolygons = new ArrayList<>();
        _legendPolygons = new ArrayList<>();
    }


    /**
     * 传入原始数据并计算坐标值
     *
     * @param data 数据
     * @param lx   x方向相邻坐标差值
     * @param ly   y方向相邻坐标差值
     */
    public void addData(double[][] data, double lx, double ly) {
        int rows = data.length;
        int cols = data[0].length;

        _discreteData = new double[3][rows * cols];

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                _discreteData[0][j * cols + i] = i * lx + 0.5;
                _discreteData[1][j * cols + i] = j * ly + 0.5;
                _discreteData[2][j * cols + i] = data[rows - j - 1][cols - i - 1];
            }
        }


    }

    /**
     * 产生网格数据(插值)
     *
     * @param rows             行数
     * @param cols             列数
     * @param num_of_neighbors 插值参数
     * @param lx               x方向相邻坐标差值
     * @param ly               y方向相邻坐标差值
     */
    public void addGridData(int rows, int cols, int num_of_neighbors, double lx, double ly) {
        _X = new double[rows];
        _Y = new double[cols];
        Interpolate.createGridXY_Num(0, 0, lx, ly, _X, _Y);
        _gridData = new double[rows][cols];
        _gridData = Interpolate.interpolation_IDW_Neighbor(_discreteData, _X, _Y, num_of_neighbors, _undefData);
    }


    /**
     * 计算最大最小值和平均值
     *
     * @return {最小值，平均值，最大值}
     */
    public double[] GetMinMaxValues() {
        int dNum = 0;
        double min = 0, max = 0, avg = 0;
        for (int i = 0; i < _gridData.length; i++) {
            for (int j = 0; j < _gridData[0].length; j++) {
                if (_gridData[i][j] == _undefData) {
                    continue;
                }

                avg += _gridData[i][j];
                if (dNum == 0) {
                    min = _gridData[i][j];
                    max = min;
                } else {
                    if (min > _gridData[i][j]) {
                        min = _gridData[i][j];
                    } else if (max < _gridData[i][j]) {
                        max = _gridData[i][j];
                    }
                }
                dNum += 1;
            }
        }

        avg = avg / (_gridData.length * _gridData[0].length);
        return new double[]{min, avg, max};
    }


    /**
     * 设置起始颜色
     *
     * @param aColor 颜色值
     */
    public void setStartColor(Color aColor) {
        this._startColor = aColor;
    }

    /**
     * 设置终止颜色
     *
     * @param aColor 颜色值
     */
    public void setEndColor(Color aColor) {
        this._endColor = aColor;
    }

    /**
     * 设置图例值的范围
     *
     * @param values 范围数组
     */
    public void SetContourValues(double[] values) {
        _CValues = values;
    }


    /**
     * 追踪等值线
     */
    public void TracingContourLines() {
        int nc = _CValues.length;
        int[][] S1 = new int[_gridData.length][_gridData[0].length];
        _borders = Contour.tracingBorders(_gridData, _X, _Y, S1, _undefData);
        _contourLines = Contour.tracingContourLines(_gridData, _X, _Y, nc, _CValues, _undefData, _borders, S1);
    }

    /**
     * 平滑等值线
     */
    public void SmoothLines() {
        _contourLines = Contour.smoothLines(_contourLines);
    }


    /**
     * 剪辑多边形
     */
    public void ClipPolygons() {
        _clipContourPolygons = new ArrayList<>();

        for (List<PointD> cLine : _clipLines) {
            _clipContourPolygons.addAll(Contour.clipPolygons(_contourPolygons, cLine));
        }
    }

    /**
     * 跟踪多边形
     */
    public void TracingPolygons() {
        int nc = _CValues.length;

        CreateColors(_startColor, _endColor, nc + 1);

        _contourPolygons = Contour.tracingPolygons(_gridData, _contourLines, _borders, _CValues);
    }

    /**
     * 按照温度值分配颜色（伪彩色）
     *
     * @param sColor
     * @param eColor
     * @param cNum
     */
    public void CreateColors(Color sColor, Color eColor, int cNum) {
        _colors = new Color[cNum];

        int sR = sColor.getRed();
        int sG = sColor.getGreen();
        int sB = sColor.getBlue();
        int eR = eColor.getRed();
        int eG = eColor.getGreen();
        int eB = eColor.getBlue();
        double rStep = ((double) (eR - sR) / cNum);
        double gStep = ((double) (eG - sG) / cNum);
        double bStep = ((double) (eB - sB) / cNum);
        for (int i = 0; i < _colors.length; i++) {
            int r = (int) (sR + i * rStep);
            int g = (int) (sG + i * gStep);
            int b = (int) (sB + i * bStep);
            _colors[i] = new Color(pcolor2(r, 0), pcolor2(g, 1), pcolor2(b, 2));
        }
    }

    /**
     * 设置坐标范围
     */
    public void SetCoordinate() {
        this.SetCoordinate(-10, this.getWidth(), 0, this.getHeight());
    }

    /**
     * 创建图例
     */
    public void CreateLegend() {
        PointD aPoint = new PointD();

        double width = _maxX - _minX;
        aPoint.X = _minX + width / 4;
        aPoint.Y = _minY + width / 100;
        LegendPara lPara = new LegendPara();
        lPara.startPoint = aPoint;
        lPara.isTriangle = true;
        lPara.isVertical = false;
        lPara.length = width / 2;
        lPara.width = width / 100;
        lPara.contourValues = legendVavues;

        _legendPolygons = Legend.createLegend(lPara);
        _legendPolygons.remove(0);
        _legendPolygons.remove(_legendPolygons.size() - 1);

    }

    /**
     * 画图
     *
     * @param g 画布
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("宋体", Font.BOLD, 16));
        if (_antiAlias) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        if (_drawContourPolygon && this._contourPolygons.size() > 0) {
            if (legendType == 0) {
                this.drawLegend(g2);
            } else {
                this.drawLegend2(g2);
            }

        }

        if (_drawData) {
            drawDataStatistics(g2);
        }

        if (_drawTile) {
            drawTitle(g2);
        }

        if (_drawPlane && room_type.equals(ROOM_TYPE.BUNGALOW)) {
            drawPlane(g2);
            drawDerection(g2);
            drawScale(g2);
        } else {
            drawDerection(g2);
        }

        if (room_type.equals(ROOM_TYPE.CYLINDER)) {
            double r = this._maxX - this._minX;
            int R = (int) (r * this._scaleX);
            //Ellipse2D.Double shape = new Ellipse2D.Double(this.getWidth() / 2 - R, this.getHeight() / 2 - R, 2 * R, 2 * R);
            Ellipse2D.Double shape = new Ellipse2D.Double(150, 155, 0.65 * this
                    .getWidth() + 150, 0.42 * this.getHeight() + 150);
            g2.setClip(shape);
        }

        if (_drawContourPolygon && this._contourPolygons.size() > 0) {
            this.drawContourPolygons(g2);
        }

        if (_drawContourLine && this._contourLines.size() > 0) {
            //this.drawContourLines(g2);
        }


        if (_drawBorderLine && this._borders.size() > 0) {
            this.drawBorder(g2);
        }

        if (_drawDiscreteData && room_type.equals(ROOM_TYPE.BUNGALOW)) {
            this.drawGridData2(g2);
        }

        if (_drawGridData) {
            this.drawGridData(g2);
        }

        if (_drawContourValue) {
            //drawContourValue(g2);
            drawPolygonValue2(g2);
        }

    }

    /**
     * 画边界线
     *
     * @param g 画布
     */
    private void drawBorder(Graphics2D g) {
        PointD aPoint;
        for (int i = 0; i < _borders.size(); i++) {
            Border aBorder = _borders.get(i);
            for (int j = 0; j < aBorder.getLineNum(); j++) {
                BorderLine bLine = aBorder.LineList.get(j);
                int len = bLine.pointList.size();
                int[] xPoints = new int[len];
                int[] yPoints = new int[len];

                for (int k = 0; k < len; k++) {
                    aPoint = bLine.pointList.get(k);
                    int[] sxy = ToCanvas(aPoint.X, aPoint.Y);
                    xPoints[k] = sxy[0];
                    yPoints[k] = sxy[1];
                }
                g.setColor(Color.black);
                g.drawPolyline(xPoints, yPoints, len);
            }
        }
    }

    /**
     * 画等值线
     *
     * @param g 画布
     */
    private void drawContourLines(Graphics2D g) {
        List<PolyLine> drawLines = _contourLines;
        if (_drawClipped) {
            drawLines = _clipContourLines;
        }

        PointD aPoint;
        for (int i = 0; i < drawLines.size(); i++) {
            PolyLine aLine = drawLines.get(i);
            int len = aLine.PointList.size();
            int[] xPoints = new int[len];
            int[] yPoints = new int[len];
            for (int j = 0; j < len; j++) {
                aPoint = aLine.PointList.get(j);
                int[] sxy = ToCanvas(aPoint.X, aPoint.Y);
                xPoints[j] = sxy[0];
                yPoints[j] = sxy[1];
            }
            g.setColor(Color.red);
            g.drawPolyline(xPoints, yPoints, len);
        }
    }

    /**
     * 填充区域
     *
     * @param g 画布
     */
    private void drawContourPolygons(Graphics2D g) {
        List<Polygon> drawPolygons = _contourPolygons;
        if (_drawClipped) {
            drawPolygons = _clipContourPolygons;
        }

        List<String> values = new ArrayList<String>();
        for (double v : _CValues) {
            values.add(String.valueOf(v));
        }
        for (int i = 0; i < drawPolygons.size(); i++) {
            Polygon aPolygon = drawPolygons.get(i);
            drawPolygon(g, aPolygon, values, false);
        }
        if (this._highlight) {
            if (this._highlightIdx < drawPolygons.size()) {
                drawPolygon(g, drawPolygons.get(this._highlightIdx), values, true);
            }
        }
    }

    /**
     * 填充颜色
     *
     * @param g           画布
     * @param aPolygon    区域
     * @param values      值
     * @param isHighlight 是否高亮
     */
    private void drawPolygon(Graphics2D g, Polygon aPolygon, List<String> values, boolean isHighlight) {
        PointD aPoint;
        String aValue = String.valueOf(aPolygon.LowValue);
        int idx = values.indexOf(aValue) + 1;
        Color aColor = Color.black;
        Color bColor = Color.gray;
        if (isHighlight) {
            aColor = Color.green;
            bColor = Color.blue;
        } else {
            aColor = _colors[idx];
            if (!aPolygon.IsHighCenter) {
                for (int j = 1; j < _colors.length; j++) {
                    if (aColor.getRGB() == _colors[j].getRGB()) {
                        aColor = _colors[j - 1];
                    }
                }
            }
        }

        int len = aPolygon.OutLine.PointList.size();
        GeneralPath drawPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, len);
        for (int j = 0; j < len; j++) {
            aPoint = aPolygon.OutLine.PointList.get(j);
            int[] sxy = ToCanvas(aPoint.X, aPoint.Y);
            if (j == 0) {
                drawPolygon.moveTo(sxy[0], sxy[1]);
            } else {
                drawPolygon.lineTo(sxy[0], sxy[1]);
            }
        }

        if (aPolygon.HasHoles()) {
            for (int h = 0; h < aPolygon.HoleLines.size(); h++) {
                List<PointD> newPList = aPolygon.HoleLines.get(h).PointList;
                for (int j = 0; j < newPList.size(); j++) {
                    aPoint = newPList.get(j);
                    int[] sxy = ToCanvas(aPoint.X, aPoint.Y);
                    if (j == 0) {
                        drawPolygon.moveTo(sxy[0], sxy[1]);
                    } else {
                        drawPolygon.lineTo(sxy[0], sxy[1]);
                    }
                }
            }
        }
        drawPolygon.closePath();

        g.setColor(aColor);
        g.fill(drawPolygon);
        g.setColor(bColor);
        if (_drawContourLine) {
            g.draw(drawPolygon);
        }
    }

    /**
     * 画插值点
     *
     * @param g 画布
     */
    private void drawGridData(Graphics2D g) {
        if (_gridData != null) {
            for (int i = 0; i < _X.length; i++) {
                for (int j = 0; j < _Y.length; j++) {
                    int[] sxy = ToCanvas(_X[i], _Y[j]);
                    if (DoubleEquals(_gridData[j][i], _undefData)) {
                        g.setColor(Color.gray);
                        g.fillOval(sxy[0], sxy[1], 2, 2);
                    } else {
                        g.setColor(Color.blue);
                        g.fillOval(sxy[0], sxy[1], 4, 4);
                    }
                }
            }
        }
    }

    /**
     * 画离散点
     *
     * @param g 画布
     */
    private void drawGridData2(Graphics2D g) {
        if (_discreteData != null) {
            double[] x = _discreteData[0];
            double[] y = _discreteData[1];
            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < y.length; j++) {
                    int[] sxy = ToCanvas(x[i], y[j]);
                    g.setColor(Color.black);
                    g.fillOval(sxy[0] - (int) (0.5 * _scaleX), sxy[1], 4, 4);
                }
            }
        }
    }

    /**
     * 画图例
     *
     * @param g 画布
     */
    private void drawLegend(Graphics2D g) {
        if (_legendPolygons.size() > 0) {
            PointD p2 = _legendPolygons.get(2).pointList.get(2);
            PointD p1 = _legendPolygons.get(1).pointList.get(2);
            int flag = ToCanvas(p2.X, p2.Y)[0] - ToCanvas(p1.X, p1.Y)[0];
            LPolygon aLPolygon;
            int i, j;
            List<Double> values = new ArrayList<Double>();
            for (double v : _CValues) {
                values.add(v);
            }
            PointD aPoint;
            for (i = 0; i < _legendPolygons.size(); i++) {
                aLPolygon = _legendPolygons.get(i);
                double aValue = aLPolygon.value;
                //int idx = values.indexOf(aValue) + 1;
                int idx = getIdx(aValue);
                Color aColor;
                if (aLPolygon.isFirst) {
                    aColor = _colors[0];
                } else {
                    aColor = _colors[idx];
                }
                List<PointD> newPList = aLPolygon.pointList;

                int len = newPList.size();
                GeneralPath drawPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, len);
                int dx = 0, dy = 0;
                for (j = 0; j < len; j++) {
                    aPoint = newPList.get(j);
                    int[] sxy = ToCanvas(aPoint.X, aPoint.Y);
                    //sxy = new int[]{sxy[0] + 1300, sxy[1]};
                    if (j == 0) {
                        drawPolygon.moveTo(sxy[0], sxy[1]);
                    } else {
                        drawPolygon.lineTo(sxy[0], sxy[1]);
                        if (j == 2) {
                            dx = sxy[0];
                            dy = sxy[1];
                        }
                    }
                }
                drawPolygon.closePath();

                g.setColor(aColor);
                g.fill(drawPolygon);
                g.setColor(Color.black);
                g.draw(drawPolygon);

                if (i < _legendPolygons.size()) {
                    g.drawString(String.valueOf(legendVavues[i]), dx - 10 - flag, dy - 10);
                    //g.drawString(String.valueOf(legendVavues[i]), dx + 10, dy + 60);
                }

                if (i == _legendPolygons.size() - 1) {
                    g.drawString(String.valueOf(legendVavues[i + 1]), dx - 10, dy - 10);
                    //g.drawString(String.valueOf(legendVavues[i + 1]), dx + 10, dy + 10);
                }
            }
        }
    }

    /**
     * 图例（竖直）
     *
     * @param g2
     */
    private void drawLegend2(Graphics2D g2) {
        int x0 = (int) (0.8 * this.getWidth()) + 150;
        int y0 = 156;
        int dx = (int) (0.02 * this.getWidth());
        int dy = (int) (0.58 * this.getHeight() / _legendPolygons.size());

        for (int i = legendVavues.length - 2; i >= 0; i--) {
            GeneralPath drawPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
            drawPolygon.moveTo(x0, y0 + i * dy);
            drawPolygon.lineTo(x0 + dx, y0 + i * dy);
            drawPolygon.lineTo(x0 + dx, y0 + dy + i * dy);
            drawPolygon.lineTo(x0, y0 + dy + i * dy);
            drawPolygon.closePath();
            g2.setColor(_colors[getIdx(legendVavues[legendVavues.length - i - 1])]);
            g2.fill(drawPolygon);
            g2.setColor(Color.black);
            g2.draw(drawPolygon);
            g2.drawString(String.valueOf(legendVavues[legendVavues.length - 2 - i]), x0 + dx + 10, y0 + (i + 1) * dy);
        }
        g2.drawString(String.valueOf(legendVavues[legendVavues.length - 1]), x0 + dx + 10, y0 + 10);
        g2.drawString("温度", x0, y0 - 5);

    }

    /**
     * 画温度统计值
     *
     * @param g 画布
     */
    private void drawDataStatistics(Graphics2D g) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double[] values = GetMinMaxValues();
        String min = decimalFormat.format(values[0]);
        String avg = decimalFormat.format(values[1]);
        String max = decimalFormat.format(values[2]);
        g.drawString("最低温:" + String.valueOf(min) + "℃", 150, this.getHeight() - 150);
        g.drawString("平均温:" + String.valueOf(avg) + "℃", 350, this.getHeight() - 150);
        g.drawString("最高温:" + String.valueOf(max) + "℃", 150, this.getHeight() - 100);
        g.drawString("检测时刻:" + checkTime, 350, this.getHeight() - 100);

    }

    private void drawTitle(Graphics2D g) {
        g.drawString(title, this.getWidth() / 2 - 80, 120);
    }

    /**
     * 画文字
     *
     * @param g    画布
     * @param x    x坐标
     * @param y    y坐标
     * @param para 字符串
     */
    private void drawDataStatistics2(Graphics2D g, int[] x, int[] y, List<String> para) {
        int len = x.length;
        for (int i = 0; i < len; i++) {
            int xv = x[i];
            int yv = y[i];
            String vv = para.get(i);
            g.drawString(vv, xv, yv);
        }
    }

    /**
     * 比较浮点值
     *
     * @param a
     * @param b
     * @return
     */
    private static boolean DoubleEquals(double a, double b) {
        if (Math.abs(a - b) < 0.000001) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 真实坐标转换为画板(panel)坐标
     *
     * @param pX x坐标
     * @param pY y坐标
     * @return
     */
    private int[] ToCanvas(double pX, double pY) {
        //画图原点坐标：（150,150）
        int sX = (int) ((pX - _minX) * _scaleX) + 150;
        int sY = (int) ((_maxY - pY) * _scaleY) + 150;

        int[] sxy = {sX, sY};
        return sxy;
    }


    /**
     * 伪彩色
     *
     * @param v    颜色深度值
     * @param type 类型（R,G,B）
     * @return
     */
    public int pcolor2(int v, int type) {
        if (type == 0) {
            if (v >= 0 && v < 96) {
                return 0;
            }
            if (v >= 96 && v < 160) {
                return 4 * v - 384;
            }
            if (v >= 160 && v <= 224) {
                return 255;
            }
            if (v >= 224 && v <= 255) {
                return 1152 - 4 * v;
            }
        }

        if (type == 1) {
            if (v >= 0 && v < 32) {
                return 0;
            }
            if (v >= 32 && v < 96) {
                return 4 * v - 128;
            }
            if (v >= 96 && v <= 160) {
                return 255;
            }
            if (v > 160 && v <= 224) {
                return 896 - 4 * v;
            }
            if (v > 224 && v <= 255) {
                return 0;
            }
        }

        if (type == 2) {
            if (v >= 0 && v < 32) {
                return 4 * v + 127;
            }
            if (v >= 32 && v <= 96) {
                return 255;
            }
            if (v > 96 && v < 160) {
                return 640 - 4 * v;
            }
            if (v >= 160 && v <= 224) {
                return 0;
            }
            if (v >= 224 && v <= 255) {
                return 0;
            }
        }
        return 0;

    }

    /**
     * 合并颜色（将某些温度段的颜色值合并为一种颜色）
     */
    public void mergeColor(List<TempSecDto> tempSecDtoList, int type) {
        Color[] temp = _colors;
        for (TempSecDto tempSecDto : tempSecDtoList) {
            double s = tempSecDto.getStartTemp();
            double e = tempSecDto.getEndTemp();
            int num = (int) ((e - s) / precions);
            int idx = getIdx(s);
            Color color;
            if (type == 0) {
                color = tempSecDto.getColor();
            } else {
                color = temp[idx];
            }

            for (int i = 0; i < num; i++) {
                _colors[idx + i] = color;
            }
        }
    }

    /**
     * 还原颜色（按等差数列排列）
     */
    public void reductColor() {
        CreateColors(_startColor, _endColor, _CValues.length + 1);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDrawTitle(boolean drawTitle) {
        this._drawTile = drawTitle;
    }

    private void drawPlane(Graphics2D g2) {
        if (plane_type.equals(PLANE_TYPE.OVER)) {
            drawPlane1(g2);
        } else if (plane_type.equals(PLANE_TYPE.VISION)) {
            drawPlane2(g2);
        } else if (plane_type.equals(PLANE_TYPE.FRONT)) {
            drawPlane3(g2);
        }
    }

    /**
     * 画层数实例图
     *
     * @param g2
     */
    public void drawPlane1(Graphics2D g2) {
        int scale = 56;
        int d = 16;
        int x0 = this.getWidth() - 240;
        int y0 = this.getHeight() - 20;

        int x1 = x0 + scale;
        int x2 = x0 - 2 * scale;
        int x3 = x0 - 3 * scale;
        int y1 = y0 - scale;
        int y2 = y1;
        int y3 = y0;
        for (int i = 0; i < floorNum; i++) {
            GeneralPath drawPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
            drawPolygon.moveTo(x0, y0);
            drawPolygon.lineTo(x1, y1);
            drawPolygon.lineTo(x2, y2);
            drawPolygon.lineTo(x3, y3);
            drawPolygon.closePath();
            if (i == currentFloor - 1) {
                g2.setColor(Color.yellow);
                g2.fill(drawPolygon);
            } else {
                g2.setColor(Color.white);
                g2.fill(drawPolygon);
            }
            g2.setColor(Color.black);
            g2.draw(drawPolygon);
            y0 -= d;
            y1 -= d;
            y2 -= d;
            y3 -= d;
        }
    }

    private void drawPlane2(Graphics2D g2) {
        int scale = 56;
        int d = 10;
        int x0 = this.getWidth() - 180 - floorNum * d;
        int y0 = this.getHeight() - 180;

        int x1 = x0;
        int x2 = x0 - scale;
        int x3 = x2;
        int y1 = y0 + 2 * scale;
        int y2 = y0 + 3 * scale;
        int y3 = y0 + scale;
        for (int i = 0; i < floorNum; i++) {
            GeneralPath drawPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
            drawPolygon.moveTo(x0, y0);
            drawPolygon.lineTo(x1, y1);
            drawPolygon.lineTo(x2, y2);
            drawPolygon.lineTo(x3, y3);
            drawPolygon.closePath();
            if (i == currentFloor - 1) {
                g2.setColor(Color.yellow);
                g2.fill(drawPolygon);
            } else {
                g2.setColor(Color.white);
                g2.fill(drawPolygon);
            }
            g2.setColor(Color.black);
            g2.draw(drawPolygon);
            x0 += d;
            x1 += d;
            x2 += d;
            x3 += d;
        }
    }

    private void drawPlane3(Graphics2D g2) {
        int scale = 56 * 2;
        int d = 4;
        int x0 = this.getWidth() - 200 - d * floorNum;
        int y0 = this.getHeight() - 20 - d * floorNum;

        int x1 = x0;
        int x2 = (int) (x0 - scale / 1.2);
        int x3 = x2;
        int y1 = y0 - scale;
        int y2 = y1;
        int y3 = y0;
        for (int i = 0; i < floorNum; i++) {
            GeneralPath drawPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
            drawPolygon.moveTo(x0, y0);
            drawPolygon.lineTo(x1, y1);
            drawPolygon.lineTo(x2, y2);
            drawPolygon.lineTo(x3, y3);
            drawPolygon.closePath();
            if (i == currentFloor - 1) {
                g2.setColor(Color.yellow);
                g2.fill(drawPolygon);
            } else {
                g2.setColor(Color.white);
                g2.fill(drawPolygon);
            }
            g2.setColor(Color.black);
            g2.draw(drawPolygon);
            x0 += 1.5 * d;
            x1 += 1.5 * d;
            x2 += 1.5 * d;
            x3 += 1.5 * d;
            y0 += d;
            y1 += d;
            y2 += d;
            y3 += d;
        }
    }

    private void drawDerection(Graphics2D g2) {
        int x0 = 150;
        int y0 = (int) (this.getHeight() * 0.6) + 150;

        g2.drawString(abscissa, (int) (this.getWidth() * 0.5 - 100), y0 + 36);
        g2.rotate(-Math.PI / 2, x0 - 36, (int) (this.getHeight() * 0.3 + 200));
        g2.drawString(ordinate, x0 - 36, (int) (this.getHeight() * 0.3 + 200));
        g2.rotate(Math.PI / 2, x0 - 36, (int) (this.getHeight() * 0.3 + 200));
    }

    private void drawScale(Graphics2D g2) {
        int xnum = (int) (this._maxX / 5);
        int ynum = (int) (this._maxY / 5);
        int dy = (int) ((this.getHeight() * 0.6) / ynum * 0.98);
        int dx = (int) ((this.getWidth() * 0.8) / xnum * 0.98);
        int x0 = 130;
        int y0 = (int) (0.58 * this.getHeight()) + 180;

        for (int i = 0; i <= ynum; i++) {
            g2.drawString(String.valueOf(i * 5), x0, y0 - dy * i);
        }

        for (int i = 0; i <= xnum; i++) {
            g2.drawString(String.valueOf(i * 5), x0 + dx * i, y0);
        }

    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setDrawDiscreteData(boolean isDraw) {
        this._drawDiscreteData = isDraw;
    }

    public void setDrawGridData(boolean isDraw) {
        this._drawGridData = isDraw;
    }

    public void setDrawBorderLine(boolean isDraw) {
        this._drawBorderLine = isDraw;
    }

    public void setDrawContourLine(boolean isDraw) {
        this._drawContourLine = isDraw;
    }

    public void setDrawContourPolygon(boolean isDraw) {
        this._drawContourPolygon = isDraw;
    }

    public void setDrawClipped(boolean isDraw) {
        this._drawClipped = isDraw;
    }

    public void setAntiAlias(boolean isTrue) {
        this._antiAlias = isTrue;
    }

    public void setHighlight(boolean isTrue) {
        this._highlight = isTrue;
    }

    public void setHighlightIdx(int idx) {
        this._highlightIdx = idx;
    }


    public void SetScale() {
        _scaleX = (this.getWidth() - 10) / (_maxX - _minX);
        _scaleY = (this.getHeight() - 10) / (_maxY - _minY);
        this.repaint();
    }

    public void SetCoordinate(double minX, double maxX, double minY, double maxY) {
        _minX = minX;
        _maxX = maxX;
        _minY = minY;
        _maxY = maxY;
        _scaleX = (this.getWidth() - 10) / (_maxX - _minX);
        _scaleY = (this.getHeight() - 10) / (_maxY - _minY);
        if (legendType != 0) {
            _scaleX = 0.8 * _scaleX;
        }

        _scaleY = 0.6 * _scaleY;
        this.repaint();
    }

    public void setPrecision(double precision) {
        this.precions = precision;
        double v = legendVavues[legendVavues.length - 1] - legendVavues[0];
        int num = (int) (v / precision);
        double[] value = new double[num];
        value[0] = legendVavues[0];
        for (int i = 1; i < num; i++) {
            value[i] = value[i - 1] + precision;
        }
        SetContourValues(value);
    }

    public void setLegendVavues(double[] legendVavues) {
        this.legendVavues = legendVavues;
    }

    public void set_drawData(boolean _drawData) {
        this._drawData = _drawData;
    }

    private int getIdx(double v) {
        for (int i = 0; i < _CValues.length; i++) {
            if (_CValues[i] - v >= 0) {
                return i;
            }
        }

        return _CValues.length;
    }

    public void setAbscissa(String abscissa) {
        this.abscissa = abscissa;
    }

    public void setOrdinate(String ordinate) {
        this.ordinate = ordinate;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * 求不规则多边形面积
     * 对于曲边多边形可以求近似值
     *
     * @param points
     * @return
     */
    public double areaFun(List<PointD> points) {
        double area = 0.0;
        int i, j;
        int n = points.size();
        for (i = 0; i < n; i++) {
            j = (i + 1) % n;

            area += points.get(i).X * points.get(j).Y;
            area -= points.get(i).Y * points.get(j).X;
        }
        area /= 2;
        return Math.abs(area);
    }

    /**
     * 寻找需要标记温度值的等值区域
     * 以区域的重心坐标代表该区域
     */
    public void createPointTemp() {
        for (PolyLine polyLine : _contourLines) {
            List<PointD> pointList = polyLine.PointList;
            double area = areaFun(pointList);
            if (area >= minArea) {
                PointD p0 = pointList.get((int) (pointList.size() / 3));
                /*boolean flag = false;

                for (PolyLine polyLine2 : _contourLines) {
                    List<PointD> pointDList = polyLine2.PointList;

                    if (IsPtInPoly(p0, pointDList)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
*/
                PointTempDto tempDto = new PointTempDto();
                //TempLabelDto label = label(pointList.get(0), pointList.get(1));
                tempDto.setT(polyLine.Value);
                //PointD pointD = weightPoint(pointList);

               /* while (!IsPtInPoly(pointD, pointList)) {
                    pointD = deviation(pointD, pointList);
                }*/
                //PointD pointD = findMidPoint(pointList);
                tempDto.setX(p0.X);
                tempDto.setY(p0.Y);
                //tempDto.setX(label.getX());
                //tempDto.setY(label.getY());
                //tempDto.setAlpha(label.getAlpha());
                pointTemp.add(tempDto);
            }
        }
    }

    /**
     * 标记温度值
     *
     * @param g2
     */
    public void drawContourValue(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.gray);
        for (PointTempDto p : pointTemp) {
            int[] xys = ToCanvas(p.getX(), p.getY());
            //g2.rotate(p.getAlpha(), xys[0], xys[1]);
            g2.drawString(String.valueOf(p.getT()), xys[0], xys[1]);
            //g2.rotate(-p.getAlpha(), xys[0], xys[1]);
        }
    }

    /**
     * 寻找多边形重心
     *
     * @param pointDS
     * @return
     */
    public PointD weightPoint(List<PointD> pointDS) {
        double area = 0.0;//多边形面积  
        double Gx = 0.0, Gy = 0.0;// 重心的x、y  
        for (int i = 1; i <= pointDS.size(); i++) {
            double iLat = pointDS.get(i % pointDS.size()).X;
            double iLng = pointDS.get(i % pointDS.size()).Y;
            double nextLat = pointDS.get(i - 1).X;
            double nextLng = pointDS.get(i - 1).Y;
            double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
            area += temp;
            Gx += temp * (iLat + nextLat) / 3.0;
            Gy += temp * (iLng + nextLng) / 3.0;
        }
        Gx = Gx / area;
        Gy = Gy / area;
        return new PointD(Gx, Gy);
    }

    /**
     * 寻找几何中心
     *
     * @param pointDS
     * @return
     */
    public PointD findMidPoint(List<PointD> pointDS) {
        int n = pointDS.size();
        double sumX = 0;
        double sumY = 0;
        for (PointD p : pointDS) {
            sumX += p.X;
            sumY += p.Y;
        }
        return new PointD(sumX / n, sumY / n);
    }


    public void set_drawContourValue(boolean _drawContourValue) {
        this._drawContourValue = _drawContourValue;
    }

    public TempLabelDto label(PointD p1, PointD p2) {
        double x = p1.X;
        double y = p1.Y;
        double k = (p2.Y - p1.Y) / (p2.X - p1.X);
        double k2 = -(1 / k);
        double alpha = Math.atan(k2);
        return new TempLabelDto(x, y, alpha);
    }


    /**
     * 判断点是否在多边形内
     *
     * @param point 检测点
     * @param pts   多边形的顶点
     * @return 点在多边形内返回true, 否则返回false
     */
    public boolean IsPtInPoly(PointD point, List<PointD> pts) {

        int N = pts.size();
        int intersectCount = 0;
        double precision = 2e-10;
        Point2D.Double p1, p2;
        Point2D.Double p = new Point2D.Double(point.X, point.Y);

        p1 = new Point2D.Double(pts.get(0).X, pts.get(0).Y);
        for (int i = 1; i <= N; ++i) {
            if (p.equals(p1)) {
                return false;
            }

            p2 = new Point2D.Double(pts.get(i % N).X, pts.get(i % N).Y);
            if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {
                p1 = p2;
                continue;
            }

            if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {
                if (p.y <= Math.max(p1.y, p2.y)) {
                    if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {
                        return true;
                    }

                    if (p1.y == p2.y) {
                        if (p1.y == p.y) {
                            return true;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        if (Math.abs(p.y - xinters) < precision) {
                            return true;
                        }

                        if (p.y < xinters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                if (p.x == p2.x && p.y <= p2.y) {
                    Point2D.Double p3 = new Point2D.Double(pts.get((i + 1) % N).X, pts.get((i + 1) % N).Y);
                    if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;
        }

        if (intersectCount % 2 == 0) {
            return false;
        } else {
            return true;
        }

    }

    public PointD deviation(PointD pointD, List<PointD> pointDList) {
        PointD maxPoint = new PointD();
        double maxDistance = 0;
        for (PointD p : pointDList) {
            double d = Math.sqrt(Math.pow(p.X - pointD.X, 2) + Math.pow(p.Y - pointD.Y, 2));
            if (d > maxDistance) {
                maxDistance = d;
                maxPoint = p;
            }
        }
        return new PointD((pointD.X + maxPoint.X) / 2, (pointD.Y + maxPoint.Y) / 2);
    }

    public void setPlane_type(PLANE_TYPE plane_type) {
        this.plane_type = plane_type;
    }


    public void setDrawPlane(boolean d) {
        this._drawPlane = d;
    }


    /**
     * 找出等值区域内温度最高的测温点
     */
    public void findPolygonHighValue() {
        int len = _discreteData[0].length;
        for (Polygon polygon : _contourPolygons) {
            if (!(polygon.Area > minArea)) { //找出面积大于阈值的等值区域
                continue;
            }
            int flag = 0;
            double maxTemperature = -999D;
            for (int i = 0; i < len; i++) { //找出区域内温度最高的测温点
                if (IsPtInPoly(new PointD(_discreteData[0][i], _discreteData[1][i]), polygon.OutLine.PointList)) {
                    if (_discreteData[2][i] > maxTemperature) {
                        maxTemperature = _discreteData[2][i];
                        flag = i;
                    }
                }
            }
            if (flag != 0) {
                PointD point = new PointD(_discreteData[0][flag], _discreteData[1][flag]);
                AreaHighPointDto areaHighPointDto = new AreaHighPointDto(maxTemperature, point);
                areaHighPoints.add(areaHighPointDto);
            }

        }
    }

    /**
     * 标记等值区域内的温度值
     */
    private void drawPolygonValue2(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.gray);
        for (AreaHighPointDto areaHighPointDto : areaHighPoints) {
            int[] xys = ToCanvas(areaHighPointDto.getPoint().X, areaHighPointDto.getPoint().Y);
            g2.drawString("" + areaHighPointDto.getTemprature(), xys[0], xys[1]);
        }
    }
}
