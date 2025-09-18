<%@ page contentType="text/html;charset=UTF-8" %>
<%
String sessionPhone = (String) session.getAttribute("phoneNumber");
if (sessionPhone == null) {
    String currentUri = request.getRequestURI().substring(request.getContextPath().length()); 
    response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=" + currentUri);
    return;
}
%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Balance - Movie Booking</title>
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
            margin-right: 1rem;
        }

        .navbar .logo {
            font-weight: bold;
        }

        .main-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 1rem;
        }

        .page-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .page-title {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .page-subtitle {
            color: #555;
        }

        .balance-display {
            border: 1px solid #ccc;
            border-radius: 6px;
            padding: 1.5rem;
            text-align: center;
            margin-bottom: 2rem;
            background: #fff;
        }

        .balance-label {
            color: #777;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }

        .current-balance {
            font-size: 2rem;
            font-weight: bold;
            color: green;
        }

        .add-balance-section {
            border: 1px solid #ccc;
            border-radius: 6px;
            padding: 1.5rem;
            background: #fff;
        }

        .section-header {
            text-align: center;
            margin-bottom: 1.5rem;
        }

        .section-title {
            font-size: 1.3rem;
            margin-bottom: 0.3rem;
        }

        .section-subtitle {
            color: #666;
            font-size: 0.9rem;
        }

        .message {
            padding: 0.8rem;
            margin-bottom: 1rem;
            border-radius: 4px;
            text-align: center;
            display: none;
        }

        .success-message {
            background: #d4edda;
            color: #155724;
        }

        .error-message {
            background: #f8d7da;
            color: #721c24;
        }

        .amount-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
            gap: 0.8rem;
            margin-bottom: 1rem;
        }

        .amount-button {
            padding: 0.8rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            background: #eee;
            cursor: pointer;
        }

        .amount-button.selected {
            background: #007bff;
            color: #fff;
            border-color: #007bff;
        }

        .custom-amount {
            margin-top: 1rem;
            text-align: center;
        }

        .custom-input {
            padding: 0.6rem;
            width: 100%;
            border: 1px solid #ccc;
            border-radius: 4px;
            text-align: center;
        }

        .action-section {
            margin-top: 1.5rem;
            display: flex;
            justify-content: center;
            gap: 1rem;
        }

        .action-btn {
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-add {
            background: #e50914;
            color: #fff;
        }

        .btn-cancel {
            background: #ccc;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <div class="logo">Movie Booking</div>
        <div>
            <a href="home.jsp">Home</a>
            <a href="profile.jsp">Profile</a>
        </div>
    </nav>

    <!-- Main -->
    <div class="main-container">
        <div class="page-header">
            <h1 class="page-title">Add Balance</h1>
            <p class="page-subtitle">Top up your wallet for quick bookings</p>
        </div>

        <div class="balance-display">
            <div class="balance-label">Current Balance</div>
            <div class="current-balance" id="currentBalance">₹0.00</div>
        </div>

        <div class="add-balance-section">
            <div class="section-header">
                <h2 class="section-title">Add Money</h2>
                <p class="section-subtitle">Choose or enter an amount</p>
            </div>

            <div id="successMessage" class="message success-message"></div>
            <div id="errorMessage" class="message error-message"></div>

            <div class="amount-grid">
                <button class="amount-button" data-amount="100">₹100</button>
                <button class="amount-button" data-amount="200">₹200</button>
                <button class="amount-button" data-amount="500">₹500</button>
                <button class="amount-button" data-amount="1000">₹1000</button>
            </div>

            <div class="custom-amount">
                <input type="number" id="customAmountInput" class="custom-input" placeholder="Enter custom amount" min="1" max="100000">
            </div>

            <div class="action-section">
                <button id="addBalanceButton" class="action-btn btn-add" onclick="addBalance()">Add to Wallet</button>
                <button class="action-btn btn-cancel" onclick="cancelAndGoBack()">Cancel</button>
            </div>
        </div>
    </div>

    <script>
        let selectedAmount = 0;
        let currentBalance = 0;

        document.addEventListener('DOMContentLoaded', function() {
            loadUserBalance();
            setupEventListeners();
        });

        async function loadUserBalance() {
            try {
                const phoneNumber = "<%= (session.getAttribute("phoneNumber") != null 
                    ? session.getAttribute("phoneNumber").toString() 
                    : "") %>";

                if (!phoneNumber || phoneNumber === "null") {
                    window.location.href = "login.jsp";
                    return;
                }

                const response = await fetch("<%= request.getContextPath() %>/getUserData", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: "phoneNumber=" + encodeURIComponent(phoneNumber)
                });

                if (!response.ok) throw new Error("Failed to fetch balance");

                const userData = await response.json();
                currentBalance = parseFloat(userData.balance.replace('₹', '')) || 0;
                document.getElementById('currentBalance').textContent = userData.balance;

            } catch (error) {
                console.error("Error:", error);
            }
        }

        function setupEventListeners() {
            document.querySelectorAll('.amount-button').forEach(btn => {
                btn.addEventListener('click', function() {
                    clearAmountSelection();
                    this.classList.add('selected');
                    selectedAmount = parseInt(this.dataset.amount);
                    document.getElementById('customAmountInput').value = '';
                });
            });

            document.getElementById('customAmountInput').addEventListener('input', function() {
                clearAmountSelection();
                selectedAmount = parseInt(this.value) || 0;
            });
        }

        function clearAmountSelection() {
            document.querySelectorAll('.amount-button').forEach(btn => btn.classList.remove('selected'));
        }

        async function addBalance() {
            if (!selectedAmount || selectedAmount <= 0) {
                showError("Please select or enter a valid amount.");
                return;
            }
            if ( selectedAmount > 100000000) {
                showError("Amount should be less than 100000000 ");
                return;
            }

            const phoneNumber = "<%= session.getAttribute("phoneNumber") %>";
            const newBalance = currentBalance + selectedAmount;

            try {
                const response = await fetch("<%= request.getContextPath() %>/updateUserBalance", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: new URLSearchParams({
                        phoneNumber: phoneNumber,
                        Balance: newBalance
                    })
                });

                const result = await response.text();
                100000000
                if (result.includes('updated')) {
                    currentBalance = newBalance;
                    document.getElementById('currentBalance').textContent = `₹${newBalance}`;
                    showSuccess(`₹${selectedAmount} added successfully!`);
                    setTimeout(() => window.location.href = 'profile.jsp', 2000);
                } else {
                    showError("Failed to add balance.");
                }
            } catch (error) {
                showError("Error occurred. Try again.");
            }
        }

        function showSuccess(msg) {
            document.getElementById('successMessage').textContent = msg;
            document.getElementById('successMessage').style.display = 'block';
            document.getElementById('errorMessage').style.display = 'none';
        }

        function showError(msg) {
            document.getElementById('errorMessage').textContent = msg;
            document.getElementById('errorMessage').style.display = 'block';
            document.getElementById('successMessage').style.display = 'none';
        }

        function cancelAndGoBack() {
            window.location.href = 'profile.jsp';
        }
    </script>
</body>
</html>
