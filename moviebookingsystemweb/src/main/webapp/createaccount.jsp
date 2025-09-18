<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Account - Movie Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* full screen center */
            margin: 0;
        }
        .card {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            width: 380px;
        }
        h1 {
            margin-top: 0;
            text-align: center;
            color: #2c3e50;
        }
        p {
            text-align: center;
            color: #555;
        }
        form div {
            margin-bottom: 15px;
        }
        label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }
        input, select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
        button {
            width: 100%;
            padding: 10px;
            background: #3498db;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }
        button:disabled {
            background: #95a5a6;
        }
        #strengthFill {
            border-radius: 4px;
            margin-top: 5px;
        }
        ul {
            padding-left: 20px;
            font-size: 13px;
            margin: 8px 0;
        }
        a {
            display: block;
            text-align: center;
            margin-top: 10px;
            color: #3498db;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="card">
        <h1>Join Movie Booking</h1>
        <p>Create your account to start booking movies</p>

        <form id="registerForm" onsubmit="registerUser(event)">
            <!-- Username -->
            <div>
                <label for="username">Username</label>
                <input type="text" id="username" required minlength="3" autocomplete="username">
                <div id="usernameError" style="color:red;"></div>
            </div>

            <!-- Email -->
            <div>
                <label for="email">Email</label>
                <input type="email" id="email" required autocomplete="email">
                <div id="emailError" style="color:red;"></div>
            </div>

            <!-- Phone -->
            <div>
                <label for="phone">Phone</label>
                <input type="tel" id="phone" required pattern="[0-9]{10}" maxlength="10">
                <div id="phoneError" style="color:red;"></div>
            </div>

            <!-- Gender -->
            <div>
                <label for="gender">Gender</label>
                <select id="gender" required>
                    <option value="">Select</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                </select>
                <div id="genderError" style="color:red;"></div>
            </div>

            <!-- Role -->
            <div>
                <label for="role">Role</label>
                <select id="role" required>
                    <option value="">Select</option>
                    <option value="user">User</option>
                    <option value="theatreAdmin">Theatre Admin</option>
                </select>
                <div id="roleError" style="color:red;"></div>
            </div>

            <!-- Timezone -->
            <div>
                <label for="timezone">Timezone</label>
                <select id="timezone" required>
                    <option value="">Select</option>
                    <option value="Asia/Kolkata">India Standard Time</option>
                    <option value="America/New_York">Eastern Time</option>
                    <option value="Europe/London">GMT</option>
                </select>
                <div id="timezoneError" style="color:red;"></div>
            </div>

            <!-- Password -->
            <div>
                <label for="password">Password</label>
                <input type="password" id="password" required autocomplete="new-password">
                <div id="passwordError" style="color:red;"></div>

                <!-- Password Strength -->
                <div>
                    <div id="strengthFill" style="height:5px; width:0; background:red;"></div>
                    <span id="strengthText">Password strength</span>
                </div>
                <ul>
                    <li id="req-length">At least 6 characters</li>
                    <li id="req-upper">One uppercase letter</li>
                    <li id="req-lower">One lowercase letter</li>
                    <li id="req-number">One number</li>
                    <li id="req-special">One special character</li>
                </ul>
            </div>

            <button type="submit" id="registerBtn">Create Account</button>
            <div id="msg"></div>
        </form>

        <a href="login.jsp">Already have an account? Sign In</a>
    </div>


    <script>
        async function registerUser(event) {
            event.preventDefault();
            const formData = {
                username: document.getElementById("username").value.trim(),
                email: document.getElementById("email").value.trim(),
                phone: document.getElementById("phone").value.trim(),
                gender: document.getElementById("gender").value,
                role: document.getElementById("role").value,
                timezone: document.getElementById("timezone").value,
                password: document.getElementById("password").value
            };
            clearAllErrors();

            if (!validateForm(formData)) return;

            const btn = document.getElementById("registerBtn");
            btn.disabled = true;
            btn.textContent = "Processing...";

            try {
                const response = await fetch("register", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: new URLSearchParams(formData)
                });
                const result = await response.text();
                if (result.includes("Registered Successfully")) {
                    showMessage("Account created successfully! Redirecting...", "green");
                    setTimeout(() => { window.location.href = "login.jsp"; }, 2000);
                } else {
                    handleRegistrationErrors(result);
                }
            } catch {
                showMessage("Error connecting to server.", "red");
            } finally {
                btn.disabled = false;
                btn.textContent = "Create Account";
            }
        }

        function validateForm(data) {
            let ok = true;
            if (data.username.length < 3) { showFieldError("username", "Username too short"); ok = false; }
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) { showFieldError("email", "Invalid email"); ok = false; }
            if (!/^[0-9]{10}$/.test(data.phone)) { showFieldError("phone", "Phone must be 10 digits"); ok = false; }
            if (!isValidPassword(data.password)) { showFieldError("password", "Weak password"); ok = false; }
            return ok;
        }

        function handleRegistrationErrors(result) {
            if (result.includes("phone")) showFieldError("phone", "Phone already registered");
            if (result.includes("email")) showFieldError("email", "Email already registered");
            showMessage("Registration failed. Try again.", "red");
        }

        function showFieldError(id, msg) {
            document.getElementById(id + "Error").textContent = msg;
        }

        function clearAllErrors() {
            ["username","email","phone","gender","role","timezone","password"].forEach(id => {
                document.getElementById(id + "Error").textContent = "";
            });
        }

        function showMessage(text, color) {
            const msg = document.getElementById("msg");
            msg.textContent = text;
            msg.style.color = color;
        }

        function isValidPassword(pwd) {
            return pwd.length >= 6 &&
                /[A-Z]/.test(pwd) &&
                /[a-z]/.test(pwd) &&
                /[0-9]/.test(pwd) &&
                /[!@#$%^&*(),.?":{}|<>]/.test(pwd);
        }

        function updatePasswordStrength(password) {
            const reqs = {
                length: password.length >= 6,
                upper: /[A-Z]/.test(password),
                lower: /[a-z]/.test(password),
                number: /[0-9]/.test(password),
                special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
            };
            Object.keys(reqs).forEach(r => {
                document.getElementById("req-" + r).style.color = reqs[r] ? "green" : "red";
            });
            const count = Object.values(reqs).filter(Boolean).length;
            const fill = document.getElementById("strengthFill");
            const txt = document.getElementById("strengthText");
            if (count < 3) { fill.style.width = "33%"; fill.style.background = "red"; txt.textContent = "Weak"; }
            else if (count < 5) { fill.style.width = "66%"; fill.style.background = "orange"; txt.textContent = "Medium"; }
            else { fill.style.width = "100%"; fill.style.background = "green"; txt.textContent = "Strong"; }
        }

        document.addEventListener("DOMContentLoaded", () => {
            document.getElementById("password").addEventListener("input", e => updatePasswordStrength(e.target.value));
        });
    </script>
</body>
</html>
