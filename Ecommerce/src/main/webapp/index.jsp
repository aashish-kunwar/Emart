<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>

<body>

<!-- Common Navbar -->
<jsp:include page="navbar.jsp" />

<!-- Hero Section -->

<div class="hero">

    <div class="hero-content">

        <span class="hero-badge">
            NEW ARRIVALS EVERY WEEK
        </span>

        <h1>
            Big Deals,
            <span>Every Day</span>
        </h1>

        <p>
            Explore authentic local products including fresh vegetables,
            homemade foods, traditional wear, handicrafts and daily essentials
            — all from trusted local sellers.
        </p>

        <a href="login.jsp">
            <button>
                Shop Now →
            </button>
        </a>

    </div>

</div>

<!-- Features -->

<div class="features">

    <div class="card">

        <div class="feature-icon">✓</div>

        <h3>Quality Products</h3>

        <p>
            Authentic and high-quality products from trusted sellers.
        </p>

    </div>

    <div class="card">

        <div class="feature-icon">🛒</div>

        <h3>Easy Shopping</h3>

        <p>
            A simple, convenient and secure shopping experience.
        </p>

    </div>

    <div class="card">

        <div class="feature-icon">🚚</div>

        <h3>Fast Delivery</h3>

        <p>
            Quick and reliable delivery directly to your location.
        </p>

    </div>

</div>

</body>

</html>