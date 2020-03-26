package com.xpzheng.watermark;

/**
 * 水印生成自定义异常，通过分析错误码可得知错误原因
 * 
 * @author xpzheng
 *
 */
public class WatermarkException extends RuntimeException {

    private static final long serialVersionUID = -3443953033009813691L;

    public static final int ERR_UNDEFINED = 0; // 未定义错误
    public static final int ERR_SOURCE_NOT_FOUND = 1; // 源文件找不到
    public static final int ERR_TARGET_NOT_FOUND = 2; // 目标文件找不到
    public static final int ERR_UNSUPPORTED = 3; // 不支持添加水印
    public static final int ERR_READ_WRITE_ERROR = 4; // 读写错误
    public static final int ERR_INNER_ERROR = 5; // 内部错误(如pdf的DocumentException，cmd命令执行错误，图片解析错误，jacob桥接错误等等)
    public static final int ERR_WATERMARK_INVALID = 6; // 水印不合法

    private int errorCode;

    public WatermarkException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("[").append(errorCode).append("]");
        switch (errorCode) {
        case ERR_UNDEFINED:
            sb.append("未定义错误");
            break;
        case ERR_SOURCE_NOT_FOUND:
            sb.append("源文件未找到");
            break;
        case ERR_TARGET_NOT_FOUND:
            sb.append("目标文件错误");
            break;
        case ERR_UNSUPPORTED:
            sb.append("不支持的文件类型");
            break;
        case ERR_READ_WRITE_ERROR:
            sb.append("读写错误");
            break;
        case ERR_INNER_ERROR:
            sb.append("内部错误");
            break;
        case ERR_WATERMARK_INVALID:
            sb.append("非法水印");
            break;
        default:
            break;
        }
        return sb.toString();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
