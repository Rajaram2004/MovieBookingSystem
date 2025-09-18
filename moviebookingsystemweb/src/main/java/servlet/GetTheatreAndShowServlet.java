package servlet;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MovieDAO;
import model.Show;

public class GetTheatreAndShowServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String movieId = request.getParameter("movieId");
		String date = request.getParameter("date");

		long range[] = getDayEpochRange(date);
		System.out.println("Starting time : "+ range[0]);
		System.out.println("Ending time : "+ range[1]);
		List<Show> shows = MovieDAO.getTheatreAndShow(movieId, range[0], range[1]);

		if (!shows.isEmpty()) {

			StringBuilder jsonBuilder = new StringBuilder();
			jsonBuilder.append("[");
			String d = "2D";
			for (int i = 0; i < shows.size(); i++) {
				Show show = shows.get(i);
				long epochMillis = show.getShowDateTime(); 
				Instant instant = Instant.ofEpochMilli(epochMillis);
				LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				String formattedTime = dateTime.format(formatter);

				jsonBuilder.append("{").append("\"showId\":").append(show.getShowId()).append(",")
						.append("\"showTime\":\"").append(formattedTime).append("\",").append("\"showType\":\"2D\",")
						.append("\"theatreName\":\"").append(show.getTheatre().getTheatreName()).append("\",")
						.append("\"theatreLocation\":\"").append(show.getTheatre().getTheatreLocation()).append("\"")
						.append("}");
				if (i < shows.size() - 1) {
					jsonBuilder.append(",");
				}
			}

			jsonBuilder.append("]");
			System.out.println(jsonBuilder);
			String json = jsonBuilder.toString();
			System.out.println("json sent to website ------------------------");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		} else {

			System.out.println("json no shows for the time ------------------------");
			response.getWriter().write("[]");
		}
	}

	public static long[] getDayEpochRange(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.parse(dateStr, formatter);
		long startMillis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

		long endMillis = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;

		return new long[] { startMillis, endMillis };
	}
}
