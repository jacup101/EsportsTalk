package com.jacup101.esportstalk;

public class Comment {
    private String user;
    private String text;
    private long postID;

    public Comment(String user, String text, long postID) {
        this.user = user;
        this.text = text;
        this.postID = postID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }
}
