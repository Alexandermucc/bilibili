package com.alex.domain;

/**
 * @author Alexandermucc
 * @date 2022/05/14 13:17
 **/
public class JsonResponse<T> {

    private String code;

    private String msg;

    private T data;

    public JsonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data) {
        this.data = data;
        msg = "成功";
        code = "0";
    }

    // 返回前端,但不需要数据的场景
    public static JsonResponse<String> success() {
        return new JsonResponse<>(null);
    }

    // 返回前端,但不需要数据的场景
    public static JsonResponse<String> success(String data) {
        return new JsonResponse<>(data);
    }

    /**
     * 通用返回信息
     * @return
     */
    public static JsonResponse<String> fail() {
        return new JsonResponse<>("1","失败");
    }

    /**
     * 返回特定 状态码 和 信息 给前端
     * @param code
     * @param msg
     * @return
     */
    public static JsonResponse<String> fail(String code, String msg) {
        return new JsonResponse<>(code, msg);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

