package dataaccess;

import java.util.HashMap;

import business.Book;
import business.CheckoutEntry;
import business.CheckoutRecord;
import business.LibraryMember;
import business.User;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public HashMap<String, CheckoutEntry> readCheckoutEntryMap();
	public HashMap<String, CheckoutRecord> readCheckoutRecordMap();
	public void saveNewMember(LibraryMember member);
	public void saveNewBook(Book book);
}
