package com.Gao.entity;

public class Category {
    private int id; //图书分类id
    private String name; //图书分类类别
    private String description; //分类描述
    public Category(){}
    public Category(int id,String name,String description){
        this.id=id;
        this.name=name;
        this.description=description;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    @Override
    public String toString(){
        return "编号："+getId()+" 类名："+getName()+" 描述："+getDescription();
    }
}
