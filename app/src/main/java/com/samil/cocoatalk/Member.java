package com.samil.cocoatalk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {

    @SerializedName("memberID")
    @Expose
    private String memberID;
    @SerializedName("memberPassword")
    @Expose
    private String memberPassword;
    @SerializedName("memberName")
    @Expose
    private String memberName;
    @SerializedName("memberPhone")
    @Expose
    private String memberPhone;
    @SerializedName("memberImg")
    @Expose
    private String memberImg;
    @SerializedName("memberMsg")
    @Expose
    private String memberMsg;

    public Member(String memberPhone){
        this.memberPhone = memberPhone;
    }

    public Member(String memberID, String memberPassword, String memberName, String memberPhone, String memberImg, String memberMsg) {
        this.memberID = memberID;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.memberImg = memberImg;
        this.memberMsg = memberMsg;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberImg() {
        return memberImg;
    }

    public void setMemberImg(String memberImg) {
        this.memberImg = memberImg;
    }

    public String getMemberMsg() {
        return memberMsg;
    }

    public void setMemberMsg(String memberMsg) {
        this.memberMsg = memberMsg;
    }

    @Override
    public String toString(){
        return "Member{" +
                "memberId" + memberID + '\'' +
                "memberPassword" + memberPassword + '\'' +
                "memberName" + memberName + '\'' +
                "memberhone" + memberPhone + '\'' +
                "memberImg" + memberImg + '\'' +
                "memberMsg" + memberMsg + '\'' +
                '}';


    }
}