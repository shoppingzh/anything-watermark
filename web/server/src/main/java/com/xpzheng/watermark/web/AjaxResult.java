/**
 * 
 */
package com.xpzheng.watermark.web;

/**
 * @author xpzheng
 *
 */
public class AjaxResult {

    private boolean success;
    private int errorCode;
    private String msg;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AjaxResult [success=" + success + ", errorCode=" + errorCode + ", msg=" + msg + ", data=" + data + "]";
    }

    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setSuccess(true);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult failure(int errorCode) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setSuccess(false);
        ajaxResult.setErrorCode(errorCode);
        return ajaxResult;
    }

}
