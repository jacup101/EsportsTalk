package com.jacup101.esportstalk;

public class SearchResult {

    private String type;
    private long numID;
    private String strID;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNumID() {
        return numID;
    }

    public void setNumID(long numID) {
        this.numID = numID;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    private String text;
    private Post post;

    public SearchResult(long id, String type, String text) {
        this.numID = id;
        this.type = type;
        this.text = text;
    }
    public SearchResult(long id, String type, String text, Post post) {
        this.numID = id;
        this.type = type;
        this.text = text;
        this.post = post;
    }

    public SearchResult(String id, String type, String text) {
        this.strID = id;
        this.type = type;
        this.text = text;
    }



}
