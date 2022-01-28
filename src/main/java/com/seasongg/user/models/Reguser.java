package com.seasongg.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seasongg.user.models.permissions.UserPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="regusers")
@NamedQuery(name="Reguser.findAll", query="SELECT r FROM Reguser r")
public class Reguser implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private BigInteger userId;

    private String username;

    @JsonIgnore
    private String password;

    @Column(name="REGISTRATION_TIME")
    @JsonIgnore
    private Timestamp registrationTime;

    @JsonIgnore
    private String salt;

    @OneToMany(mappedBy="reguser")
    @JsonIgnore
    private List<UserPermission> userPermissions;

    public Reguser() {
    }

    public BigInteger getUserId() {
        return this.userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<UserPermission> userPermissions = getUserPermissions();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (UserPermission userPermission : userPermissions) {
            authorities.add(new SimpleGrantedAuthority(userPermission.getPermission().getPermValue()));
        }

        return authorities;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegistrationTime() {
        return this.registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<UserPermission> getUserPermissions() {
        return this.userPermissions;
    }

    public void setUserPermissions(List<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public UserPermission addUserPermission(UserPermission userPermission) {
        getUserPermissions().add(userPermission);
        userPermission.setReguser(this);

        return userPermission;
    }

    public UserPermission removeRound(UserPermission userPermission) {
        getUserPermissions().remove(userPermission);
        userPermission.setReguser(null);

        return userPermission;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reguser reguser = (Reguser) o;
		return Objects.equals(userId, reguser.userId) && Objects.equals(username, reguser.username) && Objects.equals(password, reguser.password) && Objects.equals(registrationTime, reguser.registrationTime) && Objects.equals(salt, reguser.salt) && Objects.equals(userPermissions, reguser.userPermissions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, username, password, registrationTime, salt, userPermissions);
	}

}
