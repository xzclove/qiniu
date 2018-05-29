package com.zhazhapan.qiniu.util;

import com.zhazhapan.qiniu.modules.constant.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lenovo on 2018/5/29.
 */
public class PathUtil {

    private static Logger logger = LoggerFactory.getLogger(PathUtil.class);

    private static PathUtil pathUtil = new PathUtil();

    private static String osname;

    public static void main(String[] args) {
        System.out.println(getImagePath());
    }

    public static String getProjectPath() {
        return pathUtil.getClass().getResource("/").getPath();
    }

    // 设置程序工作目录
    public static String getWorkDir() {
        osname = System.getProperties().getProperty("os.name").toLowerCase();
        if (osname.contains(Values.WINDOW_OS)) {
            return Values.APP_PATH_OF_WINDOWS;
        } else {
            return Values.APP_PATH_OF_UNIX;
        }
    }

    public static String getImagePath() {
        osname = System.getProperties().getProperty("os.name").toLowerCase();
        if (osname.contains(Values.WINDOW_OS)) {
            return "C:\\ProgramData\\";
        } else {
            return Values.APP_PATH_OF_LINUX;
        }
    }
}
