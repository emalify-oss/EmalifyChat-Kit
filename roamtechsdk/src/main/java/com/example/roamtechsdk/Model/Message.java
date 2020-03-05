package com.example.roamtechsdk.Model;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/**
 * Created by bliveinhack on 28/9/17.
 */

public class Message implements IMessage, MessageContentType.Image  {

    /*...*/
    String id;
    String text;
    String name;
    String file;
    String meta;
    String contact_id;
    String message;
    String setID;
    String channel;

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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(Author user) {
        this.user = user;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public void setMessage(String message) { this.message = message;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    Date createdAt;
    Author user;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Author getUser() {
        return user;
    }

    @Exclude
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getImageUrl() {
        return image!=null?image.trim():null;
    }

    public String getName() {
        return name;
    }
    public String getFile() {
        return file;
    }
    public String getMeta() {
        return meta;
    }
    public String getContact_id() {
        return contact_id;
    }
    public String getMessage() {
        return message;
    }
    public String getSetID() {
        return setID;
    }
    public String getChannel() {
        return channel;
    }
    public String getImage() {
        return image;
    }


}
