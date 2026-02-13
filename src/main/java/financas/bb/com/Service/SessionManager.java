package financas.bb.com.Service;

import org.springframework.stereotype.Component;

import financas.bb.com.Controller.UserController;
import financas.bb.com.Models.User;

@Component
public class SessionManager {
    private final UserController uc;
    private User user;


    public SessionManager(UserController uc) {
        this.uc = uc;
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        if (user != null) {
            uc.logout(user);
            user = null;
        }
    }

    public User getUser() {
        return user;
    }
}
