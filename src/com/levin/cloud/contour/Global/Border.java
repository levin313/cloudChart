
package com.levin.cloud.contour.Global;

import java.util.List;
import java.util.ArrayList;

/**
 * Border class - contour line border
 *
 * @author levin
 * @version 1.0.0
 */
public class Border {
    public List<BorderLine> LineList = new ArrayList<BorderLine>();
    
    public Border()
    {
        
    }
    
    public int getLineNum(){
        return LineList.size();
    }
            
}
