package business;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class CheckoutRecord implements Serializable{
    private String id;    
    private LibraryMember libraryMember;
    private List<CheckoutEntry> checkoutEntries;//ChecoutRecord consists of many of entries 
    private List<Fine> fines;   

    public LibraryMember getLibraryMember() {
        return libraryMember;
    }

    public void setLibraryMember(LibraryMember libraryMember) {
        this.libraryMember = libraryMember;
    }

    public List<CheckoutEntry> getCheckoutEntries() {
        return checkoutEntries;
    }

    public void setCheckoutEntries(List<CheckoutEntry> checkoutEntries) {
        this.checkoutEntries = checkoutEntries;
    }

    public List<Fine> getFines() {
        return fines;
    }

    public void setFines(List<Fine> fines) {
        this.fines = fines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
