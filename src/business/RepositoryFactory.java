package business;

public final class RepositoryFactory {
	private static RepositoryInterface<Book> bookRepository;
	public static RepositoryInterface<Book> getBookRepository() {
		if (bookRepository == null) {
			bookRepository = new BookRepositoryImpl();
		}
		return bookRepository;
	}
	
	private static RepositoryInterface<LibraryMember> memberRepository;
	public static RepositoryInterface<LibraryMember> getMemberRepository() {
		if (memberRepository == null) {
			memberRepository = new MemberRepositoryImpl();
		}
		return memberRepository;
	}
	
	private static RepositoryInterface<SystemUser> userRepository;
	public static RepositoryInterface<SystemUser> getUserRepository() {
		if (userRepository == null) {
			userRepository = new UserRepositoryImpl();
		}
		return userRepository;
	}

	private static CheckoutInterface checkoutRecordRepository;
	public static CheckoutInterface getCheckoutRecordRepository() {
		if (checkoutRecordRepository == null) {
			checkoutRecordRepository = new CheckoutRepositoryImpl();
		}
		return checkoutRecordRepository;
	}
		
	private static AuthServiceInterface authService;
	public static AuthServiceInterface getAuthService() {
		if (authService == null) {
			RepositoryInterface<SystemUser> systemuserRepository = getUserRepository();
			authService = new AuthServiceImpl(systemuserRepository);
		}
		return authService;
	}
}
