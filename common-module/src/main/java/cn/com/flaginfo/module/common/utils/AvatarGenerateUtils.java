package cn.com.flaginfo.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 头像生成工具
 * @author: Meng.Liu
 * @date: 2018/12/4 下午5:21
 */
@Slf4j
public class AvatarGenerateUtils {

    /**
     * 男性头像的背景色
     */
    public final static String GENDER_MALE_COLOR = "#5EC9F6";
    /**
     * 女性头像的背景色
     */
    public final static String GENDER_FEMALE_COLOR = "#66CCCC";
    /**
     * 未知性别的背景色
     */
    public final static String GENDER_UNKNOWN_COLOR = "#D7D2D5";
    /**
     * 默认头像的尺寸
     */
    private final static int DEFAULT_IMAGE_SIZE = 512;
    /**
     * 默认字体
     */
    private final static Font FONT_DEFAULT;

    static {
        try {
            FONT_DEFAULT = Font.createFont(Font.TRUETYPE_FONT,
                    AvatarGenerateUtils.class.getResourceAsStream("/fonts/w3.otf"))
                    .deriveFont(Font.PLAIN, 316.416F);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Init Font Error Caused", e);
        }
    }

    /**
     * 根据性别获取头像的背景色
     *
     * @param gender
     * @return
     */
    public static String getGenderDefaultColor(GenderUtils.Gender gender) {
        switch (gender) {
            case Male:
                return GENDER_MALE_COLOR;
            case Female:
                return GENDER_FEMALE_COLOR;
            case Other:
            default:
                return GENDER_UNKNOWN_COLOR;
        }
    }

    /**
     * 根据名称生成头像
     * @param name
     * @return
     */
    public static byte[] generateAvatar(String name) {
        return generateAvatar(name, GenderUtils.Gender.Other);
    }

    /**
     * 根据名称和性别生成头像
     * @param name
     * @param gender
     * @return
     */
    public static byte[] generateAvatar(String name, GenderUtils.Gender gender) {
        return generateAvatar(name, gender, DEFAULT_IMAGE_SIZE);
    }

    /**
     * 根据名称和头像和尺寸生成头像
     * @param name
     * @param gender
     * @param size
     * @return
     */
    public static byte[] generateAvatar(String name, GenderUtils.Gender gender, int size) {
        return generateFillCircleTextImage(size,
                name,
                null, Color.decode(getGenderDefaultColor(gender)), Color.WHITE, null);
    }


    /**
     * 绘制填充背景的文字图片
     * @param size
     * @param text
     * @param canvasBgColor
     * @param circleBgColor
     * @param fontColor
     * @param font
     * @return
     */
    public static byte[] generateFillCircleTextImage(
            final int size,
            final String text,
            Color canvasBgColor,
            final Color circleBgColor,
            final Color fontColor,
            final Font font) {

        return generateImage(size, canvasBgColor, g2d -> {
            fillCircle(size, g2d, circleBgColor);
            drawCentralText(size, g2d, font, fontColor, text);
        });
    }

    /**
     * 绘制不填充的文字图片
     * @param size
     * @param text
     * @param canvasBgColor
     * @param circleColor
     * @param fontColor
     * @param font
     * @return
     */
    public static byte[] generateCircleTextImage(
            final int size,
            final String text,
            Color canvasBgColor,
            final Color circleColor,
            final Color fontColor,
            final Font font) {

        return generateImage(size, canvasBgColor, g2d -> {
            drawCircle(size, g2d, circleColor);
            drawCentralText(size, g2d, font, fontColor, text);
        });
    }

    static byte[] generateImage(int size, Color bgColor, ImageProcess op) {
        if (size < 1) {
            size = DEFAULT_IMAGE_SIZE;
            log.debug("No Valid Image Size Specified, Use Default Size [{}].", size);
        }
        BufferedImage image =
                new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (bgColor != null) {
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, size, size);
        } else {
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, size, size);
            g2d.setComposite(AlphaComposite.Src);
        }
        if (op != null) {
            op.process(g2d);
        }
        g2d.dispose();
        image.flush();
        return imageToBytes(image, null);
    }

    static void drawCentralText(int size, Graphics2D g2d, Font font, Color fontColor, String text) {
        log.debug("Draw Central Text [{}].", text);
        if (font == null) {
            log.debug("No Font Specified, Use Default.");
            font = FONT_DEFAULT;
        }
        font = font.deriveFont(Font.PLAIN, size * 0.618F);
        g2d.setFont(font);
        g2d.setColor(fontColor);
        Shape outline = font.createGlyphVector(
                g2d.getFontMetrics().getFontRenderContext(),
                text).getOutline();
        double halfSize = size / 2.0, halfW = outline.getBounds().width / 2.0,
                halfH = outline.getBounds().height / 2.0;
        g2d.fill(AffineTransform.getTranslateInstance(
                -outline.getBounds().getX() + halfSize - halfW,
                -outline.getBounds().getY() + halfSize - halfH)
                .createTransformedShape(outline));
    }

    static void drawCircle(int size, Graphics2D g2d, Color color) {
        g2d.setColor(color);
        // line stroke log2(size)
        float lineWidth = (float) Math.ceil(Math.log(size) / Math.log(2));
        g2d.setStroke(new BasicStroke(lineWidth));
        double halfSize = size / 2.0;
        int r = (int) (halfSize - lineWidth - 4);
        int c = (int) (halfSize - r);
        g2d.drawOval(c, c, 2 * r, 2 * r);
    }

    static void fillCircle(int size, Graphics2D g2d, Color color) {
        g2d.setColor(color);
        int r = size / 2 - 4;
        int c = size / 2 - r;
        g2d.fillOval(c, c, 2 * r, 2 * r);
    }

    /**
     * 图片执行器
     */
    static interface ImageProcess {
        /**
         * 图片操作
         * @param g2d
         */
        void process(Graphics2D g2d);
    }

    static byte[] imageToBytes(BufferedImage image, String format) {
        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();
        try {
            ImageIO.write(image,
                    StringUtils.isNotBlank(format) ?
                            format : "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Write Image Error Caused", e);
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
}
