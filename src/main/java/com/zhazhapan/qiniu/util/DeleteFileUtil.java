package com.zhazhapan.qiniu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DeleteFileUtil {

    private static Logger logger = LoggerFactory.getLogger(DeleteFileUtil.class);

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("## 删除单个文件" + fileName + "  成功！");
                return true;
            } else {
                System.out.println("## 删除单个文件" + fileName + "  失败！");
                return false;
            }
        } else {
            System.out.println("## 删除单个文件失败：" + fileName + "  不存在！");
            return false;
        }
    }
}
