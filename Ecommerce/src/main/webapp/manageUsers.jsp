<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="model.User" %>

<%
User currentUser =
        (User) session.getAttribute("user");

if(currentUser == null ||
   !"admin".equalsIgnoreCase(currentUser.getRole())) {

    response.sendRedirect(
            request.getContextPath()
            + "/login.jsp"
    );

    return;
}

UserDAO userDAO =
        new UserDAO();

ArrayList<User> users =
        userDAO.getAllCustomers();

String userMessage =
        (String) session.getAttribute(
                "userMessage"
        );

if(userMessage != null) {

    session.removeAttribute(
            "userMessage"
    );
}
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Manage Users - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=1200">

<style>

/* =========================
   MANAGE USERS
========================= */

.manage-users-container{
    width:90%;
    max-width:1100px;
    margin:40px auto;
}

.manage-users-container h1{
    text-align:center;
    color:#172554;
    margin-bottom:10px;
}

.manage-users-container > p{
    text-align:center;
    color:#64748b;
    margin-bottom:30px;
}

.user-card{
    background:white;
    padding:20px;
    margin-bottom:18px;
    border-radius:12px;
    box-shadow:0 6px 18px rgba(15,23,42,0.12);

    display:flex;
    justify-content:space-between;
    align-items:center;
    gap:20px;
}

.user-info h3{
    color:#172554;
    margin-bottom:8px;
}

.user-info p{
    color:#64748b;
    margin:5px 0;
}

.delete-user-btn{
    background:#dc2626 !important;
    color:white !important;
    min-width:120px;
}

.delete-user-btn:hover{
    background:#b91c1c !important;
}

.user-message{
    background:#dcfce7;
    color:#166534;
    border:1px solid #bbf7d0;
    padding:12px;
    border-radius:8px;
    margin-bottom:20px;
    text-align:center;
    font-weight:bold;
}

.no-users{
    text-align:center;
    color:#64748b;
    padding:40px;
}


/* DELETE USER POPUP */

.user-delete-modal{
    display:none;
    position:fixed;
    inset:0;

    background:rgba(15,23,42,0.65);

    justify-content:center;
    align-items:center;

    z-index:9999;
}

.user-delete-box{
    width:380px;
    max-width:90%;

    background:white;

    padding:30px;

    border-radius:14px;

    text-align:center;

    box-shadow:0 12px 35px rgba(0,0,0,0.25);
}

.user-delete-box h2{
    color:#991b1b;
    margin-bottom:12px;
}

.user-delete-box p{
    color:#64748b;
    margin-bottom:25px;
}

.user-delete-actions{
    display:flex;
    justify-content:center;
    gap:15px;
}

.cancel-user-delete{
    background:#e2e8f0 !important;
    color:#172033 !important;
}

.confirm-user-delete{
    background:#dc2626 !important;
    color:white !important;
}

.confirm-user-delete:hover{
    background:#b91c1c !important;
}


@media(max-width:700px){

    .user-card{
        flex-direction:column;
        align-items:flex-start;
    }

    .delete-user-btn{
        width:100%;
    }
}

</style>

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="manage-users-container">


    <h1>
        Manage Customers
    </h1>


    <p>
        View registered customers and remove customer accounts.
    </p>


    <%
    if(userMessage != null){
    %>

        <div class="user-message">

            <%=userMessage%>

        </div>

    <%
    }
    %>


    <%
    if(users != null &&
       !users.isEmpty()){

        for(User u : users){
    %>


        <div class="user-card">


            <div class="user-info">


                <h3>
                    <%=u.getName()%>
                </h3>


                <p>
                    <strong>Email:</strong>
                    <%=u.getEmail()%>
                </p>


                <p>
                    <strong>User ID:</strong>
                    <%=u.getId()%>
                </p>


            </div>


            <button type="button"
                    class="delete-user-btn"
                    onclick="openUserDeletePopup('<%=u.getEmail()%>')">

                Delete User

            </button>


        </div>


    <%
        }
    }
    else{
    %>


        <div class="no-users">

            No customers available.

        </div>


    <%
    }
    %>


</div>



<!-- =========================
     DELETE USER POPUP
========================= -->

<div id="userDeleteModal"
     class="user-delete-modal">


    <div class="user-delete-box">


        <h2>
            Delete Customer?
        </h2>


        <p>
            This will remove the customer account and cart.
            Their previous order history will remain available to admin.
        </p>


        <div class="user-delete-actions">


            <button type="button"
                    class="cancel-user-delete"
                    onclick="closeUserDeletePopup()">

                Cancel

            </button>


            <form action="<%=request.getContextPath()%>/DeleteUserServlet"
                  method="post">


                <input type="hidden"
                       name="email"
                       id="deleteUserEmail">


                <button type="submit"
                        class="confirm-user-delete">

                    Delete

                </button>


            </form>


        </div>


    </div>


</div>



<script>

function openUserDeletePopup(email){

    document.getElementById(
        "deleteUserEmail"
    ).value = email;


    document.getElementById(
        "userDeleteModal"
    ).style.display = "flex";
}


function closeUserDeletePopup(){

    document.getElementById(
        "userDeleteModal"
    ).style.display = "none";
}


window.addEventListener(
    "click",
    function(event){

        var modal =
            document.getElementById(
                "userDeleteModal"
            );


        if(event.target === modal){

            closeUserDeletePopup();
        }
    }
);

</script>


</body>

</html>