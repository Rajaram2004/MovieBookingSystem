package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.adventnet.moviebookingsystem.SHOWS;
import com.adventnet.moviebookingsystem.MOVIES;
import model.Movies;
import model.Show;

public class MovieDAO {
	public static List<Movies> getActiveMovies() {
		List<Movies> movieList = new ArrayList<>();
		try {
			SelectQuery query = new SelectQueryImpl(Table.getTable(MOVIES.TABLE));
			query.addSelectColumn(new Column(SHOWS.TABLE, "*"));
			query.addSelectColumn(Column.getColumn(MOVIES.TABLE, "*"));
			Criteria c = new Criteria(Column.getColumn(SHOWS.TABLE, SHOWS.MOVIEID),
					Column.getColumn(MOVIES.TABLE, MOVIES.MOVIEID), QueryConstants.EQUAL);
			Join J = new Join(Table.getTable(MOVIES.TABLE), Table.getTable(SHOWS.TABLE), c, Join.INNER_JOIN);
			query.addJoin(J);
			DataObject dobj = DataAccess.get(query);
			Iterator<Row> rows = dobj.getRows(MOVIES.TABLE);

			while (rows.hasNext()) {
				Row r = rows.next();
				Movies movie = new Movies(r.getLong("MOVIEID"), r.getString("MOVIENAME"), r.getInt("DURATION"),
						r.getString("GENRE"), r.getString("LANGUAGE"), r.getLong("RELEASEDATE"));
				movieList.add(movie);
			}

		} catch (

		DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (movieList.isEmpty()) {
			return null;
		}
		return movieList;
	}

	public static List<Show> getTheatreAndShow(String movieId, long start, long end) {
		List<Show> showList = new ArrayList<>();
		try {
			SelectQuery query = new SelectQueryImpl(Table.getTable(SHOWS.TABLE));
			query.addSelectColumn(new Column(SHOWS.TABLE, "*"));
			Criteria c = new Criteria(new Column(SHOWS.TABLE, SHOWS.MOVIEID), movieId, QueryConstants.EQUAL);
			c.and( new Criteria(new Column(SHOWS.TABLE, SHOWS.SHOWDATETIME), start, QueryConstants.GREATER_EQUAL));
			c.and(new Criteria(new Column(SHOWS.TABLE, SHOWS.SHOWDATETIME), end, QueryConstants.LESS_EQUAL));
			c.and( new Criteria(new Column(SHOWS.TABLE, SHOWS.STATUS), "Active", QueryConstants.EQUAL));
			query.setCriteria(c);
			

			DataObject dobj = DataAccess.get(query);
			Iterator<Row> rows = dobj.getRows(SHOWS.TABLE);

			while (rows.hasNext()) {
				Row r = rows.next();
				Show show = new Show();
				show.setShowId(r.getLong(SHOWS.SHOWID));
				show.setShowDateTime(r.getLong(SHOWS.SHOWDATETIME));
				show.setTheatre(TheatreDAO.getTheatreById(r.getLong(SHOWS.THEATREID)));
				showList.add(show);
			}
		} catch (
		DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return showList;
	}
	
	
	
	
	
	
	
	
	
	

}
