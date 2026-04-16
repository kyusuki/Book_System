package com.Gao.web.controller;

import com.Gao.entity.Book;
import com.Gao.entity.BorrowRecord;
import com.Gao.entity.Category;
import com.Gao.entity.User;
import com.Gao.view.BookManager;
import com.Gao.web.config.SessionAuthInterceptor;
import com.Gao.web.dto.ApiResponse;
import com.Gao.web.dto.BookStatsDto;
import com.Gao.web.dto.BorrowRequest;
import com.Gao.web.dto.ReturnBookRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookApiController {

    private final BookManager bookManager = new BookManager();

    private User requireUser(HttpSession session) {
        User u = (User) session.getAttribute(SessionAuthInterceptor.SESSION_USER_KEY);
        if (u == null) {
            throw new IllegalStateException("未登录");
        }
        return u;
    }

    private boolean isAdmin(User u) {
        return u.getUserType() == 1;
    }

    @GetMapping("/books")
    public ApiResponse<List<Book>> listBooks(HttpSession session) {
        requireUser(session);
        return ApiResponse.ok(bookManager.getBookList());
    }

    @GetMapping("/books/search")
    public ApiResponse<List<Book>> search(
            HttpSession session,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "isbn") String mode) {
        requireUser(session);
        int flag;
        if ("title".equalsIgnoreCase(mode)) {
            flag = 2;
        } else if ("category".equalsIgnoreCase(mode)) {
            flag = 3;
        } else {
            flag = 1;
        }
        return ApiResponse.ok(bookManager.searchBook(keyword, flag));
    }

    @PostMapping("/books")
    public ApiResponse<Void> addBook(HttpSession session, @RequestBody Book book) {
        User u = requireUser(session);
        if (!isAdmin(u)) {
            return ApiResponse.fail("无权限");
        }
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            return ApiResponse.fail("ISBN 不能为空");
        }
        boolean ok = bookManager.addBook(book);
        return ok ? ApiResponse.ok("添加成功", null) : ApiResponse.fail("添加失败（ISBN 可能已存在）");
    }

    @PutMapping("/books/{isbn}")
    public ApiResponse<Void> updateBook(HttpSession session, @PathVariable String isbn, @RequestBody Book book) {
        User u = requireUser(session);
        if (!isAdmin(u)) {
            return ApiResponse.fail("无权限");
        }
        book.setIsbn(isbn);
        boolean ok = bookManager.updateBook(isbn, book);
        return ok ? ApiResponse.ok("更新成功", null) : ApiResponse.fail("更新失败");
    }

    @DeleteMapping("/books/{isbn}")
    public ApiResponse<Void> deleteBook(HttpSession session, @PathVariable String isbn) {
        User u = requireUser(session);
        if (!isAdmin(u)) {
            return ApiResponse.fail("无权限");
        }
        boolean ok = bookManager.deleteBook(isbn);
        return ok ? ApiResponse.ok("删除成功", null) : ApiResponse.fail("删除失败");
    }

    @PostMapping("/books/borrow")
    public ApiResponse<Void> borrow(HttpSession session, @RequestBody BorrowRequest req) {
        User u = requireUser(session);
        String isbn = req.getIsbn();
        if (isbn == null || isbn.isBlank()) {
            return ApiResponse.fail("ISBN 不能为空");
        }
        String name;
        String borrowerId;
        if (isAdmin(u)) {
            if (req.getBorrowerName() == null || req.getBorrowerName().isBlank()
                    || req.getBorrowerId() == null || req.getBorrowerId().isBlank()) {
                return ApiResponse.fail("管理员代借需填写借阅人姓名与证件号");
            }
            name = req.getBorrowerName().trim();
            borrowerId = req.getBorrowerId().trim();
        } else {
            name = u.getUsername();
            borrowerId = u.getId();
        }
        boolean ok = bookManager.borrowBook(isbn.trim(), name, borrowerId);
        return ok ? ApiResponse.ok("借阅成功", null) : ApiResponse.fail("借阅失败（无此书或已无库存）");
    }

    @PostMapping("/books/return")
    public ApiResponse<Void> returnBook(HttpSession session, @RequestBody ReturnBookRequest req) {
        User u = requireUser(session);
        String isbn = req.getIsbn();
        if (isbn == null || isbn.isBlank()) {
            return ApiResponse.fail("ISBN 不能为空");
        }
        String borrowerId;
        if (isAdmin(u)) {
            if (req.getBorrowerId() == null || req.getBorrowerId().isBlank()) {
                return ApiResponse.fail("管理员代还需填写借阅人证件号");
            }
            borrowerId = req.getBorrowerId().trim();
        } else {
            borrowerId = u.getId();
        }
        boolean ok = bookManager.returnBook(isbn.trim(), borrowerId);
        return ok ? ApiResponse.ok("归还成功", null) : ApiResponse.fail("归还失败（无匹配未归还记录）");
    }

    @GetMapping("/books/stats")
    public ApiResponse<BookStatsDto> stats(HttpSession session) {
        requireUser(session);
        List<Book> bs = bookManager.getBookList();
        BookStatsDto dto = new BookStatsDto();
        int titleCount = bs.size();
        int withAvail = 0;
        for (Book b : bs) {
            if (b.getAvailableCount() > 0) {
                withAvail++;
            }
        }
        dto.setTitleCount(titleCount);
        dto.setTitlesWithAvailableCopies(withAvail);
        dto.setTitlesFullyBorrowed(titleCount - withAvail);
        return ApiResponse.ok(dto);
    }

    @GetMapping("/borrow-records")
    public ApiResponse<List<BorrowRecord>> records(HttpSession session) {
        User u = requireUser(session);
        if (isAdmin(u)) {
            return ApiResponse.ok(bookManager.getRecordList_root());
        }
        return ApiResponse.ok(bookManager.getRecordList_user(u.getId()));
    }

    @GetMapping("/categories")
    public ApiResponse<List<Category>> listCategories(HttpSession session) {
        requireUser(session);
        return ApiResponse.ok(bookManager.listCategories());
    }

    @PostMapping("/categories")
    public ApiResponse<Void> addCategory(HttpSession session, @RequestBody Category category) {
        User u = requireUser(session);
        if (!isAdmin(u)) {
            return ApiResponse.fail("无权限");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            return ApiResponse.fail("分类名称不能为空");
        }
        boolean ok = bookManager.addCategory(category.getName().trim(), category.getDescription());
        return ok ? ApiResponse.ok("新增分类成功", null) : ApiResponse.fail("新增分类失败");
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<Void> updateCategory(
            HttpSession session,
            @PathVariable int id,
            @RequestBody Category category) {
        User u = requireUser(session);
        if (!isAdmin(u)) {
            return ApiResponse.fail("无权限");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            return ApiResponse.fail("分类名称不能为空");
        }
        boolean ok = bookManager.updateCategory(id, category.getName().trim(), category.getDescription());
        return ok ? ApiResponse.ok("修改分类成功", null) : ApiResponse.fail("修改分类失败");
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<Void> deleteCategory(HttpSession session, @PathVariable int id) {
        User u = requireUser(session);
        if (!isAdmin(u)) {
            return ApiResponse.fail("无权限");
        }
        List<Book> books = bookManager.searchCategory(id);
        if (!books.isEmpty()) {
            return ApiResponse.fail("该分类下仍有图书，删除失败");
        }
        boolean ok = bookManager.deleteCategory(id);
        return ok ? ApiResponse.ok("删除分类成功", null) : ApiResponse.fail("删除分类失败");
    }
}
