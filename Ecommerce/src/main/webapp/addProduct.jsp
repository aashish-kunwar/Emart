<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>

<%
User user = (User) session.getAttribute("user");

if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Add Product - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=8">

</head>

<body>

<jsp:include page="navbar.jsp" />

<div class="form-container">

    <h2>Add New Product</h2>

    <form action="ProductServlet" method="post">

        <input type="hidden"
               name="action"
               value="add">

        <label>Product Name</label>

        <input type="text"
               name="name"
               placeholder="Enter product name"
               required>

        <label>Description</label>

        <textarea name="description"
                  rows="4"
                  placeholder="Enter product description"
                  required></textarea>

        <label>Price</label>

        <input type="number"
               name="price"
               step="0.01"
               min="0"
               placeholder="Enter price"
               required>

        <label>Image URL</label>

        <input type="text"
               name="image"
               placeholder="Paste product image URL"
               required>

        <label>Category</label>

        <select name="category" required>

            <option value="">
                Select Category
            </option>

            <option value="Electronics">
                Electronics
            </option>

            <option value="Fashion">
                Fashion
            </option>

            <option value="Groceries">
                Groceries
            </option>

            <option value="Handicrafts">
                Handicrafts
            </option>

            <option value="Books">
                Books
            </option>

            <option value="Others">
                Others
            </option>

        </select>

        <input type="submit"
               value="Add Product">

    </form>

</div>

</body>

</html>
