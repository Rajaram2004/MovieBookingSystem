<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Movie Booking</title>
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
        }

        .nav-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .nav-left {
            display: flex;
            align-items: center;
            gap: 2rem;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: bold;
            color: #fff;
            text-decoration: none;
        }

        .nav-links {
            list-style: none;
            display: flex;
            gap: 1.5rem;
            margin: 0;
            padding: 0;
        }

        .nav-links a {
            color: #fff;
            text-decoration: none;
        }

        .nav-links a.active {
            text-decoration: underline;
        }

        .nav-right {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .profile-button {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            background: #e50914;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 0.5rem 1rem;
            cursor: pointer;
        }

        .profile-avatar {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            background: #f5c518;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            color: #333;
            font-size: 0.8rem;
        }

        .logout-btn {
            background: #555;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 0.5rem 1rem;
            cursor: pointer;
        }

        /* Main */
        .main-container {
            padding: 2rem;
        }

        .hero-section {
            text-align: center;
            margin-bottom: 2rem;
        }

        .hero-title {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .hero-subtitle {
            font-size: 1rem;
            color: #666;
        }

        /* Actions */
        .actions-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 1.5rem;
        }

        .action-card {
            border: 1px solid #ccc;
            border-radius: 6px;
            padding: 1.5rem;
            background: #fff;
            text-align: center;
            cursor: pointer;
        }

        .action-icon {
            font-size: 2rem;
            margin-bottom: 1rem;
        }

        .action-title {
            font-size: 1.2rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
        }

        .action-description {
            font-size: 0.9rem;
            color: #555;
            margin-bottom: 1rem;
        }

        .action-button {
            background: #e50914;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 0.6rem 1.2rem;
            cursor: pointer;
        }

        .action-button:hover {
            background: #b20710;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <div class="nav-container">
            <div class="nav-left">
                <a href="home.jsp" class="logo">Movie Booking</a>
                <ul class="nav-links">
                    <li><a href="home.jsp" class="active">Home</a></li>
                    <li><a href="<%= request.getContextPath() %>/movies">Movies</a></li>
                    <li><a href="#">Theaters</a></li>
                    <li><a href="#">Offers</a></li>
                </ul>
            </div>
            <div class="nav-right">
                <button class="profile-button" onclick="redirectToProfile()">
                    <div class="profile-avatar">U</div>
                    <span>Profile</span>
                </button>
                <button class="logout-btn" onclick="logout()">Logout</button>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="main-container">
        <section class="hero-section">
            <h1 class="hero-title">Welcome to Movie Booking</h1>
            <p class="hero-subtitle">Book tickets, manage your bookings, and discover new films.</p>
        </section>

        <!-- Action Cards -->
        <section class="actions-container">
            <div class="action-card" onclick="bookTicket()">
                <div class="action-icon">🍿</div>
                <h2 class="action-title">Book Tickets</h2>
                <p class="action-description">Discover the latest movies and book your tickets instantly.</p>
                <button class="action-button">Book Now</button>
            </div>

            <div class="action-card" onclick="viewBookings()">
                <div class="action-icon">🎫</div>
                <h2 class="action-title">My Bookings</h2>
                <p class="action-description">View and manage all your booked tickets.</p>
                <button class="action-button">View Bookings</button>
            </div>
        </section>
    </div>

    <script>
        function redirectToProfile() {
            window.location.href = 'profile.jsp';
        }

        function bookTicket() {
        	window.location.href = '<%= request.getContextPath() %>/movies';
        }

        function viewBookings() {
            window.location.href = 'viewbookings.jsp';
        }

        function logout() {
            if (confirm('Are you sure you want to logout?')) {
                window.location.href = '<%= request.getContextPath() %>/logoutServlet';
            }
        }
    </script>
</body>
</html>
