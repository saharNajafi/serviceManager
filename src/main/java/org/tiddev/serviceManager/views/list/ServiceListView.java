package org.tiddev.serviceManager.views.list;

import java.util.List;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.tiddev.serviceManager.model.ServiceInfo;
import org.tiddev.serviceManager.model.User;
import org.tiddev.serviceManager.service.ServerManagerService;
import org.tiddev.serviceManager.views.main.MainView;

@Route(value = "serviceManager/services", layout = MainView.class)
@PageTitle("List")
@CssImport(value = "./styles/views/list/list-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class ServiceListView extends Div {
    private final ServerManagerService serverManager;
    private Grid<ServiceInfo> grid = new Grid<>();
    private ListDataProvider<ServiceInfo> dataProvider;
    private User user;

    @Autowired
    public ServiceListView(ServerManagerService serverManager) {
        this.serverManager = serverManager;
        setId("list-view");
//        setSizeFull();
        user = VaadinSession.getCurrent().getAttribute(User.class);
        getServices(user);
    }

    private void getServices(User user) {
        List<ServiceInfo> serviceInfo;
        try {
            serviceInfo = serverManager.getServices(user);
            if (serviceInfo != null) {
                createGrid();
                dataProvider = new ListDataProvider<>(serviceInfo);
                grid.setDataProvider(dataProvider);
                add(grid);
            }
        } catch (Exception ex) {
            Notification.show("An error has occurred");
        }
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
    }

    private void createGridComponent() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeightByRows(true);
        grid.setHeight("100%");
    }

    private void addColumnsToGrid() {
        createNameColumn();
        createPortColumn();
    }

    private void createNameColumn() {
        grid.addColumn(new ComponentRenderer<>(serviceInfo -> {
            Anchor anchor = new Anchor();
            anchor.setText(serviceInfo.getServiceName());
            anchor.getElement().addEventListener("click", e -> {
                        getServiceInfo(serviceInfo);
                    }
            );
            return anchor;
        })).setHeader("Name").setWidth("120px");
    }

    private void getServiceInfo(ServiceInfo serviceInfo) {
        try {
            if (serviceInfo != null) {
                VaadinSession.getCurrent().setAttribute(ServiceInfo.class, serviceInfo);
                UI.getCurrent().navigate("serviceManager/serviceInfo");
            }
        } catch (Exception ex) {
            Notification.show("An error has occurred");
        }
    }

    private void createPortColumn() {
        grid.addColumn(ServiceInfo::getPort)
                .setHeader("Port").setWidth("120px");
    }

};

