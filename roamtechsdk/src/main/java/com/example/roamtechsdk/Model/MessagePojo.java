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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    String type;

    public Long getMeta() {
        return meta;
    }

    public void setMeta(Long meta) {
        this.meta = meta;
    }

    Long meta;
    String email;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCustomer_account() {
        return customer_account;
    }

    public void setCustomer_account(String customer_account) {
        this.customer_account = customer_account;
    }

    String author;
    String customer_account;
    String date;

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    Long created_at;

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    Long message_id;
    String isAdmin;

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getSetID() {
        return setID;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    String setID;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

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
        a.setAvatar(getImage());
        m.setUser(a);
        m.setImage(getImage());
        return m;
    }
}
