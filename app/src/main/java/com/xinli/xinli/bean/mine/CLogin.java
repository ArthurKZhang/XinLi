package com.xinli.xinli.bean.mine;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class CLogin {
    /*
    属性名	中文	格式	举例	备注
上传信息
name	    用户名	String	pang
password	密码	String	123321
identity	身份类型	String	student(或teacher )	只有两种
     */
    private String name;
    private String password;
    private String identity;

    public CLogin(String name, String password, String identity) {
        this.name = name;
        this.password = password;
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "CLogin{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", identity='" + identity + '\'' +
                '}';
    }
}
