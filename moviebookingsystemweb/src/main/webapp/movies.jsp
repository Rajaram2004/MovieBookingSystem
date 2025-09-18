<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Movies" %>
<%
    List<Movies> movieList = (List<Movies>) request.getAttribute("movies");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movies - Movie Booking</title>
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

        .navbar a {
            color: #fff;
            text-decoration: none;
            margin-right: 1rem;
        }

        .navbar a.active {
            font-weight: bold;
            text-decoration: underline;
        }

        /* Main Content */
        .main-container {
            padding: 2rem;
        }

        .page-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .page-title {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .movie-count {
            color: #555;
        }

        /* Movies Grid */
        .movies-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 1rem;
        }

        /* Movie Card */
        .movie-card {
            border: 1px solid #ccc;
            border-radius: 6px;
            background: #fff;
            padding: 1rem;
        }

        .movie-title {
            font-size: 1.2rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
        }

        .movie-details {
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }

        .movie-genres {
            margin-bottom: 1rem;
        }

        .genre-tag {
            display: inline-block;
            background: #eee;
            color: #333;
            padding: 0.2rem 0.6rem;
            border-radius: 4px;
            font-size: 0.8rem;
            margin-right: 0.3rem;
        }

        .book-button {
            display: inline-block;
            width: 100%;
            padding: 0.6rem;
            background: #e50914;
            color: #fff;
            border: none;
            border-radius: 4px;
            text-align: center;
            cursor: pointer;
        }

        .book-button:hover {
            background: #b20710;
        }

        /* No Movies */
        .no-movies {
            text-align: center;
            margin-top: 2rem;
            color: #777;
        }
    </style>
</head>
<body>
    <!-- Navigation Header -->
    <nav class="navbar">
        <a href="user/home.jsp">Home</a>
        <a href="movies" class="active">Movies</a>
        <a href="#">Theaters</a>
        <a href="#">Events</a>
    </nav>

    <!-- Main Content -->
    <div class="main-container">
        <div class="page-header">
            <h1 class="page-title">Now Showing</h1>
            <p>Discover the latest movies and book your tickets instantly</p>
            <% if (movieList != null) { %>
                <p class="movie-count"><%= movieList.size() %> Movie<%= movieList.size() != 1 ? "s" : "" %> Available</p>
            <% } %>
        </div>

        <% if (movieList != null && !movieList.isEmpty()) { %>
            <div class="movies-grid">
                <% for (Movies movie : movieList) { %>
                    <div class="movie-card" onclick="viewMovie(<%= movie.getMovieId() %>)">
                        <h3 class="movie-title"><%= movie.getMovieTitle() %></h3>
                        <div class="movie-details">
                            <p>⏱ 
                            <%
                                int totalMinutes = movie.getDuration();
                                int hours = totalMinutes / 60;
                                int minutes = totalMinutes % 60;
                            %>
                            <%= hours %> hrs <%= minutes %> mins</p>

                            <p>📅 <%= movie.getReleaseYear() > 0 ? String.valueOf(movie.getReleaseYear()) : "Coming Soon" %></p>
                            <p>🌐 <%= movie.getLanguage() %></p>
                        </div>

                        <div class="movie-genres">
                            <% 
                                String genres = movie.getGenre();
                                if (genres != null && !genres.trim().isEmpty()) {
                                    String[] genreArray = genres.split(",");
                                    for (String genre : genreArray) { 
                            %>
                                <span class="genre-tag"><%= genre.trim() %></span>
                            <% 
                                    } 
                                }
                            %>
                        </div>

                        <button class="book-button" onclick="event.stopPropagation(); viewMovie(<%= movie.getMovieId() %>)">
                            Book Tickets
                        </button>
                    </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="no-movies">
                <h2>No Movies Available</h2>
                <p>We're currently updating our movie listings. Please check back soon!</p>
            </div>
        <% } %>
    </div>

    <script>
        function viewMovie(movieId) {
            if (movieId) {
                window.location.href = 'viewTheatreAndShow.jsp?movieId=' + movieId;
            }
        }
    </script>
</body>
</html>
