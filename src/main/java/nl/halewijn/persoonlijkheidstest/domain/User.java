package nl.halewijn.persoonlijkheidstest.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {

	@Id
	private String emailAddress;

    @NotNull
    private String passwordHash;

    @NotNull
    private boolean isAdmin = false;

	public User(String emailAddress, boolean isAdmin) {
        this.emailAddress = emailAddress;
        this.isAdmin = isAdmin;
	}

	public User() {
        /*
         * ThymeLeaf requires us to have default constructors, further explanation can be found here:
         * http://javarevisited.blogspot.in/2014/01/why-default-or-no-argument-constructor-java-class.html
         */
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

    public boolean isAdmin() {
        return isAdmin;
    }

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}