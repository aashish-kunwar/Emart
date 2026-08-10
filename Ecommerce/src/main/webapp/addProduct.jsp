<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>

<%
User user =
        (User) session.getAttribute("user");

if(user == null ||
   !"admin".equalsIgnoreCase(user.getRole())) {

    response.sendRedirect(
            request.getContextPath()
            + "/login.jsp"
    );

    return;
}
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Add Product - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=4000">

</head>

<body>

<jsp:include page="navbar.jsp" />


<div class="form-container">

    <h2>
        Add New Product
    </h2>


    <form action="<%=request.getContextPath()%>/ProductServlet"
          method="post">


        <input type="hidden"
               name="action"
               value="add">


        <label>
            Product Name
        </label>

        <input type="text"
               name="name"
               placeholder="Enter product name"
               required>


        <label>
            Description
        </label>

        <textarea name="description"
                  rows="4"
                  placeholder="Enter product description"
                  required></textarea>


        <label>
            Price
        </label>

        <input type="number"
               name="price"
               step="0.01"
               min="0"
               placeholder="Enter price"
               required>


        <label>
            Image URL
        </label>

        <input type="text"
               name="image"
               placeholder="Paste product image URL"
               required>


        <label>
            Category
        </label>

        <select name="category"
                required>


            <option value="">
                Select Category
            </option>


            <option value="Local Food & Grocery">
                Local Food & Grocery
            </option>


            <option value="Tea & Coffee">
                Tea & Coffee
            </option>


            <option value="Clothing & Textiles">
                Clothing & Textiles
            </option>


            <option value="Handicrafts">
                Handicrafts
            </option>


            <option value="Lokta & Stationery">
                Lokta & Stationery
            </option>


            <option value="Jewelry & Accessories">
                Jewelry & Accessories
            </option>


            <option value="Home & Décor">
                Home & Décor
            </option>


            <option value="Gifts & Souvenirs">
                Gifts & Souvenirs
            </option>


            <option value="Others">
                Others
            </option>


        </select>


        <button type="submit">
            Add Product
        </button>


    </form>


</div>


</body>

</html>