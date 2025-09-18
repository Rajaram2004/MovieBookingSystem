<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - MovieBooking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-card {
            background: #fff;
            padding: 2rem;
            border: 1px solid #ddd;
            border-radius: 6px;
            width: 100%;
            max-width: 400px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }

        .card-header {
            text-align: center;
            margin-bottom: 1.5rem;
        }

        .card-title {
            font-size: 1.8rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
            color: #333;
        }

        .card-subtitle {
            font-size: 0.9rem;
            color: #666;
        }

        .login-form {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        .form-input {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1rem;
        }

        .form-input.error {
            border-color: #dc3545;
            background: #ffecec;
        }

        .btn-primary {
            background: #e50914;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 0.75rem;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
        }

        .btn-primary:hover {
            background: #b20710;
        }

        .message {
            margin-top: 1rem;
            padding: 0.75rem;
            border-radius: 4px;
            font-size: 0.9rem;
            text-align: center;
            display: none;
        }

        .message.success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            display: block;
        }

        .message.error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            display: block;
        }

        .footer-links {
            text-align: center;
            margin-top: 1.5rem;
        }

        .footer-links a {
            color: #e50914;
            text-decoration: none;
            margin: 0 0.5rem;
        }

        .footer-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="card-header">
            <h1 class="card-title">Welcome Back</h1>
            <p class="card-subtitle">Sign in to book your favorite movies</p>
        </div>

        <form id="loginForm" class="login-form" onsubmit="loginUser(event)">
            <input 
                type="text" 
                id="username" 
                class="form-input" 
                placeholder="Enter your Phone Number / Email Id" 
                required
                autocomplete="username"
            >

            <input 
                type="password" 
                id="password" 
                class="form-input" 
                placeholder="Enter your password" 
                required
                autocomplete="current-password"
            >

            <button type="submit" class="btn-primary" id="loginBtn">Sign In</button>
            <div id="msg" class="message"></div>
        </form>

        <div class="footer-links">
            <a href="#">Forgot Password?</a>
            <a href="createaccount.jsp">Create Account</a>
        </div>
    </div>

    <script>
        async function loginUser(event) {
            event.preventDefault();
            
            const usernameInput = document.getElementById("username");
            const passwordInput = document.getElementById("password");
            const msg = document.getElementById("msg");
            const loginBtn = document.getElementById("loginBtn");

            const username = usernameInput.value.trim();
            const password = passwordInput.value.trim();

            usernameInput.classList.remove("error");
            passwordInput.classList.remove("error");
            msg.className = "message";
            loginBtn.disabled = true;
            loginBtn.textContent = "Signing In...";

            if (!username || !password) {
                showMessage("All fields are required!", "error");
                if (!username) usernameInput.classList.add("error");
                if (!password) passwordInput.classList.add("error");
                resetButton();
                return;
            }

            try {
                const response = await fetch("login", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: "username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password)
                });

                const result = await response.text();

                if (result.includes("Login Successful")) {
                    showMessage("Login Successful! Redirecting...", "success");
                    setTimeout(() => {
                        window.location.href = "user/home.jsp";
                    }, 1500);
                } else {
                    showMessage("Invalid username or password. Please try again.", "error");
                    usernameInput.classList.add("error");
                    passwordInput.classList.add("error");
                    setTimeout(() => {
                        usernameInput.value = "";
                        passwordInput.value = "";
                        usernameInput.focus();
                        resetButton();
                    }, 1000);
                }
            } catch (error) {
                console.error("Login error:", error);
                showMessage("Connection error. Please try again.", "error");
                resetButton();
            }
        }

        function showMessage(text, type) {
            const msg = document.getElementById("msg");
            msg.textContent = text;
            msg.className = `message ${type}`;
        }

        function resetButton() {
            const loginBtn = document.getElementById("loginBtn");
            loginBtn.disabled = false;
            loginBtn.textContent = "Sign In";
        }
    </script>
</body>
</html>
