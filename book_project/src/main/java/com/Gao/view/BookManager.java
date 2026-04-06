package com.Gao.view;

import com.Gao.entity.Book;
import com.Gao.entity.BorrowRecord;
import com.Gao.util.DBHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class BookManager {
    private List<Book> bookList=new ArrayList<>(); //所有图书列表
    private List<BorrowRecord> recordList=new ArrayList<>(); //所有借阅记录列表
    //添加图书
    public boolean addBook(Book book){
        String sql="insert into book(isbn, title, author, publisher, publish_date, total_count, available_count) values(?,?,?,?,?,?,?)";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,book.getIsbn());
            ps.setString(2,book.getTitle());
            ps.setString(3,book.getAuthor());
            ps.setString(4,book.getPublisher());
            ps.setDate(5,Date.valueOf(book.getPublishDate()));
            ps.setInt(6,book.getTotalCount());
            ps.setInt(7,book.getAvailableCount());
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //修改图书信息
    public boolean updateBook(String isbn,Book newBook){
        String sql="update book set title=?,author=?,publisher=?,publish_date=?,total_count=?,available_count=? where isbn=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,newBook.getTitle());
            ps.setString(2,newBook.getAuthor());
            ps.setString(3,newBook.getPublisher());
            ps.setDate(4,Date.valueOf(newBook.getPublishDate()));
            ps.setInt(5,newBook.getTotalCount());
            ps.setInt(6,newBook.getAvailableCount());
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //删除图书
    public boolean deleteBook(String isbn){
        String sql="delete from book where isbn=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,isbn);
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //查询图书
    public List<Book> searchBook(String keyword,boolean isbnSearch){
        List<Book> result=new ArrayList<>(); //创建结果列表
        String sql;
        if(isbnSearch){
            sql="select * from book where isbn=?";
        }
        else{
            sql="select * from book where title like ?";
        }
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            if(isbnSearch){
                ps.setString(1,keyword);
            }
            else{
                ps.setString(1,"%"+keyword+"%");
            }
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Book book=new Book();
                book.setIsbn(rs.getString("isbn"));
                book.setAuthor(rs.getString("author"));
                book.setTitle(rs.getString("title"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishDate(rs.getDate("publish_date").toString());
                book.setTotalCount(rs.getInt("total_count"));
                book.setAvailableCount(rs.getInt("available_count"));
                result.add(book);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    //借阅图书
    public boolean borrowBook(String isbn,String borrowerName,String borrowerId){
        Connection conn=null;
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        try{
            conn=DBHelper.getConnection();
            conn.setAutoCommit(false);
            //检查库存
            String Check_sql="select available_count from book where isbn=? for update";
            ps1=conn.prepareStatement(Check_sql);
            ps1.setString(1,isbn);
            ResultSet rs=ps1.executeQuery();
            if(!rs.next()||rs.getInt(1)<=0){
                conn.rollback();
                return false;
            }
            //更新库存
            String Update_sql="update book set available_count=available_count-1 where isbn=? and available_count>0";
            ps2=conn.prepareStatement(Update_sql);
            ps2.setString(1,isbn);
            int update=ps2.executeUpdate();
            if(update==0){
                conn.rollback();
                return false;
            }
            //借阅记录
            String Insert_sql="insert into borrow_record(borrow_name,borrow_id,book_isbn,borrow_date,return_date,status) values(?,?,?,?,?,0)";
            ps2=conn.prepareStatement(Insert_sql);
            ps2.setString(1,borrowerName);
            ps2.setString(2,borrowerId);
            ps2.setString(3,isbn);
            LocalDateTime now=LocalDateTime.now();
            ps2.setTimestamp(4,Timestamp.valueOf(now));
            ps2.setTimestamp(5,Timestamp.valueOf(now.plusDays(30)));
            ps2.executeUpdate();
            conn.commit();
            return true;
        }catch (SQLException e){
            if(conn!=null){
                try{
                    conn.rollback();
                }catch (SQLException ex){}
            }
            e.printStackTrace();
            return false;
        }finally {
            DBHelper.close(conn,null,null);
            try{
                if(ps1!=null){
                    ps1.close();
                }
            }catch (SQLException e){}
            try{
                if(ps2!=null){
                    ps2.close();
                }
            }catch (SQLException e){}
        }
    }
    //归还图书
    public boolean returnBook(String isbn,String borrowerId){
        Connection conn=null;
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        try{
            conn=DBHelper.getConnection();
            conn.setAutoCommit(false);
            //查询未归还记录
            String Search_sql="select id from borrow_record where book_isbn=? and borrow_id=? and status=0 for update";
            ps1=conn.prepareStatement(Search_sql);
            ps1.setString(1,isbn);
            ps1.setString(2,borrowerId);
            ResultSet rs=ps1.executeQuery();
            if(!rs.next()){
                conn.rollback();
                return false;
            }
            //更新借阅记录
            String Update1_sql="update borrow_record set return_date=?,status=1 where book_isbn=? and borrow_id=? and status=0";
            ps2=conn.prepareStatement(Update1_sql);
            LocalDateTime now=LocalDateTime.now();
            ps2.setTimestamp(1,Timestamp.valueOf(now));
            ps2.setString(2,isbn);
            ps2.setString(3,borrowerId);
            ps2.executeUpdate();
            //更新库存
            String Update2_sql="update book set available_count=available_count+1 where isbn=?";
            ps2=conn.prepareStatement(Update2_sql);
            ps2.setString(1,isbn);
            ps2.executeUpdate();
            conn.commit();
            return true;
        }catch (SQLException e){
            if(conn!=null){
                try{
                    conn.rollback();
                }catch (SQLException es){}
            }
            e.printStackTrace();
            return false;
        }finally {
            DBHelper.close(conn,null,null);
            try{
                if(ps1!=null){
                    ps1.close();
                }
            }catch (SQLException e){}
            try{
                if(ps2!=null){
                    ps2.close();
                }
            }catch (SQLException e){}
        }
    }
    //获取图书列表
    public List<Book> getBookList(){
//        return bookList;
        List<Book> result=new ArrayList<>();
        String sql="select * from book";
        try(Connection conn=DBHelper.getConnection();Statement stmt=conn.createStatement();ResultSet rs=stmt.executeQuery(sql)){
            while(rs.next()){
                Book book=new Book();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishDate(rs.getDate("publish_date").toString());
                book.setTotalCount(rs.getInt("total_count"));
                book.setAvailableCount(rs.getInt("available_count"));
                result.add(book);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    //用户获取借阅记录列表
    public List<BorrowRecord> getRecordList_user(String borrowerId){
        List<BorrowRecord> records=new ArrayList<>();
        String sql="select r.*,b.title as book_title from borrow_record as r left join book as b on r.book_isbn=b.isbn where r.borrow_id=? order by borrow_date desc";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,borrowerId);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                BorrowRecord record=new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setBorrowerName(rs.getString("borrow_name"));
                record.setBorrowerId(rs.getString("borrow_id"));
                record.setBookIsbn(rs.getString("book_isbn"));
                record.setBookTitle(rs.getString("book_title"));
                Timestamp borrow_ts=rs.getTimestamp("borrow_date");
                if(borrow_ts!=null){
                    record.setBorrowDate(borrow_ts.toLocalDateTime());
                }
                Timestamp return_ts=rs.getTimestamp("return_date");
                if(return_ts!=null){
                    record.setReturnDate(return_ts.toLocalDateTime());
                }
                record.setStatus(rs.getInt("status"));
                records.add(record);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return records;
    }
    //管理员获取记录
    public List<BorrowRecord> getRecordList_root(){
        List<BorrowRecord> records=new ArrayList<>();
        String sql="select r.*,b.title as book_title from borrow_record as r left join book as b on r.book_isbn=b.isbn order by r.borrow_date desc";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps= conn.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                BorrowRecord record=new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setBorrowerName(rs.getString("borrow_name"));
                record.setBorrowerId(rs.getString("borrow_id"));
                record.setBookIsbn(rs.getString("book_isbn"));
                record.setBookTitle(rs.getString("book_title"));
                Timestamp borrow_ts=rs.getTimestamp("borrow_date");
                if(borrow_ts!=null){
                    record.setBorrowDate(borrow_ts.toLocalDateTime());
                }
                Timestamp return_ts=rs.getTimestamp("return_date");
                if(return_ts!=null){
                    record.setReturnDate(return_ts.toLocalDateTime());
                }
                record.setStatus(rs.getInt("status"));
                records.add(record);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return records;
    }
}
