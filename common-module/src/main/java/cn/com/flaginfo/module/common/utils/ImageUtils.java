package cn.com.flaginfo.module.common.utils;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.exception.restful.RestfulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author: Meng.Liu
 * @date: 2019/4/19 下午4:08
 */
@Slf4j
public class ImageUtils {


    /**
     * 获取图片类型
     *
     * @param ins
     * @return
     */
    public static String imageType(InputStream ins) {
        ImageInputStream iis = null;
        try {
            try {
                iis = ImageIO.createImageInputStream(ins);
            } catch (IOException e) {
                log.warn("Read Image Input Stream [{}] Error Caused.", ins, e);
                return null;
            }
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            String format = null;
            if (iter.hasNext()) {
                ImageReader reader = iter.next();
                try {
                    format = reader.getFormatName().toLowerCase();
                } catch (IOException e) {
                    log.warn("Get Image Format Name Error Caused.", e);
                } finally {
                    reader.dispose();
                }
            } else {
                log.info("Input Stream [{}] Is Not Image Input Stream.", ins);
            }
            return format;
        } finally {
            IOUtils.closeQuietly(iis);
            IOUtils.closeQuietly(ins);
        }
    }

    public static byte[] toJpegBytes(byte [] imageBytes) throws RestfulException {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        ImageOutputStream imageOut = null;
        try {
            ImageWriter writer =
                    ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(1);
            baos = new ByteArrayOutputStream();
            imageOut = new MemoryCacheImageOutputStream(baos);
            writer.setOutput(imageOut);
            bais = new ByteArrayInputStream(imageBytes);
            writer.write(null, new IIOImage(ImageIO.read(bais), null, null), iwp);
            writer.dispose();
            return baos.toByteArray();
        }
        catch (IOException e) {
            log.warn("Convert Image To JPEG Error ", e);
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        finally {
            IOUtils.closeQuietly(imageOut);
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(bais);
        }
    }

}
