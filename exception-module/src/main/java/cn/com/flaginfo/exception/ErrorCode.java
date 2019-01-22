package cn.com.flaginfo.exception;

import cn.com.flaginfo.exception.i18n.LocaleHolder;
import cn.com.flaginfo.exception.i18n.MessageStore;
import lombok.Getter;

/**
 * 通用错误码
 * code错误码
 * message国家化key
 * @author: Meng.Liu
 * @date: 2018/12/17 上午10:52
 */
@Getter
public enum ErrorCode {

    //restful错误码
    /**
     * 请求成功
     */
    SUCCESS(0L, "request.success"),
    /**
     * 系统繁忙
     */
    SYS_BUSY(-1L, "system.busy"),
    /**
     * 系统服务不可用
     */
    SYS_UNAVAILABLE(-2L, "system.unavailable"),
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(-3L, "unknown.error"),
    /**
     * 错误请求
     */
    BAD_REQUEST(-4L, "bad.request"),

    //业务错误码
    /**
     * 用户未登录
     */
    NOT_LOGIN(400001L, "not.login"),
    /**
     * Token无效
     */
    INVALID_TOKEN(400002L, "invalid.token"),
    /**
     * Token不合法
     */
    ILLEGAL_TOKEN(400003L, "illegal.token"),
    /**
     * 用户被踢下线
     */
    USER_KICKED_OFFLINE(400004L, "user.kicked.offline"),
    /**
     * 登录超时
     */
    LOGIN_TIME_OUT(400005L, "login.time.out"),
    /**
     * 缺少手机号
     */
    LACK_MOBILE(400006L, "lack.mobile"),
    /**
     * 缺少密码
     */
    LACK_PASSWORD(400007L, "lack.password"),
    /**
     * 缺少重复密码
     */
    LACK_VERIFY_PASSWORD(400008L, "lack.verify.password"),
    /**
     * 不合法的密码格式
     */
    ILLEGAL_PASSWORD_FORMAT(400009L, "illegal.password.format"),
    /**
     * 密码与重复密码不一致
     */
    PASSWORDS_NOT_MATCH(400010L, "passwords.not.match"),
    /**
     * 缺少手机验证码
     */
    LACK_MOBILE_VERIFICATION_CODE(400011L, "lack.mobile.verification.code"),
    /**
     * 不合法的手机验证码格式
     */
    ILLEGAL_MOBILE_VERIFICATION_FORMAT(400012L, "illegal.mobile.verification.format"),
    /**
     * 手机验证码已经过期
     */
    MOBILE_VERIFICATION_EXPIRED(400013L, "mobile.verification.expired"),
    /**
     * 缺少验证码
     */
    LACK_VERIFICATION(400014L, "lack.verification"),
    /**
     * 不合法的验证码格式
     */
    ILLEGAL_VERIFICATION_FORMAT(400015L, "illegal.verification.format"),
    /**
     * 验证码已经过期
     */
    VERIFICATION_EXPIRED(400016L, "verification.expired"),
    /**
     * 缺少服务器安全码
     */
    LACK_SERVER_SECURITY_CODE(400017L, "lack.server.security.code"),
    /**
     * 服务器安全码已经过期
     */
    SERVER_SECURITY_EXPIRED(400018L, "server.security.expired"),
    /**
     * 缺少客户端安全码
     */
    LACK_CLIENT_SECURITY_CODE(400019L, "lack.client.security.code"),
    /**
     * 客户端安全码已经过期
     */
    CLIENT_SECURITY_CODE_EXPIRED(400020L, "client.security.code.expired"),
    /**
     * 用户账号被锁定
     */
    ACCOUNT_LOCKED(400021L, "account.locked"),
    /**
     * 密码错误次数过多
     */
    TOO_MANY_PASSWORD_ERRORS(400022L, "too.many.password.errors"),
    /**
     * 用户没有进入任何企业
     */
    USER_NOT_ENTER_ANY_CORP(400032L, "user.not.enter.any.corp"),
    /**
     * 登录次数超过一天的最大限制
     */
    LOGIN_TIMES_BEYOND_MAX_TIMES_ONE_DAY(400033L, "login.times.beyond.max.times.one.day"),
    /**
     * 密码错误
     */
    PASSWORD_ERROR(400034L, "password.error"),
    /**
     * 验证码错误
     */
    VERIFICATION_ERROR(400035L, "verification.error"),
    /**
     * 手机验证码错误
     */
    MOBILE_VERIFICATION_ERROR(400036L, "mobile.verification.error"),
    /**
     * 缺少Corp_Id
     */
    LACK_ENTERPRISE_CORP_ID(400037L, "lack.enterprise.corp.id"),
    /**
     * 缺少Sp_Id
     */
    LACK_ENTERPRISE_SP_ID(400038L, "lack.enterprise.sp.id"),
    /**
     * 缺少Sp_Code
     */
    LACK_ENTERPRISE_SP_CODE(400039L, "lack.enterprise.sp.code"),
    /**
     * 缺少Member Id
     */
    LACK_ENTERPRISE_MEMBER_ID(400040L, "lack.enterprise.member.id"),
    /**
     * 手机验证码发送频繁
     */
    MOBILE_VERIFICATION_SEND_FREQUENT(400041L, "mobile.verification.send.frequent"),
    /**
     * 手机验证码发送失败
     */
    MOBILE_VERIFICATION_SEND_FAILED(400042L, "mobile.verification.send.failed"),
    /**
     * 验证码刷新失败
     */
    VERIFICATION_REFRESH_ERROR(400043L, "verification.refresh.error"),
    /**
     * 手机号格式不正确
     */
    ILLEGAL_MOBILE_FORMAT(400044L, "illegal.mobile.format"),
    /**
     * 电话号码格式不正确
     */
    ILLEGAL_PHONE_FORMAT(400045L, "illegal.phone.format"),
    /**
     * 邮箱格式不正确
     */
    ILLEGAL_EMAIL_FORMAT(400046L, "illegal.email.format"),
    /**
     * 身份证格式不正确
     */
    ILLEGAL_ID_CARD_FORMAT(400047L, "illegal.id.card.format"),
    /**
     * 无效的密文
     */
    ILLEGAL_CIPHER_TEXT(400048L, "illegal.cipher.text"),
    /**
     * 手机号未认证，或未注册
     */
    MOBILE_NOT_REGISTERED(400049L, "mobile.not.registered"),
    /**
     * 缺少原密码
     */
    LACK_OLD_PASSWORD(400050L, "lack.old.password"),
    /**
     * 缺少原密码
     */
    OLD_PASSWORD_ERROR(400051L, "old.password.error"),
    /**
     * 更新密码失败
     */
    UPDATE_PASSWORD_FAILED(400052L, "update.password.failed"),
    /**
     * 用户未登录或登录超时
     */
    USER_NOT_LOGIN_OR_LOGIN_TIMEOUT(400053L, "user.not.login.or.login.timeout"),
    /**
     * 查询失败
     */
    QUERY_FAILED(400054L, "query.failed"),
    /**
     * 找不到任何记录
     */
    CANNOT_FIND_ANY_RECORD(400055L, "cannot.find.any.record"),


    //restful请求头错误码
    /**
     * 请求头缺少X-APP-ID参数
     */
    REQ_HEAD_LACK_X_APP_ID(410001L, "request.head.lack.x_app.id"),
    /**
     * 请求头缺少X-APP-VERSION参数
     */
    REQ_HEAD_LACK_X_APP_VERSION(410002L, "request.head.lack.x_app.version"),
    /**
     * 请求头缺少X-OS参数
     */
    REQ_HEAD_LACK_X_OS(410003L, "request.head.lack.x_os"),
    /**
     * 请求头缺少X-OS-VERSION参数
     */
    REQ_HEAD_LACK_X_OS_VERSION(410005L, "request.head.lack.x_os.version"),
    /**
     * 请求头缺少X-DEVICE-ID参数
     */
    REQ_HEAD_LACK_X_DEVICE_ID(410004L, "request.head.lack.x_device.id"),
    /**
     * 请求头缺少X-DEVICE-TYPE参数
     */
    REQ_HEAD_LACK_X_DEVICE_TYPE(410006L, "request.head.lack.x_device.type"),
    /**
     * 请求头缺少X-DEVICE-NAME参数
     */
    REQ_HEAD_LACK_X_DEVICE_NAME(410007L, "request.head.lack.x_device.name"),
    /**
     * 请求头缺少Access-Token参数
     */
    REQ_HEAD_LACK_ACCESS_TOKEN(410008L, "request.head.lack.access.token"),
    /**
     * 请求头缺少User-Agent参数
     */
    REQ_HEAD_LACK_USER_AGENT(410009L, "request.head.lack.user.agent"),
    /**
     * 未知的App信息
     */
    UNKNOWN_APP_INFO(410010L, "unknown.app.info"),

    //媒体错误码
    /**
     * 不合法的图片大小
     */
    ILLEGAL_IMAGE_SIZE(430001L, "illegal.image.size"),
    /**
     * 不合法的图片尺寸
     */
    ILLEGAL_IMAGE_CAPACITY(430002L, "illegal.image.capacity"),
    /**
     * 消息文本长度超过限制
     */
    MESSAGE_LENGTH_BEYOND_LIMIT(430003L, "message.length.beyond.limit"),
    /**
     * 链接字段超过限制
     */
    LINK_LENGTH_BEYOND_LIMIT(430004L, "link.length.beyond.limit"),
    /**
     * 图片链接文字长度超过限制
     */
    LINK_TEXT_LENGTH_BEYOND_LIMIT(430005L, "link.text.length.beyond.limit"),
    /**
     * 多媒体文件超过大小限制
     */
    ILLEGAL_MEDIA_SIZE(430006L, "illegal.media.size"),
    /**
     * 语音播放时长超过限制
     */
    VOICE_TIME_BEYOND_LIMIT(430007L, "voice.time.beyond.limit"),

    /**
     * API调用过于频繁，情稍后再试
     */
    API_CALLS_FREQUENTLY(440001L, "api.calls.frequently"),
    /**
     * URL请求路径不合法
     */
    ILLEGAL_URL(440002L, "illegal.url"),

    /**
     * 用户没有APP的使用权
     */
    NO_ACCESS_USE_APP(500000L, "no.access.use.app"),
    /**
     * 用户企业列表为空
     */
    USER_CORP_LIST_IS_EMPTY(500001L, "user.corp.list.is.empty"),
    /**
     * 用户没有加入任何企业
     */
    USER_DID_NOT_JOIN_ANY_CORP(500002L, "user.did.not.join.any.corp"),
    /**
     * 用户没有加入这个企业
     */
    USER_DID_NOT_JOIN_THE_CORP(500003L, "user.did.not.join.the.corp"),

    //RPC服务错误码
    /**
     * 终端管理中心服务不可用
     */
    MOP_SERVICE_UNAVAILABLE(600000L, "mop.service.unavailable"),
    /**
     * App消息服务不可用
     */
    MESSAGE_SERVICE_UNAVAILABLE(600010L, "message.service.unavailable"),
    /**
     * SSO服务请求超时
     */
    SSO_SERVICE_UNAVAILABLE(600020L, "sso.service.unavailable"),
    /**
     * SSO请求错误
     */
    SSO_SERVICE_REQUEST_ERROR(600021L, "sso.service.request.error"),
    /**
     * 通讯录服务不可用
     */
    ADDRESS_BOOK_SERVICE_UNAVAILABLE(600030L, "address.book.service.unavailable"),
    /**
     * 用户中心服务不可用
     */
    USER_CENTER_SERVICE_UNAVAILABLE(600040L, "user.center.service.unavailable"),
    /**
     * 平台消息中心不可用
     */
    SMS_SERVICE_UNAVAILABLE(600050L, "sms.service.unavailable"),
    /**
     * 消息推荐服务
     */
    NEWS_RECOMMEND_SERVICE(600060L,  "news.recommend.service.unavailable"),
    /**
     * 推荐交互服务
     */
    BIZ_COMMEND_SERVICE(600070L,  "biz.commend.service.unavailable"),
    /**
     * 推荐统计服务
     */
    RECOMMEND_SYSTEM_ESTIMATION(600080L,  "recommend.system.estimation.service.unavailable");




    /**
     * 错误码
     */
    private Long code;
    /**
     * 错误消息国际化字段名
     */
    private String message;

    ErrorCode(Long code, String message){
        this.code = code;
        this.message = message;
    }

    public Long code() {
        return this.code;
    }

    public String message() {
        return MessageStore.getMessage(LocaleHolder.get(), this.message);
    }

    /**
     * 根据枚举名称获取枚举对象
     * @param name
     * @return
     */
    public static ErrorCode getRestfulCode(String name){
        for (ErrorCode item : ErrorCode.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public static String getMessage(String name) {
        for (ErrorCode item : ErrorCode.values()) {
            if (item.name().equals(name)) {
                return item.message();
            }
        }
        return name;
    }

    public static Long getCode(String name) {
        for (ErrorCode item : ErrorCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }


}
