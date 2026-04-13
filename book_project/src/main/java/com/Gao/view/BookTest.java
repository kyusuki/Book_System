package com.Gao.view;

import com.Gao.entity.Book;
import com.Gao.entity.BorrowRecord;
import com.Gao.entity.Category;
import com.Gao.entity.User;
import com.Gao.util.ClearScreen;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class BookTest {
    private Scanner sc; //声明Scanner对象而不再创建
    private static BookManager mg=new BookManager(); //全局图书管理器
    private static User currentUser; //声明当前用户
    private static ClearScreen cs=new ClearScreen(); //声明清屏工具
    public BookTest(Scanner sc){
        this.sc=sc;
    }
    public void test(User u){
        currentUser=u;
        boolean isReturn=true;
        cs.cleanScreen(); //清屏
        System.out.println("登录成功，已进入"+(currentUser.getUserType()==1?"管理员":"用户")+"端口");
        while(isReturn){
            showMenu();
            int choice=sc.nextInt();
            cs.cleanScreen(); //清屏
            if(currentUser.getUserType()==0&&choice>=1&&choice<=3){
                System.out.println("无权限操作，请重新输入");
                continue;
            }
            switch(choice){
                case 1->{
                    addBook();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 2->{
                    updateBook();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 3->{
                    deleteBook();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 4->{
                    searchBook();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 5->{
                    borrowBook();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 6->{
                    returnBook();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 7->{
                    showBooks();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 8->{
                    countBooks();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 9->{
                    showBorrowRecord();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 10->{
                    categoryManager();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 0->{
                    System.out.println("已退出系统，返回主界面");
                    isReturn=false;
                    cs.cleanScreen(); //清屏
                }
                default->{
                    System.out.println("输入错误，请重新输入");
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
            }
            System.out.println();
        }
    }
    //菜单
    public void showMenu(){
        System.out.println("----图书管理系统----");
        if(currentUser.getUserType()==1){
            System.out.println("    1.添加图书");
            System.out.println("    2.修改图书信息");
            System.out.println("    3.删除图书");
            System.out.println("    4.查询图书");
            System.out.println("    5.借阅图书");
            System.out.println("    6.归还图书");
            System.out.println("    7.显示所有图书");
            System.out.println("    8.统计图书信息");
            System.out.println("    9.借阅记录查询");
            System.out.println("    10.图书分类管理");
            System.out.println("    0.退出系统");
            System.out.print("请输入功能（1-0）：");
        }
        else if(currentUser.getUserType()==0){
            System.out.println("    1.添加图书");
            System.out.println("    2.修改图书信息");
            System.out.println("    3.删除图书");
            System.out.println("    4.查询图书");
            System.out.println("    5.借阅图书");
            System.out.println("    6.归还图书");
            System.out.println("    7.显示所有图书");
            System.out.println("    8.统计图书信息");
            System.out.println("    9.我的借阅记录");
            System.out.println("    0.退出系统");
            System.out.print("请输入功能（4-9，0）：");
        }
    }
    //添加操作
    public void addBook(){
        System.out.println("----添加图书----");
        System.out.print("请输入ISBN号：");
        String isbn=sc.next();
        System.out.print("请输入书名：");
        String title=sc.next();
        System.out.print("请输入作者：");
        String author=sc.next();
        System.out.print("请输入出版社：");
        String publisher=sc.next();
        System.out.print("请输入出版日期（格式：YYYY-MM-DD）：");
        String publishDate=sc.next();
        System.out.print("请输入总藏书量（正整数）：");
        int totalCount=sc.nextInt();
        System.out.print("请输入当前库存（正整数）：");
        int availableCount=sc.nextInt();
        System.out.print("请输入图书分类编号（1.计算机类 2.文学类 3.理工类 4.经管类 5.社科类）：");
        int category_id=sc.nextInt();
        Book newBook=new Book(isbn,title,author,publisher,publishDate,totalCount,availableCount,category_id);
        boolean isSuccess=mg.addBook(newBook);
        if(isSuccess){
            System.out.println("添加成功");
        }
        else{
            System.out.println("添加失败,ISBN号已存在");
        }
    }
    //修改操作
    public void updateBook(){
        System.out.println("----修改图书信息----");
        System.out.print("请输入要修改的ISBN号：");
        String isbn=sc.next();
        List<Book> b=mg.searchBook(isbn, 1);
        if(b.isEmpty()){ //判断图书是否存在
            System.out.println("未找到该ISBN号的图书，修改失败");
            return;
        }
        System.out.print("请输入新书名：");
        String title=sc.next();
        System.out.print("请输入新作者：");
        String author=sc.next();
        System.out.print("请输入新出版社：");
        String publisher=sc.next();
        System.out.print("请输入新出版日期（格式：YYYY-MM-DD）：");
        String publishDate=sc.next();
        System.out.print("请输入新总藏书量（正整数）：");
        int totalCount=sc.nextInt();
        System.out.print("请输入新当前库存（正整数）：");
        int availableCount=sc.nextInt();
        System.out.print("请输入新的图书分类编号（1.计算机类 2.文学类 3.理工类 4.经管类 5.社科类）：");
        int category_id=sc.nextInt();
        Book newBook=new Book(isbn,title,author,publisher,publishDate,totalCount,availableCount,category_id);
        boolean isSuccess=mg.updateBook(isbn, newBook);
        if(isSuccess){
            System.out.println("修改成功");
        }
        else{
            System.out.println("修改失败，ISBN号不存在");
            return;
        }
    }
    //删除操作
    public void deleteBook(){
        System.out.println("----删除图书----");
        System.out.print("请输入要删除的ISBN号：");
        String isbn=sc.next();
        List<Book> b=mg.searchBook(isbn, 1);
        if(b.isEmpty()){
            System.out.println("未找到该ISBN号的图书，删除失败");
            return;
        }
        boolean isSuccess=mg.deleteBook(isbn);
        if(isSuccess){
            System.out.println("删除成功");
        }
        else{
            System.out.println("删除失败，ISBN号不存在");
            return;
        }
    }
    //查询操作
    public void searchBook(){
        System.out.println("----查询图书----");
        System.out.println("1.根据ISBN号查询");
        System.out.println("2.根据书名查询");
        System.out.println("3.根据分类查询");
        int choice=sc.nextInt();
        if(choice==1){
            System.out.print("请输入要查询的ISBN号：");
            String isbn=sc.next();
            List<Book> b=mg.searchBook(isbn,1);
            if(b.isEmpty()){
                System.out.println("未找到该ISBN号的图书");
                return;
            }
            else{
                for(int i=0;i<b.size();i++){
                    Book book=b.get(i);
                    printBook(book);
                }
            }
        }
        else if(choice==2){
            System.out.print("请输入要查询的书名：");
            String title=sc.next();
            List<Book> b=mg.searchBook(title, 2);
            if(b.isEmpty()){
                System.out.println("未找到该书名的图书");
                return;
            }
            else{
                for(int i=0;i<b.size();i++){
                    Book book=b.get(i);
                    printBook(book);
                }
            }
        }
        else if(choice==3){
            System.out.print("请输入要查询的分类（1.计算机类 2.文学类 3.理工类 4.经管类 5.社科类）：");
            String category_id=sc.next();
            List<Book> b=mg.searchBook(category_id,3);
            if(b.isEmpty()){
                System.out.println("未找到该分类的图书");
                return;
            }
            else{
                for(int i=0;i<b.size();i++){
                    Book book=b.get(i);
                    printBook(book);
                }
            }
        }
    }
    //借阅操作
    public void borrowBook(){
        System.out.println("----借阅图书----");
        System.out.print("请输入要借阅书的ISBN号；");
        String isbn=sc.next();
        String id=null;
        String name=null;
        if(currentUser.getUserType()==1){
            System.out.print("请输入借阅人用户名：");
            name=sc.next();
            System.out.print("请输入借阅人的身份证信息：");
            id=sc.next();
        }
        else if(currentUser.getUserType()==0){
            name=currentUser.getUsername();
            id=currentUser.getId();
        }
        boolean isSuccess=mg.borrowBook(isbn,name,id);
        if(isSuccess){
            System.out.println("借阅成功");
        }
        else{ //判断是没找到还是已借完
            List<Book> b=mg.searchBook(isbn,1);
            if(b.isEmpty()){
                System.out.println("借阅失败，未找到该ISBN对应的书");
            }
            else{
                System.out.println("借阅失败，该书已被借完");
            }
        }
    }
    //归还操作
    public void returnBook(){
        System.out.println("----归还图书----");
        System.out.print("请输入要归还书的ISBN号：");
        String isbn=sc.next();
        String id=null;
        if(currentUser.getUserType()==1){
            System.out.print("请输入归还人的学号：");
            id=sc.next();
        }
        else if(currentUser.getUserType()==0){
            id=currentUser.getId();
        }
        boolean isSuccess=mg.returnBook(isbn,id);
        if(isSuccess){
            System.out.println("归还成功");
        }
        else{
            System.out.println("归还失败");
        }
    }
    //显示所有图书
    public void showBooks(){
        System.out.println("----图书列表----");
        List<Book> bs=mg.getBookList();
        if(bs.isEmpty()){
            System.out.println("当前无图书");
            return;
        }
        for(int i=0;i<bs.size();i++){
            Book b=bs.get(i);
            printBook(b);
            System.out.println();
        }
    }
    //统计图书信息
    public void countBooks(){
        System.out.println("----图书统计----");
        List<Book> bs=mg.getBookList();
        int totalCount=0;
        int availableCount=0;
        int borrowCount=0;
        for(int i=0;i<bs.size();i++){
            totalCount++;
            Book b=bs.get(i);
            if(b.getAvailableCount()>0){
                availableCount++;
            }
        }
        borrowCount=totalCount-availableCount;
        System.out.println("总图书数量："+totalCount+"本");
        System.out.println("可借图书数量："+availableCount+"本");
        System.out.println("已借出图书数量："+borrowCount+"本");
    }
    //打印对应图书信息
    public void printBook(Book book){
        System.out.println("ISBN号："+book.getIsbn());
        System.out.println("书名："+book.getTitle());
        System.out.println("作者："+book.getAuthor());
        System.out.println("出版社："+book.getPublisher());
        System.out.println("出版日期："+book.getPublishDate());
        System.out.println("总藏书量："+book.getTotalCount()+"本");
        System.out.println("当前库存："+book.getAvailableCount()+"本");
        System.out.println("本书分类："+book.getCategory_name());
        System.out.println("当前状态："+(book.getAvailableCount()>0?"可借":"已全部借出"));
    }
    //展示借阅记录
    public void showBorrowRecord(){
        List<BorrowRecord> records=new ArrayList<>();
        if(currentUser.getUserType()==1){
            records=mg.getRecordList_root();
            System.out.println("管理员记录查询：");
            System.out.println();
        }
        else if(currentUser.getUserType()==0){
            records=mg.getRecordList_user(currentUser.getId());
            System.out.println("用户"+currentUser.getUsername()+"记录查询：");
            System.out.println();
        }
        if(records.isEmpty()){
            System.out.println("暂无借阅记录");
            return;
        }
        for(int i=0;i<records.size();i++){
            BorrowRecord r=records.get(i);
            String status=null;
            if(r.getStatus()==1){
                status="已归还";
            }
            else if(r.getStatus()==0){
                status="借出中";
            }
            System.out.println("----记录查询----");
            System.out.println("借阅人姓名："+r.getBorrowerName());
            System.out.println("借阅人学号："+r.getBorrowerId());
            System.out.println("书名："+r.getBookTitle());
            if(r.getBorrowDate()!=null){
                System.out.println("借出时间："+r.getBorrowDate().toLocalDate().toString());
            }
            else{
                System.out.println("未知状态");
            }
            if(r.getReturnDate()!=null){
                System.out.println("归还时间："+r.getReturnDate().toLocalDate().toString());
            }
            else{
                System.out.println("未归还");
            }
            System.out.println("状态："+status);
        }
    }

    //图书分类管理
    public void categoryManager(){
        boolean isReturn=true;
        while(isReturn){
            System.out.println("    1.列出全部分类");
            System.out.println("    2.新增分类");
            System.out.println("    3.修改分类");
            System.out.println("    4.删除分类");
            System.out.println("    0.返回菜单");

            System.out.print("请输入功能：");
            int choice=sc.nextInt();
            cs.cleanScreen();
            switch (choice){
                case 1->{
                    listCategory();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 2->{
                    addCategory();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 3->{
                    updateCategory();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 4->{
                    deleteCategory();
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
                case 0->{
                    System.out.println("已返回主菜单");
                    isReturn=false;
                    cs.cleanScreen(); //清屏
                }
                default->{
                    System.out.println("输入错误，请重新输入");
                    System.out.println("\n按回车键继续");
                    sc.nextLine(); //消耗换行符
                    sc.nextLine(); //等待用户回车
                    cs.cleanScreen(); //清屏
                }
            }
            System.out.println();
        }
    }
    //列出全部分类
    public void listCategory(){
        System.out.println("----分类信息----");
        List<Category> categories=mg.listCategories();
        if(categories.isEmpty()){
            System.out.println("无分类信息");
            return;
        }
        for(int i=0;i<categories.size();i++){
            Category category=categories.get(i);
            System.out.println(category);
        }
    }
    //新增分类
    public void addCategory(){
        System.out.print("请输入新增分类类名：");
        String name=sc.next();
        System.out.print("请输入新增分类描述：");
        String description=sc.next();
        boolean result=mg.addCategory(name,description);
        if(result){
            System.out.println("添加新分类成功");
        }
        else{
            System.out.println("添加新分类失败");
        }
    }
    //修改分类
    public void updateCategory(){
        System.out.print("请输入需修改分类id：");
        int id=sc.nextInt();
        System.out.print("请输入新分类类名：");
        String newName=sc.next();
        System.out.print("请输入新分类描述：");
        String newDescription=sc.next();
        boolean result=mg.updateCategory(id,newName,newDescription);
        if(result){
            System.out.println("修改分类成功");
        }
        else{
            System.out.println("修改分类失败");
        }
    }
    //删除分类
    public void deleteCategory(){
        System.out.print("请输入需删除分类的id：");
        int id=sc.nextInt();
        List<Book> books=mg.searchCategory(id);
        if(!(books.isEmpty())){
            System.out.println("该分类下仍有图书，删除失败");
            return;
        }
        boolean result=mg.deleteCategory(id);
        if(result){
            System.out.println("删除该分类成功");
        }
        else{
            System.out.println("删除该分类失败");
        }
    }
}
