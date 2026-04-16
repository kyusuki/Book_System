const state = {
  user: null,
  books: [],
  categories: [],
};
const API_BASE = `${window.location.origin}/api`;

const el = {
  authSection: document.getElementById("authSection"),
  appSection: document.getElementById("appSection"),
  msgBox: document.getElementById("msgBox"),
  currentUserText: document.getElementById("currentUserText"),
  captchaText: document.getElementById("captchaText"),
  bookTableBody: document.getElementById("bookTableBody"),
  categoryTableBody: document.getElementById("categoryTableBody"),
  recordTableBody: document.getElementById("recordTableBody"),
  statsBox: document.getElementById("statsBox"),
  bookCategorySelect: document.getElementById("bookCategorySelect"),
  bookAddPanel: document.getElementById("bookAddPanel"),
  bookTableWrap: document.getElementById("bookTableWrap"),
  bookCategoryPanels: document.getElementById("bookCategoryPanels"),
  categoryManageSection: document.getElementById("categoryManageSection"),
};

function showMsg(message, type = "success") {
  el.msgBox.textContent = message || "";
  el.msgBox.classList.remove("error", "success");
  if (message) {
    el.msgBox.classList.add(type);
  }
}

async function api(path, options = {}) {
  const url = path.startsWith("http") ? path : `${API_BASE}${path.startsWith("/api") ? path.slice(4) : path}`;
  const resp = await fetch(url, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    credentials: "include",
    ...options,
  });
  let json = null;
  try {
    json = await resp.json();
  } catch (_) {}
  if (!resp.ok) {
    const msg = (json && json.message) || `请求失败(${resp.status}) ${url}`;
    throw new Error(msg);
  }
  if (!json || !json.success) {
    throw new Error((json && json.message) || `操作失败: ${url}`);
  }
  return json.data;
}

function setAuthTab(tab) {
  const forms = {
    login: document.getElementById("loginForm"),
    register: document.getElementById("registerForm"),
    forgot: document.getElementById("forgotForm"),
  };
  Object.entries(forms).forEach(([k, form]) => {
    form.classList.toggle("hidden", k !== tab);
  });
  document.querySelectorAll("[data-auth-tab]").forEach((b) => {
    b.classList.toggle("active", b.dataset.authTab === tab);
  });
}

function setLoggedInView() {
  const isLogin = !!state.user;
  el.authSection.classList.toggle("hidden", isLogin);
  el.appSection.classList.toggle("hidden", !isLogin);
  if (!isLogin) return;
  const role = state.user.userType === 1 ? "管理员" : "普通用户";
  el.currentUserText.textContent = `当前用户：${state.user.username}（${role}）`;
  const isAdmin = state.user.userType === 1;
  document.querySelectorAll(".admin-only").forEach((n) => n.classList.toggle("hidden", !isAdmin));
  el.bookAddPanel.classList.toggle("hidden", !isAdmin);
  el.bookTableWrap.classList.toggle("hidden", !isAdmin);
  el.bookCategoryPanels.classList.toggle("hidden", isAdmin);
  el.categoryManageSection.classList.toggle("hidden", !isAdmin);
}

function renderCategories() {
  el.categoryTableBody.innerHTML = state.categories
    .map(
      (c) => `<tr>
        <td>${c.id}</td>
        <td>${c.name || ""}</td>
        <td>${c.description || ""}</td>
        <td>
          <button class="op-btn" data-edit-category="${c.id}">编辑</button>
          <button class="op-btn danger" data-del-category="${c.id}">删除</button>
        </td>
      </tr>`
    )
    .join("");

  el.bookCategorySelect.innerHTML = state.categories
    .map((c) => `<option value="${c.id}">${c.id} - ${c.name}</option>`)
    .join("");
}

function renderBooks() {
  const isAdmin = state.user && state.user.userType === 1;
  if (isAdmin) {
    el.bookTableBody.innerHTML = state.books
    .map(
      (b) => `<tr>
        <td>${b.isbn || ""}</td>
        <td>${b.title || ""}</td>
        <td>${b.author || ""}</td>
        <td>${b.category_name || "未分类"}</td>
        <td>${b.totalCount ?? 0}</td>
        <td>${b.availableCount ?? 0}</td>
        <td>
          <button class="op-btn" data-edit-book="${b.isbn}">填入编辑表单</button>
          <button class="op-btn danger" data-del-book="${b.isbn}">删除</button>
        </td>
      </tr>`
    )
    .join("");
    return;
  }

  const grouped = new Map();
  state.books.forEach((b) => {
    const key = b.category_name || "未分类";
    if (!grouped.has(key)) grouped.set(key, []);
    grouped.get(key).push(b);
  });
  el.bookCategoryPanels.innerHTML = [...grouped.entries()]
    .map(([category, books]) => `
      <details class="category-panel">
        <summary>${category}（${books.length}本）</summary>
        <div class="category-panel-body">
          ${books.map((b) => `
            <div class="book-item">
              <div><strong>${b.title || ""}</strong></div>
              <div>ISBN：${b.isbn || ""}</div>
              <div>作者：${b.author || ""}</div>
              <div>出版社：${b.publisher || ""}</div>
              <div>库存：${b.availableCount ?? 0}/${b.totalCount ?? 0}</div>
            </div>
          `).join("")}
        </div>
      </details>
    `)
    .join("");
}

function renderRecords(list) {
  el.recordTableBody.innerHTML = list
    .map((r) => `<tr>
      <td>${r.borrowerName || ""}</td>
      <td>${r.borrowerId || ""}</td>
      <td>${r.bookTitle || ""}</td>
      <td>${r.borrowDate || ""}</td>
      <td>${r.returnDate || "未归还"}</td>
      <td>${Number(r.status) === 1 ? "已归还" : "借出中"}</td>
    </tr>`)
    .join("");
}

function renderStats(s) {
  el.statsBox.innerHTML = `
    <div>图书种数：${s.titleCount ?? 0}</div>
    <div>有库存图书种数：${s.titlesWithAvailableCopies ?? 0}</div>
    <div>已借空图书种数：${s.titlesFullyBorrowed ?? 0}</div>
  `;
}

async function refreshCaptcha() {
  try {
    const data = await api("/auth/captcha");
    el.captchaText.textContent = data.captcha || "----";
  } catch (e) {
    el.captchaText.textContent = "获取失败";
    showMsg(e.message, "error");
  }
}

async function loadAll() {
  const [books, categories, stats, records] = await Promise.all([
    api("/books"),
    api("/categories"),
    api("/books/stats"),
    api("/borrow-records"),
  ]);
  state.books = books || [];
  state.categories = categories || [];
  renderBooks();
  renderCategories();
  renderStats(stats || {});
  renderRecords(records || []);
}

function fillBookForm(book) {
  const f = document.getElementById("bookAddForm");
  f.isbn.value = book.isbn || "";
  f.title.value = book.title || "";
  f.author.value = book.author || "";
  f.publisher.value = book.publisher || "";
  f.publishDate.value = book.publishDate || "";
  f.totalCount.value = book.totalCount ?? 0;
  f.availableCount.value = book.availableCount ?? 0;
  f.categoryId.value = book.category_id ?? "";
}

function fillCategoryForm(category) {
  const f = document.getElementById("categoryForm");
  f.id.value = category.id;
  f.name.value = category.name || "";
  f.description.value = category.description || "";
}

function bindEvents() {
  document.querySelectorAll("[data-auth-tab]").forEach((b) => {
    b.addEventListener("click", () => setAuthTab(b.dataset.authTab));
  });
  document.getElementById("refreshCaptchaBtn").addEventListener("click", refreshCaptcha);
  document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target;
    try {
      state.user = await api("/auth/login", {
        method: "POST",
        body: JSON.stringify({
          username: f.username.value.trim(),
          password: f.password.value,
          captcha: f.captchaInput.value.trim(),
        }),
      });
      setLoggedInView();
      await loadAll();
      showMsg("登录成功");
    } catch (err) {
      showMsg(err.message, "error");
      await refreshCaptcha();
    }
  });

  document.getElementById("registerForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target;
    try {
      await api("/auth/register", {
        method: "POST",
        body: JSON.stringify({
          username: f.username.value.trim(),
          password: f.password.value,
          passwordConfirm: f.password.value,
          idCard: f.id.value.trim(),
          phone: f.phone.value.trim(),
        }),
      });
      showMsg("注册成功，请登录");
      setAuthTab("login");
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  document.getElementById("forgotForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target;
    try {
      await api("/auth/forgot-password", {
        method: "POST",
        body: JSON.stringify({
          username: f.username.value.trim(),
          idCard: f.id.value.trim(),
          phone: f.phone.value.trim(),
          newPassword: f.newPassword.value,
        }),
      });
      showMsg("密码重置成功，请登录");
      setAuthTab("login");
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  document.getElementById("logoutBtn").addEventListener("click", async () => {
    try {
      await api("/auth/logout", { method: "POST" });
      state.user = null;
      setLoggedInView();
      await refreshCaptcha();
      showMsg("已退出登录");
    } catch (e) {
      showMsg(e.message, "error");
    }
  });

  document.getElementById("bookSearchForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target;
    const keyword = f.keyword.value.trim();
    if (!keyword) {
      showMsg("请输入关键词", "error");
      return;
    }
    try {
      state.books = await api(
        `/books/search?mode=${encodeURIComponent(f.mode.value)}&keyword=${encodeURIComponent(keyword)}`
      );
      renderBooks();
      showMsg("查询完成");
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  document.getElementById("bookSearchResetBtn").addEventListener("click", async () => {
    try {
      state.books = await api("/books");
      renderBooks();
      showMsg("已重置为全部图书");
    } catch (e) {
      showMsg(e.message, "error");
    }
  });

  document.getElementById("bookAddForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!state.user || state.user.userType !== 1) {
      showMsg("普通用户无权限操作", "error");
      return;
    }
    const f = e.target;
    const payload = {
      isbn: f.isbn.value.trim(),
      title: f.title.value.trim(),
      author: f.author.value.trim(),
      publisher: f.publisher.value.trim(),
      publishDate: f.publishDate.value,
      totalCount: Number(f.totalCount.value),
      availableCount: Number(f.availableCount.value),
      category_id: Number(f.categoryId.value),
    };
    const isUpdate = state.books.some((b) => b.isbn === payload.isbn);
    try {
      if (isUpdate) {
        await api(`/books/${encodeURIComponent(payload.isbn)}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
        showMsg("图书更新成功");
      } else {
        await api("/books", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        showMsg("图书新增成功");
      }
      await loadAll();
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  el.bookTableBody.addEventListener("click", async (e) => {
    if (!state.user || state.user.userType !== 1) {
      return;
    }
    const editIsbn = e.target.getAttribute("data-edit-book");
    const delIsbn = e.target.getAttribute("data-del-book");
    if (editIsbn) {
      const book = state.books.find((b) => b.isbn === editIsbn);
      if (book) fillBookForm(book);
      return;
    }
    if (delIsbn) {
      if (!confirm(`确认删除图书 ${delIsbn} 吗？`)) return;
      try {
        await api(`/books/${encodeURIComponent(delIsbn)}`, { method: "DELETE" });
        await loadAll();
        showMsg("删除成功");
      } catch (err) {
        showMsg(err.message, "error");
      }
    }
  });

  document.getElementById("borrowForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target;
    try {
      await api("/books/borrow", {
        method: "POST",
        body: JSON.stringify({
          isbn: f.isbn.value.trim(),
          borrowerName: f.borrowerName.value.trim(),
          borrowerId: f.borrowerId.value.trim(),
        }),
      });
      await loadAll();
      showMsg("借阅成功");
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  document.getElementById("returnForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target;
    try {
      await api("/books/return", {
        method: "POST",
        body: JSON.stringify({
          isbn: f.isbn.value.trim(),
          borrowerId: f.borrowerId.value.trim(),
        }),
      });
      await loadAll();
      showMsg("归还成功");
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  document.getElementById("categoryForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!state.user || state.user.userType !== 1) {
      showMsg("普通用户无权限操作", "error");
      return;
    }
    const f = e.target;
    const id = f.id.value.trim();
    const payload = {
      name: f.name.value.trim(),
      description: f.description.value.trim(),
    };
    try {
      if (id) {
        await api(`/categories/${id}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
        showMsg("分类修改成功");
      } else {
        await api("/categories", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        showMsg("分类新增成功");
      }
      f.reset();
      await loadAll();
    } catch (err) {
      showMsg(err.message, "error");
    }
  });

  document.getElementById("categoryFormResetBtn").addEventListener("click", () => {
    document.getElementById("categoryForm").reset();
  });

  el.categoryTableBody.addEventListener("click", async (e) => {
    if (!state.user || state.user.userType !== 1) {
      return;
    }
    const editId = e.target.getAttribute("data-edit-category");
    const delId = e.target.getAttribute("data-del-category");
    if (editId) {
      const c = state.categories.find((x) => String(x.id) === String(editId));
      if (c) fillCategoryForm(c);
      return;
    }
    if (delId) {
      if (!confirm(`确认删除分类 ${delId} 吗？`)) return;
      try {
        await api(`/categories/${delId}`, { method: "DELETE" });
        await loadAll();
        showMsg("分类删除成功");
      } catch (err) {
        showMsg(err.message, "error");
      }
    }
  });
}

async function bootstrap() {
  bindEvents();
  setAuthTab("login");
  await refreshCaptcha();
  try {
    state.user = await api("/auth/me");
    setLoggedInView();
    await loadAll();
  } catch (_) {
    state.user = null;
    setLoggedInView();
  }
}

bootstrap();
