package com.xpzheng.watermark.util;

import java.awt.geom.Rectangle2D;

public class MathUtils {

    /**
     * 根据用户设定坐标及坐标轴长度计算实际坐标位置<br>
     * coord参数可设置为正数或负数，正数表示从坐标轴起点开始计算，负数表示从坐标轴终点计算； 当coord为(0, 1)或(-1, 0)之间的小数时，表示使用比例计算实际坐标的位置。<br>
     * 以坐标轴长度为100为例，coord的取值与实际坐标的对应关系如下：<br>
     * 0 = 0<br>
     * 40 = 40<br>
     * -40 = 60<br>
     * 0.3 = 30<br>
     * -0.3 = 70<br>
     * 
     * @param coord 用户设定坐标
     * @param full 坐标轴总长
     * @return
     */
    public static float transCoord(float coord, float full) {
        float ac = Math.abs(coord);
        if (ac > 0 && ac <= 1) {
            coord = full * coord;
        }
        return coord < 0 ? full + coord : coord;
    }
    
    /**
     * 矫正坐标，防止坐标超出坐标轴范围<br>
     * @param coord 坐标
     * @param full 坐标轴最大长度
     * @return
     */
    public static final float adjustCoord(float coord, float full) {
        if (coord < 0) {
            coord = 0;
        }
        if (coord > full) {
            coord = full;
        }
        return coord;
    }
    
    /**
     * 
     * @param cw  container width
     * @param ch container height
     * @param w box width
     * @param h box height
     * @param scale scale degree
     * @return
     */
    public static Rectangle2D getScaleBounds(float cw, float ch, float w, float h, float scale) {
        float ratio = w / h;
        double th = Math.sqrt(cw * ch * scale / ratio);
        Rectangle2D.Double rec = new Rectangle2D.Double(0, 0, th * ratio, th);
        return rec;
    }
    
    /**
     * 
     * @param container 
     * @param target
     * @param scale
     * @return
     */
    public static Rectangle2D getScaleBounds(Rectangle2D container, Rectangle2D target, float scale) {
        return getScaleBounds((float) container.getWidth(), (float) container.getHeight(), (float) target.getWidth(),
                (float) target.getHeight(), scale);
    }

}
