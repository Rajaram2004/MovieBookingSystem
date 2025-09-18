package dao;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.InsertQueryImpl;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.ds.query.UpdateQuery;
import com.adventnet.ds.query.UpdateQueryImpl;
import com.adventnet.mfw.bean.BeanUtil;
//import com.adventnet.beans;
import com.adventnet.moviebookingsystem.USERS;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;

import model.User;

public class UserDAO {

	public static User getUser(String phoneNumber, boolean isPhoneNumber) {
		try {
			SelectQuery query = new SelectQueryImpl(Table.getTable(USERS.TABLE));
			query.addSelectColumn(new Column(USERS.TABLE, "*"));
			Criteria active = new Criteria(new Column(USERS.TABLE, USERS.ACTIVE), 1, QueryConstants.EQUAL);
			if (isPhoneNumber) {
				Long phone = Long.parseLong(phoneNumber);
				Criteria c = new Criteria(new Column(USERS.TABLE, USERS.PHONENUMBER), phone, QueryConstants.EQUAL);
				c = c.and(active);
				query.setCriteria(c);
			} else {
				Criteria e = new Criteria(new Column(USERS.TABLE, USERS.EMAILID), phoneNumber, QueryConstants.EQUAL);
				e = e.and(active);
				query.setCriteria(e);
			}

			DataObject dobj = DataAccess.get(query);
			Row row = dobj.getFirstRow(USERS.TABLE);
			User emp = null;
			if (row != null) {
				emp = new User();
				emp.setId(row.getLong(USERS.ID));
				emp.setPassword(row.getString(USERS.PASSWORD));
				emp.setEmailId(row.getString(USERS.EMAILID));
				emp.setBalance(row.getDouble(USERS.BALANCE));
				emp.setGender("Male");
				emp.setPhoneNumber(row.getLong(USERS.PHONENUMBER));
				emp.setTimeZone(row.getString(USERS.TIMEZONE));
				emp.setUserType(row.getString(USERS.ROLE));
				emp.setActive(row.getBoolean(USERS.ACTIVE));
				emp.setName(row.getString(USERS.NAME));
			}
			return emp;
		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean updateUserData(String phoneNumber1, String newPhoneNumber1, String name, String timeZone) {
		try {

			Long phoneNumber = Long.parseLong(phoneNumber1);
			Long newPhoneNumber = Long.parseLong(newPhoneNumber1);
			Persistence per = (Persistence) BeanUtil.lookup("Persistence");

			UpdateQuery s = new UpdateQueryImpl(USERS.TABLE);
			Criteria c = new Criteria(Column.getColumn(USERS.TABLE, USERS.PHONENUMBER), phoneNumber,
					QueryConstants.EQUAL);
			s.setCriteria(c);
			s.setUpdateColumn(USERS.PHONENUMBER, newPhoneNumber);
			s.setUpdateColumn(USERS.NAME, name);
			s.setUpdateColumn(USERS.TIMEZONE, timeZone);
			int row = per.update(s);
			if (row > 0) {
				return true;
			}

		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean checkPhoneNumberPresent(String phoneNumber) {

		try {
			SelectQuery query = new SelectQueryImpl(Table.getTable(USERS.TABLE));
			query.addSelectColumn(new Column(USERS.TABLE, "*"));
			Long phone = Long.parseLong(phoneNumber);
			Criteria c = new Criteria(new Column(USERS.TABLE, USERS.PHONENUMBER), phone, QueryConstants.EQUAL);
			query.setCriteria(c);
			DataObject dobj = DataAccess.get(query);
			Row row = dobj.getFirstRow(USERS.TABLE);
			if (row != null) {
				return true;
			}
		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean checkEmailIdPresent(String emailId) {
		try {
			SelectQuery query = new SelectQueryImpl(Table.getTable(USERS.TABLE));
			query.addSelectColumn(new Column(USERS.TABLE, "*"));
			Criteria c = new Criteria(new Column(USERS.TABLE, USERS.EMAILID), emailId, QueryConstants.EQUAL);
			query.setCriteria(c);
			DataObject dobj = DataAccess.get(query);
			Row row = dobj.getFirstRow(USERS.TABLE);
			System.out.println(row);
			if (row != null) {
				return true;
			}
		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean createUser(String userName, String emailId, String phoneNumber, String gender, String role,
			String timeZone, String password) {
		try {
			InsertQueryImpl iq = new InsertQueryImpl("USERS", 1);
			Row r = new Row("USERS");
			r.set("NAME", userName);
			r.set("EMAILID", emailId);
			r.set("PHONENUMBER", phoneNumber);
			r.set("ROLE", role);
			r.set("TIMEZONE", timeZone);
			r.set("PASSWORD", password);
			r.set("BALANCE", 2000);
			r.set("ACTIVE", 1);
			// r.set("GENDER", gender);
			iq.addRow(r);
			DataAccess.insert(iq);
			return true;
		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean updateUserData(String phoneNumber1, String newPassword,boolean isPassword) {
		try {
			Long phoneNumber = Long.parseLong(phoneNumber1);
			Persistence per = (Persistence) BeanUtil.lookup("Persistence");

			UpdateQuery s = new UpdateQueryImpl(USERS.TABLE);
			Criteria c = new Criteria(Column.getColumn(USERS.TABLE, USERS.PHONENUMBER), phoneNumber,
					QueryConstants.EQUAL);
			s.setCriteria(c);
			if(isPassword) {
				s.setUpdateColumn(USERS.PASSWORD, newPassword);
			}else {
				double balance= Double.parseDouble(newPassword);
				s.setUpdateColumn(USERS.BALANCE, balance);
			}
			
			int row = per.update(s);
			if (row > 0) {
				return true;
			}

		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteAccount(String phoneNumber1) {
		try {
			Long phoneNumber = Long.parseLong(phoneNumber1);
			Persistence per = (Persistence) BeanUtil.lookup("Persistence");

			UpdateQuery s = new UpdateQueryImpl(USERS.TABLE);
			Criteria c = new Criteria(Column.getColumn(USERS.TABLE, USERS.PHONENUMBER), phoneNumber,
					QueryConstants.EQUAL);
			s.setCriteria(c);
			s.setUpdateColumn(USERS.ACTIVE, 0);
			int row = per.update(s);
			if (row > 0) {
				return true;
			}
		} catch (DataAccessException f) {
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	
}
