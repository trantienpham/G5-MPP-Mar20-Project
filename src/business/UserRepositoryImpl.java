package business;

import java.util.HashMap;
import java.util.List;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class UserRepositoryImpl implements RepositoryInterface<SystemUser> {
	DataAccess dataAccess;
	UserRepositoryImpl() {
		dataAccess = new DataAccessFacade();
	}

	@Override
	public SystemUser get(Object username) {
		HashMap<String, SystemUser> map = dataAccess.readUserMap();
		return map.get(username);
	}

	@Override
	public List<SystemUser> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(SystemUser entity) {
		// TODO Auto-generated method stub
		
	}


}
