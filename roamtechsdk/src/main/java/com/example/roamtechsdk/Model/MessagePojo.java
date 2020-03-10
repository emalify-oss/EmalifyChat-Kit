package com.example.roamtechsdk.Model;

import com.google.firebase.database.Exclude;

import java.util.Date;

import androidx.annotation.Keep;

/**
 * Created by bliveinhack on 28/9/17.
 */
@Keep
public class MessagePojo  {

    /*...*/
    String id;
    String text;
    String name;
    String  file;

    String contact_id;
    String message;
    String setID;
    String channel;




    public String getImage() {
        return image!=null?image.trim():null;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

    public void setId(String id) {
        this.id = id;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(AuthorPojo user) {
        this.user = user;
    }

    Long createdAt;
    AuthorPojo user;


    public String getId() {
        return id;
    }


    public String getText() {
        return text;
    }


    public AuthorPojo getUser() {
        return user;
    }


    public Long getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Exclude
    public Message getM() {
//        data.put("author", "Dennis");
//        data.put("channel", "email");
//        data.put("contact_id", "Dennis");
//        data.put("file", "");
//        data.put("type", "Text");
//        data.put("meta",   Calendar.getInstance().getTime().getTime()  );
//        data.put("message", message);
//        data.put("created_at", Calendar.getInstance().getTime().getTime());
//        data.put("setID", senderId);
        Message m=new Message();
        m.setText(text);
        m.setId(id);
        m.setCreatedAt(new Date(createdAt));
        m.setName(name);
        m.setMessage(message);
        m.setContact_id(contact_id);
        m.setFile(file);
        //   m.setCreatedAt(new Date(createdAt));
        Author a=new Author();
        a.setName(name);
        a.setId(id);
        a.setAvatar("http://none.com");
        m.setUser(a);
        m.setImage(getImage());
        return m;
    }
}
