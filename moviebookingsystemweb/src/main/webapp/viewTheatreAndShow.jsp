<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.*" %>

<%
    // Get movieId from URL parameter
    String movieIdParam = request.getParameter("movieId");
    int movieId = 0;
    if (movieIdParam != null) {
        try {
            movieId = Integer.parseInt(movieIdParam);
        } catch (NumberFormatException e) {
            movieId = 0;
        }
    }
    if (movieId == 0) {
        response.sendRedirect("movies");
        return;
    }

    // Generate next 7 days for date selection
    List<Map<String, String>> dateList = new ArrayList<>();
    LocalDate today = LocalDate.now();
    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd");
    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    for (int i = 0; i < 7; i++) {
        LocalDate date = today.plusDays(i);
        Map<String, String> dateInfo = new HashMap<>();
        dateInfo.put("day", date.format(dayFormatter));
        dateInfo.put("date", date.format(dateFormatter));
        dateInfo.put("month", date.format(monthFormatter));
        dateInfo.put("fullDate", date.format(fullFormatter));
        dateInfo.put("isToday", i == 0 ? "true" : "false");
        dateList.add(dateInfo);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Select Theatre & Shows</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .date-list { display: flex; gap: 10px; margin-bottom: 20px; }
        .date-item { padding: 10px; border: 1px solid #ccc; cursor: pointer; }
        .date-item.selected { background: #ddd; }
        .shows-container { margin-top: 20px; }
        .theatre-card { border: 1px solid #ccc; padding: 10px; margin-bottom: 15px; }
        .show-time { display: inline-block; margin: 5px; padding: 5px 10px; border: 1px solid #aaa; cursor: pointer; }
        .show-time.selected { background: #28a745; color: #fff; }
        .loading { margin: 20px 0; }
        .no-shows { margin: 20px 0; color: #666; }
    </style>
</head>
<body>
    <h1>Select Theatre & Shows</h1>
    <p>Movie ID: <%= movieId %></p>

    <!-- Date Selection -->
    <div class="date-list" id="dateList">
        <% for (int i = 0; i < dateList.size(); i++) { 
            Map<String, String> dateInfo = dateList.get(i);
            String selectedClass = i == 0 ? "selected" : "";
        %>
            <div class="date-item <%= selectedClass %>" 
                 data-date="<%= dateInfo.get("fullDate") %>"
                 onclick="selectDate(this)">
                <div><%= dateInfo.get("day") %></div>
                <div><%= dateInfo.get("date") %> <%= dateInfo.get("month") %></div>
                <% if ("true".equals(dateInfo.get("isToday"))) { %>
                    <small>(Today)</small>
                <% } %>
            </div>
        <% } %>
    </div>

    <!-- Shows Section -->
    <div class="shows-container">
        <h3>Available Shows for <span id="selectedDateDisplay"><%= dateList.get(0).get("fullDate") %></span></h3>

        <div id="loadingContainer" class="loading">Loading shows...</div>
        <div id="theatreList" style="display:none;"></div>
        <div id="noShowsContainer" class="no-shows" style="display:none;">
            No shows available for this date.
        </div>
    </div>

    <script>
        let movieId = <%= movieId %>;
        let selectedDate = '<%= dateList.get(0).get("fullDate") %>';
        let selectedShowId = null;

        document.addEventListener('DOMContentLoaded', () => {
            loadTheatreShows(selectedDate);
        });

        function selectDate(dateElement) {
            document.querySelectorAll('.date-item').forEach(item => item.classList.remove('selected'));
            dateElement.classList.add('selected');
            selectedDate = dateElement.dataset.date;
            document.getElementById('selectedDateDisplay').textContent = selectedDate;
            loadTheatreShows(selectedDate);
        }

        async function loadTheatreShows(date) {
            try {
                showLoading();
                const response = await fetch('<%= request.getContextPath() %>/getTheatreAndShow', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: "movieId=" + movieId + "&date=" + encodeURIComponent(date)
                });
                if (!response.ok) throw new Error("Network error");
                const shows = await response.json();
                console.log(shows);
                displayTheatreShows(shows);
            } catch (error) {
                console.error(error);
                showError();
            }
        }

        function displayTheatreShows(shows) {          
            console.log('Shows array:', shows);
            console.log('First show object:', shows[0]);
            const loadingContainer = document.getElementById('loadingContainer');
            const theatreList = document.getElementById('theatreList');
            const noShows = document.getElementById('noShowsContainer');         
            loadingContainer.style.display = 'none';
            if (!Array.isArray(shows) || shows.length === 0) {
                theatreList.style.display = 'none';
                noShows.style.display = 'block';
                return;
            }         
            const grouped = {};
            shows.forEach(show => {
                const tName = show.theatreName || "Unknown Theatre";
                if (!grouped[tName]) grouped[tName] = [];
                grouped[tName].push(show);
            });

            
            let html = "";
            for (var tName in grouped) {
                if (grouped.hasOwnProperty(tName)) {
                    var tShows = grouped[tName];
                    var theatreLocation = tShows[0].theatreLocation || '';
                    html += '<div class="theatre-card">' +
                                '<h4>' + tName + '</h4>' +
                                '<p>' + theatreLocation + '</p>' +
                                '<div class="show-times">';

                    tShows.forEach(function(show) {
                        var showTime = show.showTime || '';
                        var showType = show.showType || '';
                        html += '<div class="show-time" onclick="selectShow(' + show.showId + ', ' + movieId + ', this)">' +
                        showTime + ' (' + showType + ')' +
                    '</div>';

                    });
                    html += '</div></div>';
                }
            }          
            theatreList.innerHTML = html;
            theatreList.style.display = 'block';
            noShows.style.display = 'none';
        }

      
        function selectShow(showId,movieId, el) {
           
            document.querySelectorAll('.show-time').forEach(s => s.classList.remove('selected'));
            el.classList.add('selected');

            
            const contextPath = '<%= request.getContextPath() %>';
            
            setTimeout(() => {
            	window.location.href = contextPath + '/seatSelection.jsp?showId=' + showId + '&movieId=' + movieId;

            }, 300);
        }


        function showLoading() {
            document.getElementById('loadingContainer').style.display = 'block';
            document.getElementById('theatreList').style.display = 'none';
            document.getElementById('noShowsContainer').style.display = 'none';
        }

        function showError() {
            document.getElementById('loadingContainer').textContent = "Error loading shows. Try again.";
        }
    </script>
</body>
</html>
