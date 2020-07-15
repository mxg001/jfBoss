package cn.eeepay.framework.model.sys;

/**
 * Created by Administrator on 2019/7/8/008.
 * @author  liuks
 * 用户 数据权限
 */
public class UserBusinessNo {

    private String userName;//用户名
    private String businessNos;//业务权限

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBusinessNos() {
        return businessNos;
    }

    public void setBusinessNos(String businessNos) {
        this.businessNos = businessNos;
    }
}
