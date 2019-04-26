package cn.com.flaginfo.module.common.utils;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.exception.restful.RestfulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
 * @date: 2019/4/19 下午3:53
 */
@Slf4j
public class FileFormatterUtils {

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    static String getContentTypeWithName(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        return getContentTypeWithExtension(fileExtension);
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileExtension 文件后缀
     * @return 文件的contentType
     */
    public static String getContentTypeWithExtension(String fileExtension) {
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension)
                || ".jpg".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".png".equalsIgnoreCase(fileExtension)) {
            return "image/png";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension)
                || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension)
                || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        return "application/octet-stream";
    }

    /**
     * 根据编码获取contentType
     *
     * @param base64Str
     * @return
     * @throws RestfulException
     */
    public static String getFormatterWithBase64Str(String base64Str) throws RestfulException {
        if (StringUtils.isBlank(base64Str)) {
            log.warn("image content is blank.");
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        String[] imgInfo = base64Str.split("base64,");
        if (imgInfo.length != 2) {
            log.warn("the base64 type for image is illegal.");
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        String type = imgInfo[0];
        if ("data:image/jpeg;".equalsIgnoreCase(type)) {
            return ".jpeg";
        } else if ("data:image/jpg;".equalsIgnoreCase(type)) {
            return ".jpg";
        } else if ("data:image/gif;".equalsIgnoreCase(type)) {
            return ".gif";
        } else if ("data:image/png;".equalsIgnoreCase(type)) {
            return ".png";
        } else if ("data:image/apng;".equalsIgnoreCase(type)) {
            return ".apng";
        } else if ("data:image/svg;".equalsIgnoreCase(type)) {
            return ".svg";
        } else if ("data:image/bmp;".equalsIgnoreCase(type)) {
            return ".bmp";
        }
        log.warn("the base64 type for image is illegal.");
        throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
    }
}
