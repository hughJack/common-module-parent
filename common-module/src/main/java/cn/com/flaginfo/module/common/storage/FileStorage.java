package cn.com.flaginfo.module.common.storage;

import cn.com.flaginfo.exception.restful.RestfulException;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2018/12/4 下午4:58
 */
public interface FileStorage {

    /**
     * 上传文字
     *
     * @param content
     * @param name
     * @return
     */
    String uploadString(String content, String... name) throws RestfulException;

    /**
     * 上传制定类型的文件流
     *
     * @param in
     * @param paths
     * @return
     */
    String uploadInputStream(InputStream in, String downloadName, String contentType, String... paths);

    /**
     * 上传文件流
     * @param in
     * @param objectMetadata
     * @param name
     * @return
     */
    String uploadInputStream(InputStream in, ObjectMetadata objectMetadata, String... name);

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     * @throws RestfulException
     */
    String uploadFile(MultipartFile file, String... path) throws RestfulException;

    /**
     * 上传base64编码的图片
     * @param base64Str
     * @return
     */
    String uploadBase64Image(String base64Str, String... path) throws RestfulException;

    /**
     * 根据url获取对象流
     *
     * @param url
     * @return
     */
    InputStream getObjectStream(String url);

    /**
     * 根据url获取文件大小
     *
     * @param url
     * @return
     */
    long getObjectLength(String url);

    /**
     * 根据url获取对象流
     * @param url
     * @return
     */
    File getObjectFile(String url) throws RestfulException;

    /**
     * 判断文件对象是否存在
     * @param url
     * @return
     */
    boolean existObject(String url);

    /**
     * 获取制定大小的图片url
     * @param url
     * @return
     */
    String getResizeImageWithUrl(String url, long width, long height);


    /**
     * web前端获取oss签名直传，并设置后端回调函数
     * @param bucketName
     * @param dir
     * @param callBackUrl 回调地址
     * @return
     */
    Map<String, String> getWebSignature(String bucketName, String dir, String callBackUrl);
    /**
     * web前端获取oss签名直传
     * @param bucketName
     * @param dir
     * @return
     */
    Map<String, String> getWebSignature(String bucketName, String dir);

    /**
     * 删除文件
     *
     * @param url
     */
    void delete(String url);

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
    static String getContentTypeWithExtension(String fileExtension) {
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension)
                || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
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
     * @param base64Str
     * @return
     * @throws RestfulException
     */
    static String getContentTypeWithBase64Str(String base64Str) throws RestfulException {
        if( StringUtils.isBlank(base64Str) ){
            throw new RestfulException("image content is blank.");
        }
        String[] imgInfo = base64Str.split("base64,");
        if( imgInfo.length != 2 ){
            throw new RestfulException("the base64 string for image is illegal.");
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
        throw new RestfulException("the base64 type for image is illegal.");
    }
}
