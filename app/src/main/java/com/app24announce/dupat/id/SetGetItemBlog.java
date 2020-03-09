package com.app24announce.dupat.id;

public class SetGetItemBlog {

    String nameUser,dateUpload,photoUser,photoBlog,titleBlog;

    public SetGetItemBlog(String nameUser, String dateUpload, String photoUser, String photoBlog, String titleBlog) {
        this.nameUser = nameUser;
        this.dateUpload = dateUpload;
        this.photoUser = photoUser;
        this.photoBlog = photoBlog;
        this.titleBlog = titleBlog;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getDateUpload() {
        return dateUpload;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public String getPhotoBlog() {
        return photoBlog;
    }

    public String getTitleBlog() {
        return titleBlog;
    }
}
