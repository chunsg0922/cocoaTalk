package com.samil.cocoatalk;


public class Member {

    private String memberUid;
    private String memberID;
    private String memberPassword;
    private String memberName;
    private String memberPhone;
    private String memberImg;
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

    public String getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(String memberUid) {
        this.memberUid = memberUid;
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