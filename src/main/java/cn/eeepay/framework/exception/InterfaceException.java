package cn.eeepay.framework.exception;

/**
 * Created by Administrator on 2019/7/17/017.
 * @author liuks
 * 接口请求异常类
 */
public class InterfaceException extends RuntimeException{

    //异常信息
    private String message;

    //构造函数
    public InterfaceException(String message){
        super(message);
        this.message = message;
    }
}
