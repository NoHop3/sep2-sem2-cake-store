package common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserList implements Serializable {

    private Map<String, User> userList;

    public UserList() {
        userList = new HashMap<>();
    }

    public int getSize() {
        return new ArrayList<>(userList.values()).size();
    }

    public ArrayList<User> getAllUsers() throws IllegalStateException{
        if(getSize()!=0)
        return new ArrayList<>(userList.values());
        throw new IllegalStateException("No users to return!");
    }

    public void addUser(User user) throws IllegalArgumentException{
        if(user!=null)
        userList.put(user.getEmail(), user);
        else throw new IllegalArgumentException("User argument cannot be null");
    }

    public User removeUser(String email) throws IllegalArgumentException{
        if(email!=null)
            return userList.remove(email);
        else throw new IllegalArgumentException("Email cannot be null");

    }

    public User removeUser(User user) throws IllegalArgumentException{
        if(user!=null)
        return removeUser(user.getEmail());
        else throw new IllegalArgumentException("User argument cannot be null");
    }

    public User getUser(String email) throws IllegalArgumentException {
        if(email!=null)
        return userList.get(email);
        else throw new IllegalArgumentException("Email cannot be null");
    }

    public User getUser(User user) throws IllegalArgumentException{
        if(user!=null)
            return userList.get(user.getEmail());
        else throw new IllegalArgumentException("User argument cannot be null");
    }
}