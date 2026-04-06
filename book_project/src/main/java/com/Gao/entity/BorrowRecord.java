package com.Gao.entity;

import java.time.LocalDateTime;

public class BorrowRecord {
    private String borrowerName; //借阅人姓名
    private String borrowerId; //借阅人学号
    private String bookIsbn; //所借书ISBN号
    private LocalDateTime borrowDate; //借阅时间
    private LocalDateTime returnDate; //归还时间
    private int id; //记录id
    private String bookTitle; //书名
    private int status; //借出状态 0：借出 1：归还
    public BorrowRecord(){}
    public BorrowRecord(String borrowerName,String borrowerId,String bookIsbn,String bookTitle,LocalDateTime borrowDate){
        this.borrowerName=borrowerName;
        this.borrowerId=borrowerId;
        this.bookIsbn=bookIsbn;
        this.bookTitle=bookTitle;
        this.borrowDate=borrowDate;
        this.returnDate=null; //初始默认未归还
        this.status=0; //默认状态为借出
    }
    public void setBorrowerName(String borrowerName){
        this.borrowerName=borrowerName;
    }
    public String getBorrowerName(){
        return borrowerName;
    }
    public void setBorrowerId(String borrowerId){
        this.borrowerId=borrowerId;
    }
    public String getBorrowerId(){
        return borrowerId;
    }
    public void setBookIsbn(String bookIsbn){
        this.bookIsbn=bookIsbn;
    }
    public String getBookIsbn(){
        return bookIsbn;
    }
    public void setBorrowDate(LocalDateTime borrowDate){
        this.borrowDate=borrowDate;
    }
    public LocalDateTime getBorrowDate(){
        return borrowDate;
    }
    public void setReturnDate(LocalDateTime returnDate){
        this.returnDate=returnDate;
    }
    public LocalDateTime getReturnDate(){
        return returnDate;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
