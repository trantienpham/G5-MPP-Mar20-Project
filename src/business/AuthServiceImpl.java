package business;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class AuthServiceImpl implements AuthServiceInterface {
	AuthorizationLevel currentAuth = null; 
	RepositoryInterface<SystemUser> systemUserRepository;
	DataAccess dataAccess;

	AuthServiceImpl(RepositoryInterface<SystemUser> systemuserRepository) {
		this.systemUserRepository = systemuserRepository;
		dataAccess = new DataAccessFacade();
	}
	
	@Override
	public boolean isAuthenticate() {
		return currentAuth == null;
	}

	@Override
	public AuthorizationLevel getCurrentAuth() {
		return currentAuth;
	}

	@Override
	public SystemUser login(String username, String password) throws LoginException {
		SystemUser user = systemUserRepository.get(username);
		if (user == null) {
			throw new LoginException("Usernam " + username + " not found");
		}
		String passwordFound = user.getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = user.getAuthorization();
		return user;
	}
	
	@Override
	public void logout() {
		currentAuth = null;
	}
}
