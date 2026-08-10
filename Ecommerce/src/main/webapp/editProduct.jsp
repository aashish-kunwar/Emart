<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>
<%@ page import="model.Product" %>

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


Product product =
        (Product) request.getAttribute("product");


if(product == null) {

    response.sendRedirect(
            request.getContextPath()
            + "/ProductServlet"
    );

    return;
}
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Edit Product - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=4000">

</head>

<body>

<jsp:include page="navbar.jsp" />


<div class="form-container">


    <h2>
        Edit Product
    </h2>


    <form action="<%=request.getContextPath()%>/ProductServlet"
          method="post">


        <input type="hidden"
               name="action"
               value="update">


        <input type="hidden"
               name="id"
               value="<%=product.getId()%>">


        <label>
            Product Name
        </label>

        <input type="text"
               name="name"
               value="<%=product.getName()%>"
               required>


        <label>
            Description
        </label>

        <textarea name="description"
                  rows="4"
                  required><%=product.getDescription()%></textarea>


        <label>
            Price
        </label>

        <input type="number"
               name="price"
               step="0.01"
               min="0"
               value="<%=product.getPrice()%>"
               required>


        <label>
            Image URL
        </label>

        <input type="text"
               name="image"
               value="<%=product.getImage()%>"
               required>


        <label>
            Category
        </label>

        <select name="category"
                required>


            <option value="Local Food & Grocery"
                <%= "Local Food & Grocery".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Local Food & Grocery

            </option>


            <option value="Tea & Coffee"
                <%= "Tea & Coffee".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Tea & Coffee

            </option>


            <option value="Clothing & Textiles"
                <%= "Clothing & Textiles".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Clothing & Textiles

            </option>


            <option value="Handicrafts"
                <%= "Handicrafts".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Handicrafts

            </option>


            <option value="Lokta & Stationery"
                <%= "Lokta & Stationery".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Lokta & Stationery

            </option>


            <option value="Jewelry & Accessories"
                <%= "Jewelry & Accessories".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Jewelry & Accessories

            </option>


            <option value="Home & Décor"
                <%= "Home & Décor".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Home & Décor

            </option>


            <option value="Gifts & Souvenirs"
                <%= "Gifts & Souvenirs".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Gifts & Souvenirs

            </option>


            <option value="Others"
                <%= "Others".equalsIgnoreCase(product.getCategory())
                    ? "selected" : "" %>>

                Others

            </option>


        </select>


        <button type="submit">

            Update Product

        </button>


    </form>


    <br>


    <a href="<%=request.getContextPath()%>/ProductServlet">

        ← Back to Products

    </a>


</div>


</body>

</html>