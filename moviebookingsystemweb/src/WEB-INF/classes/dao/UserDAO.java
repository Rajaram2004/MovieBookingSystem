package dao;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.moviebookingsystem.USERS;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;

import model.Users;

public class UserDAO {

	public static Users getUser(String user, String password) {
	Long id = Long.parseLong(user);
		try {
			System.out.println("Entered into try");
			SelectQuery query = new SelectQueryImpl(Table.getTable(USERS.TABLE));
			query.addSelectColumn(new  Column(USERS.TABLE, "*"));
			Criteria c =new Criteria (new Column(USERS.TABLE, USERS.PHONENUMBER), id, QueryConstants.EQUAL);	
			query.setCriteria(c);
//			String queryString1 = RelationalAPI.getInstance().getSelectSQL(query);
//			System.out.println("------------------------------------------------------------------- "+queryString1);
			DataObject dobj = DataAccess.get(query);
			
			Row row = dobj.getFirstRow(USERS.TABLE);
			Users emp = null;
			if (row != null) {
				emp = new Users();
				emp.setUserId(row.getLong(USERS.PHONENUMBER));
				emp.setPassword(row.getString(USERS.PASSWORD));
			} 
			return emp;
		} catch (DataAccessException f) {
			System.out.println(
					"////////////////////////////////////////////////////////////////////////////////////////////");
			f.printStackTrace();
		} catch (Exception e) {
			System.out.println(
					"===========================================================================================");
			e.printStackTrace();
		}
		return null;
	}
}
