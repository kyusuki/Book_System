package com.Gao.entity;

public class Book {
    private String isbn; //ISBN号
    private String title; //书名
    private String author; //作者
    private String publisher; //出版社
    private String publishDate; //出版日期
    private int totalCount; //总书量
    private int availableCount; //可借书量

    private int category_id; //图书分类id
    private String category_name; //图书分类类别

    public Book(){}
    public Book(String isbn,String title,String author,String publisher,String publishDate,int totalCount,int availableCount,int category_id){
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.publisher=publisher;
        this.publishDate=publishDate;
        this.totalCount=totalCount;
        this.availableCount=availableCount;
        this.category_id=category_id;
    }
    public void setIsbn(String isbn){
        this.isbn=isbn;
    }
    public String getIsbn(){
        return isbn;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public String getAuthor(){
        return author;
    }
    public void setPublisher(String publisher){
        this.publisher=publisher;
    }
    public String getPublisher(){
        return publisher;
    }
    public void setPublishDate(String publishDate){
        this.publishDate=publishDate;
    }
    public String getPublishDate(){
        return publishDate;
    }
    public void setTotalCount(int totalCount){
        this.totalCount=totalCount;
    }
    public int getTotalCount(){
        return totalCount;
    }
    public void setAvailableCount(int availableCount){
        this.availableCount=availableCount;
    }
    public int getAvailableCount(){
        return availableCount;
    }
    public void setCategory_id(int category_id){
        this.category_id=category_id;
    }
    public int getCategory_id(){
        return category_id;
    }
    public void setCategory_name(String category_name){
        this.category_name=category_name;
    }
    public String getCategory_name(){
        return category_name;
    }
}

