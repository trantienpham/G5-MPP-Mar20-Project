package business;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CheckoutEntry implements Serializable{

    private String id;//same as database id
    private String checkoutDate;
    private String dueDate;
    private BookCopy bookCopy;
    private CheckoutRecord checkoutRecord;//TODO: maybe it is not needed

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }
    
    public CheckoutRecord getCheckoutRecord() {
        return checkoutRecord;
    }

    public void setCheckoutRecord(CheckoutRecord checkoutRecord) {
        this.checkoutRecord = checkoutRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
