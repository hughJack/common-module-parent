package cn.com.flaginfo.module.common.storage.oss;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.exception.restful.RestfulException;
import cn.com.flaginfo.module.common.storage.AbstractFileStorage;
import cn.com.flaginfo.module.common.utils.FileFormatterUtils;
import cn.com.flaginfo.module.common.utils.ImageUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author: Meng.Liu
 * @date: 2018/12/4 上午10:22
 */
@Component
@ConditionalOnBean(OssConfiguration.class)
@Slf4j
public class OssFileStorage extends AbstractFileStorage {
    /**
     * web签名默认过期时间
     */
    private static final long DEFAULT_SIGNATURE_EXPIRE = 30;

    private OSSClient ossClient;

    @Autowired
    private OssConfiguration ossConfiguration;


    OssFileStorage() {
    }

    @PostConstruct
    private void instance() {
        log.info("init aliyun storage : {}", ossConfiguration);
        long start = System.currentTimeMillis();
        ossClient = new OSSClient(ossConfiguration.getEndpoint(),
                ossConfiguration.getAccessKeyId(), ossConfiguration.getAccessKeySecret());
        if (!ossClient.doesBucketExist(ossConfiguration.getBucketName())) {
            log.info("storage bucket [{}] dose not exist, try to create it.", ossConfiguration.getBucketName());
            ossClient.createBucket(ossConfiguration.getBucketName());
            ossClient.setBucketAcl(ossConfiguration.getBucketName(),
                    CannedAccessControlList.PublicRead);
        }
        if (StringUtils.isNotBlank(ossConfiguration.getRootFolder())) {
            if (!ossConfiguration.getRootFolder().endsWith(separator)) {
                ossConfiguration.setRootFolder(ossConfiguration.getRootFolder() + separator);
            }
            try {
                ossClient.getObjectMetadata(ossConfiguration.getBucketName(), ossConfiguration.getRootFolder());
            } catch (OSSException e) {
                switch (e.getErrorCode()) {
                    case OSSErrorCode
                            .NO_SUCH_KEY:
                        this.createOssFolder();
                        break;
                    default:
                        throw e;
                }
            }
        }
        try {
            String endpoint = ossConfiguration.getEndpoint();
            if( !endpoint.contains("http")){
                endpoint = "http://" + endpoint;
            }
            URL url = new URL(endpoint);
            baseHost = url.getHost();
            basePath = new StringBuilder(url.getProtocol())
                    .append("://")
                    .append(ossConfiguration.getBucketName())
                    .append(point)
                    .append(baseHost)
                    .append(separator)
                    .toString();
            log.info("init storage base path is : {}", basePath);
            if (StringUtils.isNotBlank(ossConfiguration.getEndpointAlias())) {
                url = new URL(endpoint);
                aliasBaseHost = url.getHost();
                aliasBasePath = new StringBuilder(url.getProtocol())
                        .append("://")
                        .append(aliasBaseHost)
                        .append(separator)
                        .toString();
                transformAliasPath = true;
                log.info("init storage alias base path is : {}", aliasBasePath);
            }
        } catch (MalformedURLException e) {
            log.error("", e);
        }
        log.info("init storage success : {}ms", System.currentTimeMillis() - start);
    }

    private void createOssFolder() {
        log.info("cannot find folder [{}] in storage bucket [{}]", ossConfiguration.getRootFolder(),
                ossConfiguration.getBucketName());
        InputStream bin = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);
        try {
            ossClient.putObject(ossConfiguration.getBucketName(), ossConfiguration.getRootFolder(),
                    bin, objectMetadata);
        } finally {
            IOUtils.closeQuietly(bin);
        }
    }

    /**
     * 上传文字
     *
     * @param content
     * @param name
     * @return
     */
    @Override
    public String uploadString(String content, String... name) throws RestfulException {
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            return uploadInputStream(inputStream, null, null, name);
        } catch (IOException e) {
            log.error("", e);
            throw new RestfulException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    /**
     * 上传文字
     *
     * @param in
     * @param paths
     * @return
     */
    @Override
    public String uploadInputStream(InputStream in, String downloadName, String contentType, String... paths) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (StringUtils.isNotBlank(contentType)) {
            objectMetadata.setContentType(contentType);
        }
        if (StringUtils.isNotBlank(downloadName)) {
            objectMetadata.setContentDisposition("inline;filename=" + downloadName);
        }
        return uploadInputStream(in, objectMetadata, paths);
    }

    /**
     * 上传文件流
     * @param in
     * @param objectMetadata
     * @param name
     * @return
     */
    @Override
    public String uploadInputStream(InputStream in, ObjectMetadata objectMetadata, String... name) {
        long start = System.currentTimeMillis();
        String saveName = joinPath(name);
        PutObjectResult result = ossClient.putObject(ossConfiguration.getBucketName(),
                saveName, in, objectMetadata);
        if( log.isDebugEnabled() ){
            log.debug("upload {} success, eTag is {}", saveName, result.getETag());
        }
        log.info("upload file take {}ms.", System.currentTimeMillis() - start);
        return transformPath(saveName);
    }


    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     * @throws RestfulException
     */
    @Override
    public String uploadFile(MultipartFile file, String... path) throws RestfulException {
        String fileName = String.format("%s.%s",
                UUID.randomUUID().toString(),
                FilenameUtils.getExtension(file.getOriginalFilename()));
        try (InputStream inputStream = file.getInputStream()) {
            String fileType = FilenameUtils.getExtension("." + file.getOriginalFilename());
            String[] paths = Arrays.copyOf(path, path.length + 1);
            paths[paths.length - 1] = fileName;
            return uploadInputStream(inputStream,
                    buildObjectMetadata(file.getOriginalFilename(), inputStream.available(), fileType),
                    paths);
        } catch (IOException e) {
            log.error("", e);
            throw new RestfulException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    /**
     * @param base64Str
     * @return
     */
    @Override
    public String uploadBase64Image(String base64Str, String... path) throws RestfulException {
        if (StringUtils.isBlank(base64Str)) {
            log.error("base64 code string is blank.");
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        byte[] b = Base64Utils.decodeFromString(base64Str);
        return this.uploadByteImage(b, FileFormatterUtils.getFormatterWithBase64Str(base64Str), path);
    }

    /**
     * @param bytes
     * @return
     */
    @Override
    public String uploadByteImage(byte[] bytes, String formatter, String... path) throws RestfulException {
        if (null == bytes) {
            log.error("image bytes are empty.");
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        if( StringUtils.isBlank(formatter) ){
            formatter = DEFAULT_IMG_FORMATTER;
        }
        String fileName = UUID.randomUUID().toString() + formatter;
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            String[] paths = Arrays.copyOf(path, path.length + 1);
            paths[paths.length - 1] = fileName;
            return uploadInputStream(inputStream,
                    buildObjectMetadata(fileName, inputStream.available(), formatter),
                    paths);
        } catch (Exception e) {
            log.error("", e);
            throw new RestfulException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    @Override
    public String uploadAvatarImage(byte[] bytes, String... path) throws RestfulException {
        if (null == bytes) {
            log.warn("image bytes are empty.");
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        String format = ImageUtils.imageType(new ByteArrayInputStream(bytes));
        if (StringUtils.isNotBlank(format)) {
            if (!ArrayUtils.contains(new String[] {"png", "jpg",  "jpeg"}, format)) {
                bytes = ImageUtils.toJpegBytes(bytes);
            }
        } else {
            log.warn("Unsupported Image File Format [{}]", format);
            throw new RestfulException(ErrorCode.ILLEGAL_IMAGE);
        }
        return this.uploadByteImage(bytes, DEFAULT_AVATAR_FORMATTER, path);
    }

    /**
     * 根据url获取对象流
     *
     * @param url
     * @return
     */
    @Override
    public InputStream getObjectStream(String url) {
        return ossClient.getObject(new GetObjectRequest(ossConfiguration.getBucketName(),
                url2OssFileName(url))).getObjectContent();
    }

    /**
     * 根据url获取对象大小
     *
     * @param url
     * @return
     */
    @Override
    public long getObjectLength(String url) {
        return ossClient.getObject(new GetObjectRequest(ossConfiguration.getBucketName(),
                url2OssFileName(url))).getObjectMetadata().getContentLength();
    }

    /**
     * 根据url获取对象流
     *
     * @param url
     * @return
     */
    @Override
    public File getObjectFile(String url) throws RestfulException {
        OSSObject object = null;
        try {
            object = ossClient.getObject(new GetObjectRequest(ossConfiguration.getBucketName(),
                    url2OssFileName(url)));
        } catch (OSSException e) {
            if (!OSSErrorCode.NO_SUCH_KEY.equals(e.getErrorCode())) {
                log.error("get file error.", e);
            }
            return null;
        }
        ObjectMetadata metadata = object.getObjectMetadata();
        Map<String, String> userMateMap = metadata.getUserMetadata();
        String format = null;
        if (!CollectionUtils.isEmpty(userMateMap)) {
            format = userMateMap.get("File-Type");
        }
        if (StringUtils.isBlank(format)) {
            String contentType = metadata.getContentType();
            if (StringUtils.isNotBlank(contentType)) {
                MimeType mimeType = MimeTypeUtils.parseMimeType(contentType);
                format = mimeType.getSubtype();
            }
        }
        String fileName = UUID.randomUUID().toString() + "." + format;
        File file = new File(FileUtils.getTempDirectory(), fileName);
        file.deleteOnExit();
        try (InputStream fIn = object.getObjectContent();
             OutputStream fOut = new FileOutputStream(file)) {
            IOUtils.copy(fIn, fOut);
        } catch (IOException e) {
            log.error("download file [" + url + "] error.", e);
            throw new RestfulException(ErrorCode.DOWNLOAD_FILE_FAILED);
        }
        return file;
    }

    /**
     * 判断文件对象是否存在
     *
     * @param url
     * @return
     */
    @Override
    public boolean existObject(String url) {
        try {
            ossClient.getObjectMetadata(ossConfiguration.getBucketName(),
                    url2OssFileName(url));
            return true;
        } catch (OSSException e) {
            if (!OSSErrorCode.NO_SUCH_KEY.equals(e.getErrorCode())) {
                log.error("check exist object error.", e);
            }
            return false;
        }
    }

    /**
     * 获取制定大小的图片url
     *
     * @param url
     * @return
     */
    @Override
    public String getResizeImageWithUrl(String url, long width, long height) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        return url + MessageFormat.format("?x-storage-process=image/resize,m_fill,w_{0},h_{1}", width, height);
    }

    /**
     * web前端获取oss签名直传，并设置后端回调函数
     *
     * @param bucketName
     * @param dir
     * @param callBackUrl 回调地址
     * @return
     */
    @Override
    public Map<String, String> getWebSignature(String bucketName, String dir, String callBackUrl) {
        Map<String, String> resp = getWebSignature(bucketName, dir);
        JSONObject callBackJson = new JSONObject();
        callBackJson.put("callbackUrl", callBackUrl);
        callBackJson.put("callbackBody",
                "filename=${object}&size=${size}$mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callBackJson.put("callbackBodyType", ContentType.APPLICATION_FORM_URLENCODED.toString());
        String base64CallBack = BinaryUtil.toBase64String(callBackJson.toString().getBytes());
        resp.put("callback", base64CallBack);
        return resp;
    }

    /**
     * web前端获取oss签名直传
     *
     * @param bucketName
     * @param dir
     * @return
     */
    @Override
    public Map<String, String> getWebSignature(String bucketName, String dir) {
        long expireEndTime = System.currentTimeMillis() + DEFAULT_SIGNATURE_EXPIRE * 1000;
        Date expireDate = new Date(expireEndTime);
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1024 * 1024 * 100);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        String postPolicy = ossClient.generatePostPolicy(expireDate, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodePolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        Map<String, String> resp = new LinkedHashMap<>();
        resp.put("accessid", ossConfiguration.getAccessKeyId());
        resp.put("policy", encodePolicy);
        resp.put("signature", postSignature);
        resp.put("dir", dir);
        String host = "http://" + bucketName + "." + aliasBaseHost;
        resp.put("host", host);
        resp.put("expire", String.valueOf(expireEndTime / 1000));
        return resp;
    }

    private ObjectMetadata buildObjectMetadata(String fileName, int length, String formatter) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(length);
        objectMetadata.setContentEncoding(StandardCharsets.UTF_8.toString());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setContentDisposition("inline;filename=" + fileName);
        objectMetadata.setHeader("Pragma", "no-cache");
        Map<String, String> map = new HashMap<>(1);
        map.put("File-Type", formatter);
        objectMetadata.setUserMetadata(map);
        objectMetadata.setContentType(FileFormatterUtils.getContentTypeWithExtension(formatter));
        return objectMetadata;
    }

    /**
     * 转换路径为Nginx路径
     *
     * @param path
     * @return
     */
    private String transformPath(String path) {
        if (transformAliasPath) {
            return aliasBasePath + path;
        }
        return basePath + path;
    }

    /**
     * 删除文件
     *
     * @param url
     */
    @Override
    public void delete(String url) {
        if (StringUtils.isBlank(url)) {
            log.error("file name or url is blank.");
        } else {
            ossClient.deleteObject(ossConfiguration.getBucketName(), url2OssFileName(url));
        }
    }

    /**
     * 将文件全称转换为OSS的路径存储名
     *
     * @param url
     * @return
     */
    private String url2OssFileName(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        return ossConfiguration.getRootFolder() +
                url.replace(basePath, "")
                        .replace(aliasBasePath, "");

    }

    @Override
    public String getFolder() {
        return ossConfiguration.getRootFolder();
    }
}
