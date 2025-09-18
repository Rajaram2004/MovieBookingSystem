package dao;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.moviebookingsystem.SHOWS;
import com.adventnet.moviebookingsystem.THEATRE;
import com.adventnet.moviebookingsystem.USERS;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;

import model.Theatre;

public class TheatreDAO {
	public static Theatre getTheatreById(long theatreId) {
		try {
			SelectQuery query = new SelectQueryImpl(Table.getTable(THEATRE.TABLE));
			query.addSelectColumn(new Column(THEATRE.TABLE, "*"));
			Criteria c = new Criteria(new Column(THEATRE.TABLE, THEATRE.THEATREID), theatreId, QueryConstants.EQUAL);

			query.setCriteria(c);

			DataObject dobj = DataAccess.get(query);
			Row r = dobj.getFirstRow(THEATRE.TABLE);

			Theatre theatre = new Theatre(r.getLong("THEATREID"), r.getString("THEATRENAME"),
					r.getString("THEATRELOCATION"), r.getString("STATUS"));

			return theatre;
		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
