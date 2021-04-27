package com.jacup101.esportstalk;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class Post implements Comparable<Post> {
    private String title;
    private String content;
    private String user;
    private long id;
    private String type;
    private String community;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    private Uri imageUri = null;
    private String videoID;



    private List<Comment> comments;
    private String commentString;

    public Post(String title, String content, String user, long id, String type, String community, String date) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.id = id;
        this.type = type;
        this.community = community;
        this.comments = new ArrayList<Comment>();
        this.commentString = "null";
        this.date = date;
    }
    public Post(String title, String content, String user, long id, String type, String community, String date, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.id = id;
        this.type = type;
        this.community = community;
        this.comments = comments;
        this.date = date;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Content: " + content + ", User: " + user + ", Type: " + type + ", Community: " + community;
    }
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri= imageUri;
    }
    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public void parseCommentsFromString(String commentString) {
        while(commentString.indexOf("$E") != -1) {
            String sub = commentString.substring(commentString.indexOf("$CM"),commentString.indexOf("$E")+2);
            Log.d("substring_attempt", sub);
            String user = sub.substring(sub.indexOf("$U:" + 3),sub.indexOf("$P:"));
            String commentText = sub.substring(sub.indexOf("$P:" +3),sub.indexOf("$E"));
            Comment comment = new Comment(user, commentText, this.id);
            comments.add(comment);
            commentString = commentString.substring(commentString.indexOf("$E") + 2);
        }


    }

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }
    @Override public int compareTo(Post post)
    {
        long id = ((Post)post).getId();

        //  For Ascending order
        return (int) (id - this.id);

    }

}
