package business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import utils.Message;

public class CheckoutRepositoryImpl implements CheckoutInterface {
	
	DataAccess dataAccess;
	
	CheckoutRepositoryImpl() {
		dataAccess = new DataAccessFacade();
	}

	@Override
	public CheckoutRecord get(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CheckoutRecord> getAll() {
		List<CheckoutRecord> retval = new ArrayList<CheckoutRecord>();
		retval.addAll(dataAccess.readCheckoutRecordMap().values());
		return retval;
	}

	@Override
	public void save(CheckoutRecord entity) {
		dataAccess.saveCheckoutRecord(entity);
	}

	@Override
	public CheckoutRecord getByMember(LibraryMember member) {
		Optional<CheckoutRecord> optional = getAll().stream().findAny().filter(x -> x.getLibraryMember().getMemberId().equalsIgnoreCase(member.getMemberId()));
		if (optional.isPresent()) return optional.get();
		return null;
	}
	
	@Override
	public void checkout(Book book, LibraryMember member, LocalDate dueDate) {
		CheckoutEntry checkoutEntry = new CheckoutEntry();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String id = String.valueOf(new Random().nextInt(1000));
        checkoutEntry.setId(id);
        checkoutEntry.setCheckoutDate(formatter.format(dueDate));
        checkoutEntry.setDueDate(formatter.format(dueDate));
        BookCopy[] copies = book.getCopies();
        Optional<BookCopy> optional = Arrays.stream(copies).findAny().filter(x -> x.isAvailable()); 
        if (optional.isPresent()) {
            BookCopy bookCopy = optional.get();
            bookCopy.changeAvailability();
            checkoutEntry.setBookCopy(bookCopy);

            try {
            	CheckoutRecord cr = getByMember(member);
            	if (cr == null) {
            		cr = new CheckoutRecord();
                    id = String.valueOf(new Random().nextInt(1000));
            		cr.setId(id);
            		cr.setLibraryMember(member);
            		save(cr);
            	}
                checkoutEntry.setCheckoutRecord(cr);
                dataAccess.saveCheckoutEntry(checkoutEntry);
                Message.showSuccessMessage("Add Checkout Record", "Saving Checkout Record Sucess", "");            
            } catch (Exception e) {
               Message.showErrorMessage("Add Checkout Record", "Saving Checkout Record Failed. Exception message: ",  e.getMessage());  
            }
        }
	}
	
	@Override
	public List<CheckoutEntry> searchBy(String isbn, String memberId) {
		Collection<CheckoutEntry> entries = dataAccess.readCheckoutEntryMap().values();
		return entries.stream().filter(x -> {
			int flag = 1;
			if (isbn != "" && !x.getBookCopy().getIsbn().equalsIgnoreCase(isbn)) flag = 0;
			if (memberId != "" && !x.getCheckoutRecord().getLibraryMember().getMemberId().equalsIgnoreCase(memberId)) flag = 0;
			
			return flag == 1;
		}).collect(Collectors.toList());
	}
}
