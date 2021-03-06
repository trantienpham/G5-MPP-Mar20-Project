package business;

import java.io.Serializable;

final public class LibraryMember extends Person implements Serializable {
	private String memberId;
	private String fullname;
	private CheckoutRecord checkoutRecord;

	public LibraryMember(String memberId, String fname, String lname, String tel,Address add) {
		super(fname,lname, tel, add);
		this.fullname = fname + " " + lname;
		this.memberId = memberId;		
	}
	
    public CheckoutRecord getCheckoutRecord() {
        return checkoutRecord;
    }

    public void setCheckoutRecord(CheckoutRecord checkoutRecord) {
        this.checkoutRecord = checkoutRecord;
    }
	
	public String getMemberId() {
		return memberId;
	}
	
	public String getFullname() {
		return fullname;
	}

	
	
	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
				", " + getTelephone() + " " + getAddress();
	}

	private static final long serialVersionUID = -2226197306790714013L;
}
