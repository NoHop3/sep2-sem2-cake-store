package viewmodel.viewstate;

import viewmodel.object.UserViewModel;

public class UserManagementViewState {

    private UserViewModel selectedUserToBeEdited;

    public UserManagementViewState() {
        selectedUserToBeEdited = null;
    }

    public void setSelectedUser(UserViewModel user) {
        selectedUserToBeEdited = user;
    }

    public UserViewModel getSelectedUser() {
        return selectedUserToBeEdited;
    }
}
