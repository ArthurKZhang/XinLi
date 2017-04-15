package com.xinli.xinli.bean.mine;

import java.util.Date;

/**
 * Created by zhangyu on 21/02/2017.
 * 属性名	中文	格式	举例	备注
 * result	        注册结果	String	success(或fail)	只有两种结果
 * _id	            唯一标识	String	5858e8ad51d66405d53ec94f	mongoDB自动生成
 * institution	    所在机构	String	USTC
 * enrollmentDate	入学日期	Date	2016-09-01	yyyy-MM-dd
 */
public class SLogin {
//    属性名	中文	格式	举例	备注
//    result	        注册结果	String	success(或fail)	只有两种结果
//    _id	            唯一标识	String	5858e8ad51d66405d53ec94f	mongoDB自动生成
//    institution	    所在机构	String	USTC
//    enrollmentDate	入学日期	Date	2016-09-01	yyyy-MM-dd

    private String result;
    private String _id;
    private String institution;
    private Date enrollmentDate;

    public SLogin(String result, String _id, String institution, Date enrollmentDate) {
        this.result = result;
        this._id = _id;
        this.institution = institution;
        this.enrollmentDate = enrollmentDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "SLogin{" +
                "result='" + result + '\'' +
                ", _id='" + _id + '\'' +
                ", institution='" + institution + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
