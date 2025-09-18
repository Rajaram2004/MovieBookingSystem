<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String sessionPhone = (String) session.getAttribute("phoneNumber");
    if (sessionPhone == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=" + request.getRequestURI());
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile - CinemaFlix</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .navbar {
            background: #333;
            color: #fff;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
        }

        .navbar a {
            color: #fff;
            text-decoration: none;
        }

        .main-container {
            max-width: 600px;
            margin: 2rem auto;
            padding: 1rem;
        }

        .edit-profile-card {
            background: #fff;
            border: 1px solid #ccc;
            border-radius: 6px;
            padding: 2rem;
        }

        .card-header {
            text-align: center;
            margin-bottom: 1.5rem;
        }

        .card-title {
            font-size: 1.5rem;
            margin-bottom: 0.5rem;
        }

        .card-subtitle {
            color: #666;
            font-size: 0.9rem;
        }

        .edit-form {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-label {
            font-weight: bold;
            margin-bottom: 0.3rem;
        }

        .form-input, .form-select {
            padding: 0.7rem;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-input.error, .form-select.error {
            border-color: red;
        }

        .btn-container {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
        }

        .btn {
            flex: 1;
            padding: 0.8rem;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
        }

        .btn-primary {
            background: #e50914;
            color: #fff;
        }

        .btn-secondary {
            background: #ccc;
        }

        .error-message {
            margin-top: 1rem;
            padding: 0.8rem;
            border-radius: 4px;
            background: #f8d7da;
            color: #721c24;
            display: none;
        }

        .error-message.show {
            display: block;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <a href="profile.jsp">Movie Booking</a>
        <a href="profile.jsp">← Back to Profile</a>
    </nav>

    <!-- Main -->
    <div class="main-container">
        <div class="edit-profile-card">
            <div class="card-header">
                <h1 class="card-title">Edit Profile</h1>
                <p class="card-subtitle">Update your personal information</p>
            </div>

            <form id="editProfileForm" class="edit-form" onsubmit="submitForm(event)">
                <div class="form-group">
                    <label class="form-label" for="username">Username</label>
                    <input type="text" id="username" name="username" class="form-input" required minlength="3">
                </div>

                <div class="form-group">
                    <label class="form-label" for="phoneNumber">Phone Number</label>
                    <input type="tel" id="phoneNumber" name="phoneNumber" class="form-input" 
                           required pattern="[0-9]{10}" maxlength="10">
                </div>

                <div class="form-group">
                    <label class="form-label" for="timezone">Timezone</label>
                    <select id="timezone" name="timezone" class="form-select" required>
                        <option value="">Select Timezone</option>
                        <option value="America/New_York">Eastern Time (EST/EDT)</option>
                        <option value="America/Chicago">Central Time (CST/CDT)</option>
                        <option value="America/Denver">Mountain Time (MST/MDT)</option>
                        <option value="America/Los_Angeles">Pacific Time (PST/PDT)</option>
                        <option value="Europe/London">GMT/BST</option>
                        <option value="Europe/Berlin">Central European Time (CET/CEST)</option>
                        <option value="Europe/Moscow">Moscow Standard Time (MSK)</option>
                        <option value="Asia/Kolkata">India Standard Time (IST)</option>
                        <option value="Asia/Tokyo">Japan Standard Time (JST)</option>
                        <option value="Australia/Sydney">Australian Eastern Time (AEST/AEDT)</option>
                    </select>
                </div>

                <div class="btn-container">
                    <button type="button" class="btn btn-secondary" onclick="window.location.href='profile.jsp'">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="submitBtn">Update Profile</button>
                </div>

                <div id="errorMessage" class="error-message"></div>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', async function() {
            try {
                const phoneNumber = "<%= session.getAttribute("phoneNumber") %>";
                if (!phoneNumber || phoneNumber === "null") {
                    window.location.href = "login.jsp";
                    return;
                }

                const response = await fetch("<%=request.getContextPath()%>/getUserData", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: "phoneNumber=" + encodeURIComponent(phoneNumber)
                });

                if (response.ok) {
                    const userData = await response.json();
                    document.getElementById('username').value = userData.username || '';
                    document.getElementById('phoneNumber').value = userData.phoneNumber || '';
                    document.getElementById('timezone').value = userData.timezone || '';
                }
            } catch (error) {
                console.error("Error loading user data:", error);
            }
        });

        async function submitForm(event) {
            event.preventDefault();

            const submitBtn = document.getElementById('submitBtn');
            const errorMessage = document.getElementById('errorMessage');
            const phoneNumberInput = document.getElementById('phoneNumber');

            errorMessage.classList.remove('show');
            phoneNumberInput.classList.remove('error');
            submitBtn.disabled = true;

            try {
                const formData = new FormData(event.target);
                const newPhoneNumber = formData.get('phoneNumber');
                const oldPhoneNumber = "<%= (session.getAttribute("phoneNumber") != null 
                                           ? session.getAttribute("phoneNumber").toString() 
                                           : "") %>";

                const params = new URLSearchParams();
                params.append("oldPhoneNumber", oldPhoneNumber);
                params.append("newPhoneNumber", newPhoneNumber);
                params.append("name", formData.get('username'));
                params.append("timezone", formData.get('timezone'));

                if (newPhoneNumber !== oldPhoneNumber) {
                    const checkResponse = await fetch("<%=request.getContextPath()%>/checkPhoneNumber", {
                        method: "POST",
                        headers: { "Content-Type": "application/x-www-form-urlencoded" },
                        body: "phoneNumber=" + encodeURIComponent(newPhoneNumber)
                    });

                    const checkResult = await checkResponse.text();

                    if (checkResult !== "Valid Number") {
                        phoneNumberInput.classList.add('error');
                        showError("Phone number is already registered by another user");
                        return;
                    }
                }

                const updateResponse = await fetch("<%=request.getContextPath()%>/updateUserProfile", {
                    method: "POST",
                    body: params
                });

                const updateResult = await updateResponse.text();

                if (updateResult.includes("updated") || updateResult.includes("success")) {
                    window.location.href = "profile.jsp";
                } else {
                    showError("Failed to update profile. Please try again.");
                }

            } catch (error) {
                console.error("Error:", error);
                showError("Connection error. Please try again.");
            } finally {
                submitBtn.disabled = false;
            }
        }

        function showError(message) {
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.textContent = message;
            errorMessage.classList.add('show');
        }

        document.getElementById('phoneNumber').addEventListener('input', function() {
            this.value = this.value.replace(/\D/g, '');
        });
    </script>
</body>
</html>
