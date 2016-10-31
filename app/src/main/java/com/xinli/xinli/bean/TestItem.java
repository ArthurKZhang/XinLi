package com.xinli.xinli.bean;

import java.util.List;

/**
 * Created by zhangyu on 10/14/16.
 * 这代表一套测试题中的 一道题 的类
 */
public class TestItem {
    //题目内容
    private String descriptions;
    //回答类型,TestItemHelper中的CHOOSE_ONE or CHOOSE_MULTIPLE
    //用于在加载题目的时候提供判断标准
    private int answerType;
    //选项数量
    private int chooseNum;
    //存储选项的list
    private List<String> chooseItems;

    public TestItem(String descriptions, int answerType, int chooseNum, List<String> chooseItems) {
        this.descriptions = descriptions;
        this.answerType = answerType;
        this.chooseNum = chooseNum;
        this.chooseItems = chooseItems;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public int getChooseNum() {
        return chooseNum;
    }

    public void setChooseNum(int chooseNum) {
        this.chooseNum = chooseNum;
    }

    public List<String> getChooseItems() {
        return chooseItems;
    }

    public void setChooseItems(List<String> chooseItems) {
        this.chooseItems = chooseItems;
    }
}
