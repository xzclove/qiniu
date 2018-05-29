package com.zhazhapan.qiniu.util;

import com.zhazhapan.qiniu.controller.MainWindowController;
import com.zhazhapan.util.Checker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

/**
 * 剪贴板工具类
 */
public class ClipboardUtil implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ClipboardUtil.class);

    private static Boolean clipboardSwitch = false; //true关闭 false开启

    private static String lastStr = "";

    private static long sleepTime = 500;

    private static BufferedImage lastImage;

    private ClipboardUtil u = null;

    public static void main(String[] args) {
        Thread thread = new Thread(new ClipboardUtil());
        thread.start();
    }

    public static void changeClipboardSwitch() {
        clipboardSwitch = false;
        setSysClipboardText("点击剪贴板按钮");
        System.out.println("## 点击 剪贴板按钮 ");
    }

    public void run() {
        System.out.println("## 开始 剪贴板 上传 ");
        while (true) {
            try {
                getClipboardData();
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println("## 剪贴板 启动失败"+e);
            }
        }
    }

    /**
     * 剪贴板数据变动调用
     */
    public static void getClipboardData() {
        MainWindowController mainWindowController = MainWindowController.getInstance();
        String clipboardSwitchStr = "";
        if (null != mainWindowController) {
            clipboardSwitchStr = Checker.checkNull(mainWindowController.clipboardSwitch.getValue());
            if (clipboardSwitchStr.contains("关闭")) {
                clipboardSwitch = true;
            }
        }

        if (clipboardSwitch) {
            // 关闭剪贴板上传
            return;
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String text = (String) clipboard.getData(DataFlavor.stringFlavor);
                if (text.equals(lastStr)) {
                    // 文本和上次一样
                    return;
                }
                System.out.println("## 剪贴板 文本 is " +text);
                lastStr = text;
            } else if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                final BufferedImage image = (BufferedImage) clipboard.getData(DataFlavor.imageFlavor);
                if (compare(image, lastImage)) {
                    // 图片和上次一样
                    return;
                }
                String completePath = PathUtil.getImagePath() + System.currentTimeMillis() + ".png";
                File file = new File(completePath);
                Graphics2D g = image.createGraphics();
                g.drawImage(image, null, null);
                ImageIO.write((RenderedImage) image, "png", file);
                System.out.println("## 剪贴板 图片 保存位置 " +completePath);
                lastImage = image;
                MainWindowController.getInstance().uploadClipboardFile(completePath);
            } else {
                System.out.println("未知的类型");
            }
        } catch (Exception e) {
            logger.error("lostOwnership error", e);
        }
    }

    /**
     * true 图片相同
     * false  不相同
     *
     * @param sourceImage 本次截图图片
     * @param lastImage   最后一次上传的图片
     * @return
     */
    private static boolean compare(BufferedImage sourceImage, BufferedImage lastImage) {
        if (null == lastImage) {
            return false;
        }
        int sourceHeight = sourceImage.getHeight();
        int sourceWidth = sourceImage.getWidth();
        int sourceRGB = sourceImage.getRGB(0, 0);
        int lastHeight = lastImage.getHeight();
        int lastWidth = lastImage.getWidth();
        int lastRGB = lastImage.getRGB(0, 0);
        if (sourceHeight == lastHeight) {
            if (sourceWidth == lastWidth) {
                if (sourceRGB == lastRGB) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将字符串复制到剪切板。
     */
    public static void setMarkSysClipboardText(String url) {
        MainWindowController mainWindowController = MainWindowController.getInstance();
        String markdownSwitchStr = "";
        if (null != mainWindowController) {
            markdownSwitchStr = Checker.checkNull(mainWindowController.markdownSwitch.getValue());
            if (markdownSwitchStr.contains("关闭")) {
                // 关闭 markdown地址
                setSysClipboardText(url);
            }
            if (markdownSwitchStr.contains("开启")) {
                // 开启 markdown地址
                //![mark](http://ofudkearv.bkt.clouddn.com/image/180517/l409DDihFF.png)
                setSysClipboardText("![mark](" + url + ")");
            }
        }
    }

    /**
     * 将字符串复制到剪切板。
     */
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

}
