package dataaccess;

import java.util.HashMap;

import business.Book;
import business.CheckoutEntry;
import business.CheckoutRecord;
import business.LibraryMember;
import business.SystemUser;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,SystemUser> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public HashMap<String, CheckoutEntry> readCheckoutEntryMap();
	public HashMap<String, CheckoutRecord> readCheckoutRecordMap();
	public void saveNewMember(LibraryMember member);
	public void saveBook(Book book);
	public void saveCheckoutRecord(CheckoutRecord cr);
	public void saveCheckoutEntry(CheckoutEntry ce);
}
