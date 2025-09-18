<%@ page contentType="text/html;charset=UTF-8" %>
<%
String sessionPhone = (String) session.getAttribute("phoneNumber");
if (sessionPhone == null) {
    String currentUri = request.getRequestURI().substring(request.getContextPath().length());
    response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=" + currentUri);
    return;
}
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
response.setHeader("Pragma", "no-cache"); 
response.setDateHeader("Expires", 0); 
%>
<script>
    window.addEventListener("pageshow", function (event) {
        if (event.persisted || (performance.getEntriesByType("navigation")[0]?.type === "back_forward")) {
            window.location.reload();
        }
    });
</script>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile - Movie Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        nav {
            background: #333;
            color: #fff;
            padding: 1rem;
            text-align: center;
        }
        nav a {
            color: #fff;
            text-decoration: none;
            margin: 0 1rem;
        }
        nav a.active {
            text-decoration: underline;
        }

        .container {
            max-width: 700px;
            margin: 2rem auto;
            background: #fff;
            padding: 2rem;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        h1, h3 {
            text-align: center;
            margin-bottom: 1rem;
        }

        .profile-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        #profileAvatar {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            background: #f5c518;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            font-weight: bold;
            font-size: 1.5rem;
            color: #333;
        }

        .info-block {
            margin: 1rem 0;
            line-height: 1.6;
        }

        .actions {
            text-align: center;
            margin-top: 2rem;
        }
        .actions button {
            margin: 0.5rem;
            padding: 0.6rem 1.2rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            background: #e50914;
            color: #fff;
        }
        .actions button:hover {
            background: #b20710;
        }

        /* Modal */
        #deleteModal {
            display: none;
            position: fixed;
            top:0; left:0; right:0; bottom:0;
            background: rgba(0,0,0,0.6);
        }
        #deleteModal div {
            background:#fff;
            margin:10% auto;
            padding:20px;
            max-width:400px;
            text-align: center;
            border-radius: 6px;
        }
        #deleteModal button {
            margin: 0.5rem;
            padding: 0.6rem 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        #deleteModal button:first-child {
            background: #e50914;
            color: #fff;
        }
        #deleteModal button:last-child {
            background: #ccc;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav>
        <a href="home.jsp">Movie Booking</a>
        <a href="home.jsp">Home</a>
        <a href="<%= request.getContextPath() %>/movies">Movies</a>
        <a href="<%= request.getContextPath() %>/movies">Theater</a>
        <a href="#" class="active">Profile</a>
    </nav>

    <div class="container">
        <h1>Profile</h1>
        <div class="profile-header">
            <div id="profileAvatar">U</div>
            <h2 id="profileName">John Doe</h2>
            <p id="profileRole">User</p>
        </div>

        <!-- Personal Info -->
        <h3>Personal Information</h3>
        <div class="info-block">
            <p><b>Username:</b> <span id="username">john_doe</span></p>
            <p><b>Email:</b> <span id="emailId">john.doe@example.com</span></p>
            <p><b>Phone:</b> <span id="phoneNumber">+1 234 567 8900</span></p>
            <p><b>Gender:</b> <span id="gender">Male</span></p>
        </div>

        <!-- Account Info -->
        <h3>Account Details</h3>
        <div class="info-block">
            <p><b>Role:</b> <span id="role">User</span></p>
            <p><b>Timezone:</b> <span id="timezone">America/New_York (EST/EDT)</span></p>
            <p><b>Balance:</b> <span id="balance">$125.50</span></p>
            <p><b>Member Since:</b> <span id="memberSince">January 2024</span></p>
        </div>

        <!-- Actions -->
        <div class="actions">
            <button onclick="editProfile()">Edit Profile</button>
            <button onclick="changePassword()">Change Password</button>
            <button onclick="addBalance()">Add Balance</button>
            <button onclick="showDeleteModal()">Delete Account</button>
            <button onclick="logoutUser()">Logout</button>
        </div>
    </div>

    <!-- Delete Modal -->
    <div id="deleteModal">
        <div>
            <h2>Delete Account</h2>
            <p><b>Warning:</b> This action cannot be undone!</p>
            <p>Are you sure you want to delete your account?</p>
            <button onclick="confirmDelete()">Yes, Delete</button>
            <button onclick="closeDeleteModal()">Cancel</button>
        </div>
    </div>

<script>
function logoutUser() {
    if (confirm("Are you sure you want to logout?")) {
        window.location.href = '<%= request.getContextPath() %>/logoutServlet';
    }
}

document.addEventListener('DOMContentLoaded', async function() {
    try {
        const phoneNumber = "<%= (session.getAttribute("phoneNumber") != null ? session.getAttribute("phoneNumber").toString() : "") %>";
        if (!phoneNumber || phoneNumber === "null") {
            window.location.href = "login.jsp";
            return;
        }
        const response = await fetch("<%= request.getContextPath() %>/getUserData", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "phoneNumber=" + encodeURIComponent(phoneNumber)
        });
        if (!response.ok) throw new Error("Server error: " + response.status);
        const userData = await response.json();
        loadUserData(userData);
    } catch (error) {
        console.error("Error fetching user data:", error);
    }
});

function loadUserData(userData) {
    document.getElementById('profileName').textContent = userData.profileName;
    document.getElementById('profileRole').textContent = userData.role;
    document.getElementById('profileAvatar').textContent = userData.profileName.charAt(0).toUpperCase();
    document.getElementById('username').textContent = userData.username;
    document.getElementById('emailId').textContent = userData.emailId;
    document.getElementById('phoneNumber').textContent = userData.phoneNumber;
    document.getElementById('gender').textContent = userData.gender;
    document.getElementById('role').textContent = userData.role;
    document.getElementById('timezone').textContent = userData.timezone;
    document.getElementById('balance').textContent = userData.balance;
    document.getElementById('memberSince').textContent = userData.memberSince;
}

function editProfile() {
    window.location.href = '<%= request.getContextPath() %>/user/editprofile.jsp';
}
function changePassword() {
    window.location.href = '<%= request.getContextPath() %>/user/changepassword.jsp';
}
function addBalance() {
    window.location.href = '<%= request.getContextPath() %>/user/addbalance.jsp';
}

function showDeleteModal() {
    document.getElementById('deleteModal').style.display = 'block';
}
function closeDeleteModal() {
    document.getElementById('deleteModal').style.display = 'none';
}
function confirmDelete() {
    if (confirm('This action cannot be undone. Continue?')) {
        deleteAccount();
    }
}

async function deleteAccount() {
    try {
        const response = await fetch('<%= request.getContextPath() %>/deleteAccount', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                action: 'deleteAccount',
                phoneNumber: '<%= session.getAttribute("phoneNumber") %>'
            })
        });
        const result = await response.text();
        if (result.includes('deleted')) {
            alert('Your account has been deleted.');
            window.location.href = '<%= request.getContextPath() %>/logoutServlet';
        } else {
            alert('Failed to delete account.');
            closeDeleteModal();
        }
    } catch (error) {
        alert('Connection error. Please try again.');
        closeDeleteModal();
    }
}
</script>
</body>
</html>
