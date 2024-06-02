package com.example.profiledatabase.data;
//каждый элемент recicler_view_item представляет собой экземпляр класса Profile, который хранится в коллекции данных
public class Profile {
    public long _id;
    public String sName, name;
    public int age;
    public int photo;

    //конструктор
    public Profile(){}

    public Profile(long _id, String sName, String name, int age, int photo) {
        this._id = _id;
        this.sName = sName;
        this.name = name;
        this.age = age;
        this.photo = photo;
    }

    public Profile(String sName, String name, int age, int photo) {
        this.sName = sName;
        this.name = name;
        this.age = age;
        this.photo = photo;
    }
}
