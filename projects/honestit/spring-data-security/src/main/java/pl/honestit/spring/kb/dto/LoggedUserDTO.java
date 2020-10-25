package pl.honestit.spring.kb.dto;

import java.util.Objects;

public class LoggedUserDTO {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggedUserDTO that = (LoggedUserDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return "LoggedUserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
