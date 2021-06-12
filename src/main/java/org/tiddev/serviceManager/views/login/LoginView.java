/*package org.tiddev.serviceManager.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.tiddev.serviceManager.model.User;
import org.tiddev.serviceManager.service.LoginService;

@Route(value = "serviceManager/login")
@PageTitle("Login")
@CssImport("./styles/views/login/login-view.css")

public class LoginView extends Div {

    public LoginView(LoginService loginService) {
        setId("login-view");
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        add(
                new H1("Welcome"),
                username,
                password,
                new Button("Login", event -> {
                    try {
                       String userIP = loginService.readFile();
                        loginService.authenticate(username.getValue(), password.getValue(), userIP);
                        User user = new User();
                        user.setUsername(username.getValue());
                        user.setPassword(password.getValue());
                        VaadinSession.getCurrent().setAttribute(User.class, user);
//                        UI.getCurrent().getPage().setLocation("serviceManager/services");
                        UI.getCurrent().navigate("serviceManager/services");
                    } catch (Exception e) {
                        Notification.show("Wrong credentials.");
                    }
                })
        );
    }

}*/