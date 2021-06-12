package org.tiddev.serviceManager.views.logout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("logout")
@PageTitle("Logout")
public class LogoutView extends Composite<VerticalLayout> {

    public LogoutView() {
        UI.getCurrent().getPage().setLocation("http://localhost:8080/auth/realms/ServiceManager/protocol/openid-connect/logout"
        		+ "?redirect_uri=http://localhost:3333/serviceManager");
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }

}