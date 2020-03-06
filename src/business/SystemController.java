package business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import utils.Message;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;

	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if (!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();

	}

	public User authenticate(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if (!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();
		return map.get(id);

	}

	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}

	@Override
	public List<LibraryMember> allMembers() {
		DataAccess da = new DataAccessFacade();
		List<LibraryMember> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().values());
		return retval;
	}

	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}

	@Override
	public List<Book> allBooks() {
		DataAccess da = new DataAccessFacade();
		List<Book> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().values());
		return retval;
	}

	@Override
	public void saveNewMember(LibraryMember member) {
		DataAccess da = new DataAccessFacade();
		da.saveNewMember(member);
	}

	@Override
	public List<CheckoutEntry> allCheckoutEntries() {
		DataAccess da = new DataAccessFacade();
		List<CheckoutEntry> retval = new ArrayList<CheckoutEntry>();
		retval.addAll(da.readCheckoutEntryMap().values());
		return retval;
	}

	@Override
	public List<CheckoutRecord> allCheckoutRecords() {
		DataAccess da = new DataAccessFacade();
		List<CheckoutRecord> retval = new ArrayList<CheckoutRecord>();
		retval.addAll(da.readCheckoutRecordMap().values());
		return retval;
	}

	@Override
	public void saveBook(Book book) {
		DataAccess da = new DataAccessFacade();
		da.saveBook(book);
	}
	
	@Override
	public void checkoutBookByMember(Book book, LibraryMember member, LocalDate dueDate) {
        CheckoutEntry checkoutEntry = new CheckoutEntry();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String id = String.valueOf(new Random().nextInt(1000));
        checkoutEntry.setId(id);
        checkoutEntry.setCheckoutDate(formatter.format(dueDate));
        checkoutEntry.setDueDate(formatter.format(dueDate));
        BookCopy[] copies = book.getCopies();
        BookCopy bookCopy = null;
        for(BookCopy bc:copies) {
        	if (bc.isAvailable()) {
        		bookCopy = bc;
                break;
        	}
        }
        if (bookCopy != null) {
            bookCopy.changeAvailability();
            checkoutEntry.setBookCopy(bookCopy);

            try {
            	CheckoutRecord cr = getCheckoutRecordByMember(member);
            	if (cr == null) {
            		cr = new CheckoutRecord();
                    id = String.valueOf(new Random().nextInt(1000));
            		cr.setId(id);
            		cr.setLibraryMember(member);
            		addCheckoutRecord(cr);
            	}
                checkoutEntry.setCheckoutRecord(cr);
                addCheckoutEntry(checkoutEntry);
                Message.showSuccessMessage("Add Checkout Record", "Saving Checkout Record Sucess", "");            
            } catch (Exception e) {
               Message.showErrorMessage("Add Checkout Record", "Saving Checkout Record Failed. Exception message: ",  e.getMessage());  
            }
        }

	}
	private CheckoutRecord getCheckoutRecordByMember(LibraryMember member) {
		DataAccess da = new DataAccessFacade();
		Collection<CheckoutRecord> records = da.readCheckoutRecordMap().values();
		for(CheckoutRecord cr: records) {
			if (cr.getLibraryMember().getMemberId().equalsIgnoreCase(member.getMemberId())) {
				return cr;
			}
		}
		return null;
	}
	private void addCheckoutRecord(CheckoutRecord cr) {
		DataAccess da = new DataAccessFacade();
		da.saveCheckoutRecord(cr);
	}
	private void addCheckoutEntry(CheckoutEntry ce) {
		DataAccess da = new DataAccessFacade();
		da.saveCheckoutEntry(ce);
	}
}
