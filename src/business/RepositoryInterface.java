package business;

import java.util.List;

public interface RepositoryInterface<T> {
	public T get(Object id);
	public List<T> getAll();
	public void save(T entity);
}
