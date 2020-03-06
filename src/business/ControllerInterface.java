package business;

import java.time.LocalDate;
import java.util.List;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public User authenticate(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<LibraryMember> allMembers();
	public List<String> allBookIds();
	public List<Book> allBooks();
	public void saveNewMember(LibraryMember member);
	public void saveBook(Book book);
	public List<CheckoutEntry> allCheckoutEntries();
	public List<CheckoutRecord> allCheckoutRecords();
	public void checkoutBookByMember(Book book, LibraryMember member, LocalDate dueDate);
}
