package app.ij.mlwithtensorflowlite;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String name, date, classImg;
    private int id;
    private byte[] image;


    public UserModel() {

    }

    public UserModel(int id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public UserModel(int id, byte[] image, String classImage, String date) {
        this.id = id;
        this.image = image;
        this.classImg = classImage;
        this.date = date;
    }

    public UserModel(int id, byte[] image, String classImage) {
        this.id = id;
        this.image = image;
        this.classImg = classImage;
    }

    public String getClassImg() {
        return classImg;
    }

    public void setClassImg(String classImg) {
        this.classImg = classImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getImage() {

        return image;
    }

    public void setImage(byte[] image) {

        this.image = image;
    }
}