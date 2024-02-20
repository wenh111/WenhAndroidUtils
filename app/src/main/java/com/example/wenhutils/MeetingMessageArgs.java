package com.example.wenhutils;

import java.util.List;

public class MeetingMessageArgs {
    private long meetingStartTime;
    private String commitKey;
    private long meetingEndTime;
    private List<String> roomKeyList;
    private String conferenceKey;
    private String type;

    public long getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(long meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommitKey() {
        return commitKey;
    }

    public void setCommitKey(String commitKey) {
        this.commitKey = commitKey;
    }

    public long getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(long meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public List<String> getRoomKeyList() {
        return roomKeyList;
    }

    public void setRoomKeyList(List<String> roomKeyList) {
        this.roomKeyList = roomKeyList;
    }

    public String getConferenceKey() {
        return conferenceKey;
    }

    public void setConferenceKey(String conferenceKey) {
        this.conferenceKey = conferenceKey;
    }

    @Override
    public String toString() {
        return "MeetingMessageArgs{" +
                "meetingStartTime=" + meetingStartTime +
                ", commitKey='" + commitKey + '\'' +
                ", meetingEndTime=" + meetingEndTime +
                ", roomKeyList=" + roomKeyList +
                ", conferenceKey='" + conferenceKey + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}