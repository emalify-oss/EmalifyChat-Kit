package com.example.roamtechsdk.Model;

import com.google.firebase.database.Exclude;

import java.sql.Date;

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


    String type;


    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    String meta;
    String email;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    String author;

    public Long getCustomer_account() {
        return customer_account;
    }

    public void setCustomer_account(Long customer_account) {
        this.customer_account = customer_account;
    }

    Long customer_account;
    String date;


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    String created_at;



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



    public void setUser(AuthorPojo user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    String createdAt;
    AuthorPojo user;


    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    String message_id;


    public String getId() {
        return id;
    }


    public String getText() {
        return text;
    }


    public AuthorPojo getUser() {
        return user;
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

        Message m=new Message();
        m.setText(message);
        // sort
        if(id == null)
        {
            m.setId("id");
        }else
            m.setId(id);
        m.setCreatedAt(new Date(Long.parseLong(message_id)*1000));
        m.setName("name");
        m.setMessage(message);
        m.setContact_id(contact_id);
        m.setFile(file);
        Author a=new Author();
        a.setName(name);
        if(id == null)
        {
            a.setId("id");
        }else
            a.setId(id);
        a.setAvatar(getImage());
        m.setUser(a);
        m.setImage(getImage());
        return m;
    }
}
