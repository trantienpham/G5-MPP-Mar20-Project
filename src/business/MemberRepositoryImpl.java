package business;

import java.util.ArrayList;
import java.util.List;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class MemberRepositoryImpl implements RepositoryInterface<LibraryMember> {
	DataAccess dataAccess;
	MemberRepositoryImpl() {
		dataAccess = new DataAccessFacade();
	}

	@Override
	public LibraryMember get(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LibraryMember> getAll() {
		List<LibraryMember> retval = new ArrayList<>();
		retval.addAll(dataAccess.readMemberMap().values());
		return retval;
	}

	@Override
	public void save(LibraryMember entity) {
		dataAccess.saveNewMember(entity);
	}

}
