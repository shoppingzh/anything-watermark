/**
 * 
 */
package com.xpzheng.watermark.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xpzheng
 *
 */
public class WebUtils {

    /**
     * 
     * @param req
     * @param pathname
     * @return
     */
    public static File getWebPath(HttpServletRequest req, String pathname) {
        String realPath = req.getServletContext().getRealPath(pathname);
        File path = new File(realPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }
    
}
