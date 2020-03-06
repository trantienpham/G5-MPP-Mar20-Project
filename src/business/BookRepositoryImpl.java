package business;

import java.util.ArrayList;
import java.util.List;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class BookRepositoryImpl implements RepositoryInterface<Book> {
	DataAccess dataAccess;
	BookRepositoryImpl() {
		dataAccess = new DataAccessFacade();
	}
	@Override
	public Book get(Object isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getAll() {
		DataAccess da = new DataAccessFacade();
		List<Book> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().values());
		return retval;
	}

	@Override
	public void save(Book entity) {
		dataAccess.saveBook(entity);
	}

}
