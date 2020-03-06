package business;

import java.time.LocalDate;
import java.util.List;

public interface CheckoutInterface extends RepositoryInterface<CheckoutRecord> {
	public CheckoutRecord getByMember(LibraryMember member);
	public void checkout(Book book, LibraryMember member, LocalDate dueDate);
	public List<CheckoutEntry> searchBy(String isbn, String memberId);
}
