package com.example.wenhutils;

public class MeetingMessageBean {
    private String meetingName;
    private String meetingTime;
    private String meetingContent;

    public MeetingMessageBean(String meetingName, String meetingTime, String meetingContent) {
        this.meetingName = meetingName;
        this.meetingTime = meetingTime;
        this.meetingContent = meetingContent;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }
}
