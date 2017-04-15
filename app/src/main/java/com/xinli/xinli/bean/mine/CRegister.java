package com.xinli.xinli.bean.mine;

import java.util.Date;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class CRegister {
    private String name;
    private String password;
    private String institution;
    private String identity;
    private Date enrollmentDate;

    public CRegister(String name, String password, String institution, String identity, Date enrollmentDate) {
        this.name = name;
        this.password = password;
        this.institution = institution;
        this.identity = identity;
        this.enrollmentDate = enrollmentDate;
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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "CRegister{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", institution='" + institution + '\'' +
                ", identity='" + identity + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
