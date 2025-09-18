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
    <title>Change Password - Movie Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f8f9fa;
            margin: 0;
            padding: 0;
            color: #333;
        }

        /* Navbar */
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
            max-width: 500px;
            margin: 3rem auto;
            padding: 1rem;
        }

        .page-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .page-title {
            font-size: 1.8rem;
            margin-bottom: 0.5rem;
        }

        .page-subtitle {
            color: #555;
        }

        /* Card */
        .password-card {
            background: #fff;
            border: 1px solid #ccc;
            border-radius: 6px;
            padding: 2rem;
        }

        .password-form {
            display: flex;
            flex-direction: column;
            gap: 1.2rem;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-label {
            font-weight: bold;
            margin-bottom: 0.3rem;
        }

        .form-input {
            padding: 0.8rem;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-input.error {
            border-color: #dc3545;
        }

        .form-input.valid {
            border-color: #28a745;
        }

        /* Requirements */
        .password-requirements {
            margin-top: 0.5rem;
            padding: 0.8rem;
            background: #f1f1f1;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .requirements-title {
            font-weight: bold;
            font-size: 0.9rem;
            margin-bottom: 0.3rem;
        }

        .requirements-list {
            list-style: none;
            padding: 0;
            margin: 0;
            font-size: 0.85rem;
        }

        .requirements-list li {
            margin: 0.2rem 0;
            color: #dc3545;
        }

        .requirements-list li.valid {
            color: #28a745;
        }

        /* Buttons */
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

        .btn:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }

        /* Messages */
        .message {
            margin-top: 1rem;
            padding: 0.8rem;
            border-radius: 4px;
            text-align: center;
            display: none;
        }

        .message.error {
            background: #f8d7da;
            color: #721c24;
        }

        .message.success {
            background: #d4edda;
            color: #155724;
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
        <div class="page-header">
            <h1 class="page-title">Change Password</h1>
            <p class="page-subtitle">Update your account security</p>
        </div>

        <div class="password-card">
            <form id="passwordForm" class="password-form" onsubmit="changePassword(event)">
                <div class="form-group">
                    <label class="form-label" for="oldPassword">Current Password</label>
                    <input type="password" id="oldPassword" name="oldPassword" class="form-input" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="newPassword">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" class="form-input" required oninput="validatePassword()">

                    <div class="password-requirements">
                        <div class="requirements-title">Password must include:</div>
                        <ul class="requirements-list">
                            <li id="req-length">At least 6 characters</li>
                            <li id="req-upper">One uppercase letter</li>
                            <li id="req-lower">One lowercase letter</li>
                            <li id="req-number">One number</li>
                            <li id="req-special">One special character</li>
                        </ul>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="confirmPassword">Confirm New Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-input" required oninput="validatePasswordMatch()">
                </div>

                <div class="btn-container">
                    <button type="button" class="btn btn-secondary" onclick="window.location.href='profile.jsp'">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="submitBtn">Change Password</button>
                </div>

                <div id="message" class="message"></div>
            </form>
        </div>
    </div>

    <script>
        function validatePassword() {
            const password = document.getElementById('newPassword').value;
            const requirements = {
                length: password.length >= 6,
                upper: /[A-Z]/.test(password),
                lower: /[a-z]/.test(password),
                number: /[0-9]/.test(password),
                special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
            };

            Object.keys(requirements).forEach(req => {
                const element = document.getElementById(`req-${req}`);
                if (element) { 
                    if (requirements[req]) {
                        element.classList.add('valid');
                    } else {
                        element.classList.remove('valid');
                    }
                }
            });


            const input = document.getElementById('newPassword');
            const allValid = Object.values(requirements).every(Boolean);

            if (password.length > 0) {
                input.classList.toggle('valid', allValid);
                input.classList.toggle('error', !allValid);
            } else {
                input.classList.remove('valid', 'error');
            }

            validatePasswordMatch();
            return allValid;
        }

        function validatePasswordMatch() {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
           
            const input = document.getElementById('confirmPassword');
            if (input) {
                if (confirmPassword.length > 0) {
                    if (newPassword === confirmPassword) {
                        input.classList.add('valid');
                        input.classList.remove('error');
                    } else {
                        input.classList.add('error');
                        input.classList.remove('valid');
                    }
                } else {
                    input.classList.remove('valid', 'error');
                }
            }

        }

        async function changePassword(event) {
            event.preventDefault();
            const oldPasswordInput = document.getElementById('oldPassword');
            const newPasswordInput = document.getElementById('newPassword');
            const confirmPasswordInput = document.getElementById('confirmPassword');
            const message = document.getElementById('message');
            const submitBtn = document.getElementById('submitBtn');

            message.style.display = "none";

            if (!validatePassword()) {
                showMessage("New password does not meet requirements", "error");
                newPasswordInput.classList.add("error");
                return;
            }

            if (newPasswordInput.value !== confirmPasswordInput.value) {
                showMessage("Password confirmation does not match", "error");
                confirmPasswordInput.classList.add("error");
                return;
            }

            submitBtn.disabled = true;

            try {
                const sessionPhone = "<%= session.getAttribute("phoneNumber") %>";
                const formData = new FormData(event.target);

                const checkResponse = await fetch("<%=request.getContextPath()%>/checkOldPasswordValid", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: "oldPassword=" + encodeURIComponent(formData.get("oldPassword")) +
                          "&phoneNumber=" + encodeURIComponent(sessionPhone)
                });

                const checkResult = await checkResponse.text();
                if (checkResult !== "Valid Password") {
                    showMessage("Current password is incorrect", "error");
                    oldPasswordInput.classList.add("error");
                    return;
                }

                const updateResponse = await fetch("<%=request.getContextPath()%>/updatePassword", {
                    method: "POST",
                    body: new URLSearchParams({
                        phoneNumber: sessionPhone,
                        newPassword: formData.get("newPassword"),
                        confirmPassword: formData.get("confirmPassword")
                    })
                });

                const updateResult = await updateResponse.text();

                if (updateResult.includes("updated") || updateResult.includes("success")) {
                    showMessage("Password changed successfully! Redirecting...", "success");
                    setTimeout(() => window.location.href = "profile.jsp", 2000);
                } else {
                    showMessage("Failed to update password. Please try again.", "error");
                }
            } catch (err) {
                console.error("Error:", err);
                showMessage("Connection error. Please try again.", "error");
            } finally {
                submitBtn.disabled = false;
            }
        }

        function showMessage(text, type) {
            const message = document.getElementById("message");
            message.textContent = text;
            message.className = "message " + type;
            message.style.display = "block";
        }
    </script>
</body>
</html>
