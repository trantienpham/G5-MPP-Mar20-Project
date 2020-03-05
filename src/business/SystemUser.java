package business;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SystemUser implements Serializable {    
    
    public enum AuthorizationLevel {ADMINISTRATOR, LIBRARIAN, BOTH};
    
    private AuthorizationLevel role;
    private String username;
    private String password;

    
    public AuthorizationLevel getRole() {
        return role;
    }

    public void setRole(AuthorizationLevel role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
