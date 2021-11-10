package common.model;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class User implements Serializable {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private DateTime birthday;
    private char gender;
    private boolean isEmployee;

    public User(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws IllegalArgumentException {
        setEmail(email);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthday(birthday);
        setGender(gender);
    }

    public void setEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email can't be empty.");
        if (!email.contains("@") || !email.substring(email.indexOf("@") + 1).contains(".")) throw new IllegalArgumentException("Invalid email format. Email must respect user@host.domain format.");
        String[] emailParts = email.split("[@._]");
        if (emailParts.length < 1 || emailParts[0] == null || emailParts[0].isEmpty()) throw new IllegalArgumentException("User part of email can't be empty, user@host.domain .");
        if (emailParts.length < 2 || emailParts[1] == null || emailParts[1].isEmpty()) throw new IllegalArgumentException("Host part of email can't be empty, user@host.domain .");
        if (emailParts.length < 3 || emailParts[2] == null || emailParts[2].isEmpty()) throw new IllegalArgumentException("Domain part of email can't be empty, user@host.domain .");
        if (!emailParts[1].matches("[a-zA-Z0-9]*")) throw new IllegalArgumentException("Host part of email can not contain any symbols, user@host.domain .");
        if (!emailParts[2].matches("[a-zA-Z0-9]*")) throw new IllegalArgumentException("Domain part of email can not contain any symbols, user@host.domain .");
        if (emailParts[0].length() > 64) throw new IllegalArgumentException("User part of email can't be more then 64 chars, user@host.domain .");
        if (emailParts[1].length() > 63) throw new IllegalArgumentException("Host part of email can't be more then 63 chars, user@host.domain .");
        if (emailParts[2].length() > 63) throw new IllegalArgumentException("Domain part of email can't be more then 63 chars, user@host.domain .");
        char c = emailParts[1].toUpperCase().charAt(0);
        if (!('A' <= c && c <= 'Z')) throw new IllegalArgumentException("The first char of the email host part has to be a letter, user@host.domain .");
        if (!emailParts[2].matches(".*[a-zA-Z]+.*")) throw new IllegalArgumentException("Domain part of email has to have at least one letter, user@host.domain .");
        this.email = email;
    }

    public void setPassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password can't be empty.");
        if (password.length() < 8) throw new IllegalArgumentException("Password must be at least 8 characters long.");
        if (!password.matches(".*[A-Z]+.*")) throw new IllegalArgumentException("Password must have at least one uppercase letter.");
        if (!password.matches(".*[0-9]+.*")) throw new IllegalArgumentException("Password must have at least one digit.");
        this.password = password;
    }

    public void setFirstName(String firstName) throws IllegalArgumentException {
        if (firstName == null || firstName.isEmpty()) throw new IllegalArgumentException("First name can't be empty.");
        if (firstName.length() < 2) throw new IllegalArgumentException("First name can't have less then 2 characters.");
        if (firstName.length() > 150) throw new IllegalArgumentException("First name can't have more then 150 characters.");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws IllegalArgumentException {
        if (lastName == null || lastName.isEmpty()) throw new IllegalArgumentException("Last name can't be empty.");
        if (lastName.length() < 2) throw new IllegalArgumentException("Last name can't have less then 2 characters.");
        if (lastName.length() > 150) throw new IllegalArgumentException("Last name can't have more then 150 characters.");
        this.lastName = lastName;
    }

    public void setBirthday(LocalDate birthday) throws IllegalArgumentException {
        if (birthday == null) throw new IllegalArgumentException("Birthday can't be null.");
        LocalDate dateOfToday = LocalDate.now();
        if (birthday.isAfter(LocalDate.of(dateOfToday.getYear() - 14, dateOfToday.getMonth(), dateOfToday.getDayOfMonth()))) throw new IllegalArgumentException("User must be at least 14 years old to register.");
        if (birthday.isBefore(LocalDate.of(dateOfToday.getYear() - 150, dateOfToday.getMonth(), dateOfToday.getDayOfMonth()))) throw new IllegalArgumentException("User can't be more then 150 years old.");
        this.birthday = new DateTime(birthday.getDayOfMonth(), birthday.getMonthValue(), birthday.getYear());
    }

    public void setGender(char gender) throws IllegalArgumentException {
        if (!(gender == 'm' || gender == 'M' || gender == 'f' || gender == 'F')) throw new IllegalArgumentException("User can either be male or female.");
        this.gender = gender;
        isEmployee();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public int getAge() {
        return DateTime.yearsBetween(this.birthday);
    }

    public char getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s - %c", firstName, lastName, birthday.toString(), gender);
    }

    public boolean isEmployee() {
        return isEmployee = this instanceof Employee;
    }
}
