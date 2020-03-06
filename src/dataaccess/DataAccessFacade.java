package dataaccess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import business.Author;
import business.Book;
import business.CheckoutEntry;
import business.CheckoutRecord;
import business.Fine;
import business.LibraryMember;
import business.SystemUser;


public class DataAccessFacade implements DataAccess {
	
	enum StorageType {
		BOOKS, MEMBERS, AUTHORS, CHECKOUT_RECORDS, CHECKOUT_ENTRIES, USERS, FINES;
	}
	
	public static final String SEPARATOR = System.getProperty("file.separator");
	
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ SEPARATOR + "src"+ SEPARATOR+ "dataaccess"+ SEPARATOR + "storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	//implement: other save operations
	public void saveNewMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		String memberId = member.getMemberId();
		mems.put(memberId, member);
		saveToStorage(StorageType.MEMBERS, mems);	
	}
	
	@Override
	public void saveBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		String isbn = book.getIsbn();
		books.put(isbn, book);
		saveToStorage(StorageType.BOOKS, books);	
	}
	
	@Override
	public void saveCheckoutRecord(CheckoutRecord cr) {
		HashMap<String, CheckoutRecord> records = readCheckoutRecordMap();
		String id = cr.getId();
		records.put(id, cr);
		saveToStorage(StorageType.CHECKOUT_RECORDS, records);
	}
	@Override
	public void saveCheckoutEntry(CheckoutEntry ce) {
		HashMap<String, CheckoutEntry> entries = readCheckoutEntryMap();
		String id = ce.getId();
		entries.put(id, ce);
		saveToStorage(StorageType.CHECKOUT_ENTRIES, entries);
	}
	
	@SuppressWarnings("unchecked")
	public  HashMap<String,Book> readBooksMap() {
		//Returns a Map with name/value pairs being
		//   isbn -> Book
		return (HashMap<String,Book>) readFromStorage(StorageType.BOOKS);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, LibraryMember> readMemberMap() {
		//Returns a Map with name/value pairs being
		//   memberId -> LibraryMember
		return (HashMap<String, LibraryMember>) readFromStorage(
				StorageType.MEMBERS);
	}
	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, SystemUser> readUserMap() {
		//Returns a Map with name/value pairs being
		//   userId -> User
		return (HashMap<String, SystemUser>)readFromStorage(StorageType.USERS);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, CheckoutEntry> readCheckoutEntryMap() {
		HashMap<String, CheckoutEntry> data = (HashMap<String, CheckoutEntry>)readFromStorage(StorageType.CHECKOUT_ENTRIES);
		if (data == null) data = new HashMap<String, CheckoutEntry>();
		return data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, CheckoutRecord> readCheckoutRecordMap() {
		HashMap<String, CheckoutRecord> data = (HashMap<String, CheckoutRecord>)readFromStorage(StorageType.CHECKOUT_RECORDS);
		if (data == null) data = new HashMap<String, CheckoutRecord>();
		return data;
	}
	
	
	/////load methods - these place test data into the storage area
	///// - used just once at startup  
	
		
	static void loadBookMap(List<Book> bookList) {
		HashMap<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}
	static void loadUserMap(List<SystemUser> userList) {
		HashMap<String, SystemUser> users = new HashMap<String, SystemUser>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}
 
	static void loadMemberMap(List<LibraryMember> memberList) {
		HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}
	
	static void createFilesEmpty() {
		HashMap<String, Author> authors = new HashMap<String, Author>();
		saveToStorage(StorageType.AUTHORS, authors);
		
		HashMap<String, CheckoutEntry> entries = new HashMap<String, CheckoutEntry>();
		saveToStorage(StorageType.CHECKOUT_ENTRIES, entries);
		
		HashMap<String, CheckoutRecord> records = new HashMap<String, CheckoutRecord>();
		saveToStorage(StorageType.CHECKOUT_RECORDS, records);
		
		HashMap<String, Fine> fines = new HashMap<String, Fine>();
		saveToStorage(StorageType.FINES, fines);
	}
	
	static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(ob);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	
	static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			in = new ObjectInputStream(Files.newInputStream(path));
			retVal = in.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}
		return retVal;
	}
	
	
	
	final static class Pair<S,T> implements Serializable{
		
		S first;
		T second;
		Pair(S s, T t) {
			first = s;
			second = t;
		}
		@Override 
		public boolean equals(Object ob) {
			if(ob == null) return false;
			if(this == ob) return true;
			if(ob.getClass() != getClass()) return false;
			@SuppressWarnings("unchecked")
			Pair<S,T> p = (Pair<S,T>)ob;
			return p.first.equals(first) && p.second.equals(second);
		}
		
		@Override 
		public int hashCode() {
			return first.hashCode() + 5 * second.hashCode();
		}
		@Override
		public String toString() {
			return "(" + first.toString() + ", " + second.toString() + ")";
		}
		private static final long serialVersionUID = 5399827794066637059L;
	}
	
}
