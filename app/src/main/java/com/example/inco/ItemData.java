package com.example.inco;
public class ItemData {
    public String title;
    public String content;
    public String firebaseKey;
    public String dateStr;
    public ItemData(String key, String title, String content,String dateStr) {
        this.firebaseKey = key;
        this.title = title;
        this.content = content;
        this.dateStr = dateStr;
    }
    public ItemData() {
    }
    public String getFirebaseKey() {
        return firebaseKey;
    }
    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String context) {
        this.content = context;
    }
    public String getDateStr() {
        return dateStr;
    }
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}