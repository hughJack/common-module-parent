package cn.com.flaginfo.module.common.storage;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.exception.restful.RestfulException;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
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
     * @param in
     * @param downloadName
     * @param contentType
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
     * 上传byte图片
     * @param bytes
     * @param contentType
     * @param path
     * @return
     * @throws RestfulException
     */
    String uploadByteImage(byte[] bytes, String contentType, String... path) throws RestfulException;


    /**
     * 上传头像，头像强制转换为jpeg上传
     * @param bytes
     * @param path
     * @return
     * @throws RestfulException
     */
    String uploadAvatarImage(byte[] bytes, String... path) throws RestfulException;

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
}
