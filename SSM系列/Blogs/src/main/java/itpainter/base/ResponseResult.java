package itpainter.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseResult {

    private boolean success;
    private Object data;
    private String code;
    private String message;

    private ResponseResult(){}
    //正常返回业务数据
    public static ResponseResult ok(Object o){
        ResponseResult r = new ResponseResult();
        r.success = true;
        r.data = o;
        return r;
    }
    //出现异常时，返回的数据
    public static ResponseResult error(){
        return error("ERR000000", "未知的错误，请联系管理员");
    }
    public static ResponseResult error(String code, String message){
        ResponseResult r = new ResponseResult();
        r.code = code;
        r.message = message;
        return r;
    }
}
