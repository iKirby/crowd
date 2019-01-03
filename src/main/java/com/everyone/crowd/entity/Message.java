package com.everyone.crowd.entity;

public class Message {
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_SECONDARY = "secondary";
    public static final String TYPE_INFO = "info";
    public static final String TYPE_SUCCESS = "success";
    public static final String TYPE_WARNING = "warning";
    public static final String TYPE_DANGER = "danger";

    private String type;
    private String content;

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
