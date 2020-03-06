package business;

public interface AuthServiceInterface {
	public AuthorizationLevel getCurrentAuth();
	public boolean isAuthenticate();
	public SystemUser login(String username, String password) throws LoginException;
	public void logout();
}
