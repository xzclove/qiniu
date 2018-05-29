package com.zhazhapan.qiniu.util;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @ClassName ToolTip
 * @date 2017年11月14日 下午11:16:01
 */
public class ToolTip {

    // 气泡提示宽
    private int _width = 300;

    // 气泡提示高
    private int _height = 120;

    // 设定循环的步长
    private int _step = 30;

    // 每步时间
    private int _stepTime = 40;

    // 显示时间
    private static int _displayTime = 3000;

    // 显示状态
    private boolean _displayStatus = true;
    private int _checkTime = 500;

    // 目前申请的气泡提示数量
    private int _countOfToolTip = 0;

    // 当前最大气泡数
    private int _maxToolTip = 0;

    // 在屏幕上显示的最大气泡提示数量
    private int _maxToolTipSceen;

    // 字体
    private Font _font;

    // 边框颜色
    private Color _bgColor;

    // 背景颜色
    private Color _border;

    // 消息颜色
    private Color _messageColor;

    // 差值设定
    int _gap;

    // 是否要求至顶（jre1.5以上版本方可执行）
    boolean _useTop = true;

    /**
     * 构造函数，初始化默认气泡提示设置
     */
    public ToolTip() {
        // 设定字体
        _font = new Font("微软雅黑", 0, 15);
        // 设定边框颜色
        //_bgColor = new Color(255, 255, 225);
        _bgColor = new Color(249, 250, 251);
        _border = Color.BLACK;
        _messageColor = Color.BLACK;
        _useTop = true;
        // 通过调用方法，强制获知是否支持自动窗体置顶
        try {
            JWindow.class.getMethod("setAlwaysOnTop", new Class[]{Boolean.class});
        } catch (Exception e) {
            _useTop = false;
        }

    }

    /**
     * 设置消息
     *
     * @param text
     * @return ToolTip
     * @Title setTipText
     * @author jie_huang@woyitech.com
     */
    public static ToolTip setTipText(String text) {
        ToolTip tip = new ToolTip();
        tip.setToolTip(null, text);
        return tip;
    }

    /**
     * 清除消息
     *
     * @return void
     * @Title clear
     */
    public void clear() {
        _displayStatus = false;
    }

    public static void setTip(String msg) {
        ToolTip tip = new ToolTip();
        tip.setToolTip(msg);
        try {
            Thread.sleep(_displayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tip.clear();
    }

    public static void main(String[] args) throws InterruptedException {
        setTip("good");
        //ToolTip tip = ToolTip.setTipText("111111111111");
        //Thread.sleep(10000);
        //tip.clear();
        ToolTip tip = new ToolTip();
        tip.setToolTip(new ImageIcon("upload_ico.png"), "测试");
        Thread.sleep(_displayTime);
        tip.clear();
    }

    /**
     * 重构JWindow用于显示单一气泡提示框
     */
    class ToolTipSingle extends JWindow {
        private static final long serialVersionUID = 1L;

        private JLabel _iconLabel = new JLabel();

        private JTextArea _message = new JTextArea();

        public ToolTipSingle() {
            initComponents();
        }

        private void initComponents() {
            setSize(_width, _height);
            _message.setFont(getMessageFont());
            JPanel externalPanel = new JPanel(new BorderLayout(1, 1));
            externalPanel.setBackground(_bgColor);
            // 通过设定水平与垂直差值获得内部面板
            JPanel innerPanel = new JPanel(new BorderLayout(getGap(), getGap()));
            innerPanel.setBackground(_bgColor);
            _message.setBackground(_bgColor);
            _message.setMargin(new Insets(4, 4, 4, 4));
            _message.setLineWrap(true);
            _message.setWrapStyleWord(true);
            // 创建具有指定高亮和阴影颜色的阴刻浮雕化边框
            EtchedBorder etchedBorder = (EtchedBorder) BorderFactory.createEtchedBorder();
            // 设定外部面板内容边框为风化效果
            externalPanel.setBorder(etchedBorder);
            // 加载内部面板
            externalPanel.add(innerPanel);
            _message.setForeground(getMessageColor());
            innerPanel.add(_iconLabel, BorderLayout.WEST);
            innerPanel.add(_message, BorderLayout.CENTER);
            getContentPane().add(externalPanel);
        }

        /**
         * 动画开始
         */
        public void animate() {
            new Animation(this).start();
        }

    }

    /**
     * 此类处则动画处理
     */
    class Animation extends Thread {

        ToolTipSingle _single;

        public Animation(ToolTipSingle single) {
            this._single = single;
        }

        /**
         * 调用动画效果，移动窗体坐标
         *
         * @param posx
         * @param startY
         * @param endY
         * @throws InterruptedException
         */
        private void animateVertically(int posx, int startY, int endY) throws InterruptedException {
            _single.setLocation(posx, startY);
            if (endY < startY) {
                for (int i = startY; i > endY; i -= _step) {
                    _single.setLocation(posx, i);
                    Thread.sleep(_stepTime);
                }
            } else {
                for (int i = startY; i < endY; i += _step) {
                    _single.setLocation(posx, i);
                    Thread.sleep(_stepTime);
                }
            }
            _single.setLocation(posx, endY);
        }

        /**
         * 开始动画处理
         */
        public void run() {
            try {
                boolean animate = true;
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Rectangle screenRect = ge.getMaximumWindowBounds();
                int screenHeight = (int) screenRect.height;
                int startYPosition;
                int stopYPosition;
                if (screenRect.y > 0) {
                    animate = false;
                }
                _maxToolTipSceen = screenHeight / _height;
                int posx = (int) screenRect.width - _width - 1;
                _single.setLocation(posx, screenHeight);
                _single.setVisible(true);
                if (_useTop) {
                    _single.setAlwaysOnTop(true);
                }
                if (animate) {
                    startYPosition = screenHeight;
                    stopYPosition = startYPosition - _height - 1;
                    if (_countOfToolTip > 0) {
                        stopYPosition = stopYPosition - (_maxToolTip % _maxToolTipSceen * _height);
                    } else {
                        _maxToolTip = 0;
                    }
                } else {
                    startYPosition = screenRect.y - _height;
                    stopYPosition = screenRect.y;

                    if (_countOfToolTip > 0) {
                        stopYPosition = stopYPosition + (_maxToolTip % _maxToolTipSceen * _height);
                    } else {
                        _maxToolTip = 0;
                    }
                }

                _countOfToolTip++;
                _maxToolTip++;

                animateVertically(posx, startYPosition, stopYPosition);

                Thread.sleep(_displayTime);
                while (_displayStatus) {
                    Thread.sleep(_checkTime);
                }

                animateVertically(posx, stopYPosition, startYPosition);

                _countOfToolTip--;
                _single.setVisible(false);
                _single.dispose();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 设定显示的图片及信息
     *
     * @param icon
     * @param msg
     */
    public void setToolTip(Icon icon, String msg) {
        ToolTipSingle single = new ToolTipSingle();
        if (icon != null) {
            single._iconLabel.setIcon(icon);
        }
        single._message.setText(msg);
        single.animate();
    }

    /**
     * 设定显示的信息
     *
     * @param msg
     */
    public void setToolTip(String msg) {
        setToolTip(null, msg);
    }

    /**
     * 获得当前消息字体
     *
     * @return
     */
    public Font getMessageFont() {
        return _font;
    }

    /**
     * 设置当前消息字体
     *
     * @param font
     */
    public void setMessageFont(Font font) {
        _font = font;
    }

    /**
     * 获得边框颜色
     *
     * @return
     */
    public Color getBorderColor() {
        return _border;
    }

    /**
     * 设置边框颜色
     *
     * @param
     */
    public void setBorderColor(Color borderColor) {
        this._border = borderColor;
    }

    /**
     * 获得显示时间
     *
     * @return
     */
    public int getDisplayTime() {
        return _displayTime;
    }

    /**
     * 设置显示时间
     *
     * @param displayTime
     */
    public void setDisplayTime(int displayTime) {
        this._displayTime = displayTime;
    }

    /**
     * 获得差值
     *
     * @return
     */
    public int getGap() {
        return _gap;
    }

    /**
     * 设定差值
     *
     * @param gap
     */
    public void setGap(int gap) {
        this._gap = gap;
    }

    /**
     * 获得信息颜色
     *
     * @return
     */
    public Color getMessageColor() {
        return _messageColor;
    }

    /**
     * 设定信息颜色
     *
     * @param messageColor
     */
    public void setMessageColor(Color messageColor) {
        this._messageColor = messageColor;
    }

    /**
     * 获得循环步长
     *
     * @return
     */
    public int getStep() {
        return _step;
    }

    /**
     * 设定循环步长
     *
     * @param _step
     */
    public void setStep(int _step) {
        this._step = _step;
    }

    public int getStepTime() {
        return _stepTime;
    }

    public void setStepTime(int _stepTime) {
        this._stepTime = _stepTime;
    }

    public Color getBackgroundColor() {
        return _bgColor;
    }

    public void setBackgroundColor(Color bgColor) {
        this._bgColor = bgColor;
    }

    public int getHeight() {
        return _height;
    }

    public void setHeight(int height) {
        this._height = height;
    }

    public int getWidth() {
        return _width;
    }

    public void setWidth(int width) {
        this._width = width;
    }

}
