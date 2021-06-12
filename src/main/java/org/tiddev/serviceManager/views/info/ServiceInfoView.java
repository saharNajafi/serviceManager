package org.tiddev.serviceManager.views.info;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiddev.serviceManager.model.ServiceInfo;
import org.tiddev.serviceManager.service.ServerManagerService;
import org.tiddev.serviceManager.views.main.MainView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "serviceManager/serviceInfo", layout = MainView.class)
@PageTitle("List")
@CssImport(value = "./styles/views/list/list-view.css", include = "lumo-badge")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")

public class ServiceInfoView extends Div {

	private Button stopBtn = new Button();
	private Button startBtn = new Button();
	Button tcpStatusBtn = new Button();
	Button syslogBtn = new Button();
	private final ServerManagerService serverManager;
	private ServiceInfo serviceInfo;
	private String status;
	private String tcpStatus = "";
	private String syslog = "";
	private Grid<ServiceInfo> grid;;
	private ListDataProvider<ServiceInfo> dataProvider;

	@Autowired
	public ServiceInfoView(ServerManagerService serverManager) {
		this.serverManager = serverManager;
		setId("list-view");
		serviceInfo = VaadinSession.getCurrent().getAttribute(ServiceInfo.class);
		updateGrid();
		createGrid();
		 add(grid);
		stopService();
		startService();
		getSyslogService();
		getTCPStatusService();
		
	}

	private void updateGrid() {
		UI.getCurrent().getPage().executeJs("jQuery(function(){" + "setInterval(function(){"
				+ " location.reload();"
				+ "$('.site-content').css('visibility', 'hidden'); "
				+ "}, 10000);" 
				+ "});");
		
	}

	private ServiceInfo getServiceInfo(ServiceInfo serviceInfo) {
		try {
			serviceInfo = serverManager.getServiceInfo(serviceInfo);
		} catch (Exception ex) {
			Notification.show("An error has occurred");
		}
		return serviceInfo;
	}

	private void stopService() {
		stopBtn.addClickListener(e -> {
			dialogServiceActive("Are you sure to stop the service?").open();
		});
	}

	private void startService() {
		startBtn.addClickListener(e -> {
			dialogServiceActive("Are you sure to start the service?").open();
		});
	}

	private void getTCPStatusService() {
		tcpStatusBtn.addClickListener(e -> {
			try {
				tcpStatus = serverManager.getTcpStatus(serviceInfo);
				dialog(tcpStatus).open();
			} catch (Exception ex) {
				Notification.show("An error has occurred");
			}
		});
	}

	private void getSyslogService() {
		syslogBtn.addClickListener(e -> {
			try {
				syslog = serverManager.getServiceSyslog(serviceInfo);
				dialog(syslog).open();
			} catch (Exception ex) {
				Notification.show("An error has occurred");
			}
		});
	}

	private void changeServiceActive() {
		try {
			serviceInfo = serverManager.changeServiceActive(serviceInfo);
			if (serviceInfo != null) {
				status = serviceInfo.getServiceStatus();
				stopBtn.setEnabled(status.equals("active"));
				startBtn.setEnabled(!status.equals("active"));
				dataProvider = new ListDataProvider<>(Arrays.asList(serviceInfo));
				grid.setDataProvider(dataProvider);
			}
		} catch (Exception ex) {
			Notification.show("An error has occurred");
		}
	}

	private void createGrid() {
		grid = new Grid<>();
		createGridComponent();
		addColumnsToGrid();
		createStopColumn();
		createStartColumn();
		createTcpStatusColumn();
		createSyslogColumn();
	}

	private void createGridComponent() {
		// Execute JavaScript in the currently processed page
		serviceInfo = getServiceInfo(serviceInfo);
		grid.setId("grid");
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
		grid.setHeightByRows(true);
		status = serviceInfo.getServiceStatus();
		stopBtn.setEnabled(status.equals("active"));
		startBtn.setEnabled(!status.equals("active"));
		dataProvider = new ListDataProvider<>(Arrays.asList(serviceInfo));
		grid.setDataProvider(dataProvider);
	}

	private void addColumnsToGrid() {
		createNameColumn();
		createStatusColumn();
	}

	private void createNameColumn() {
		grid.addColumn(ServiceInfo::getServiceName).setHeader("Name").setWidth("120px").setFlexGrow(0);
	}

	private void createStatusColumn() {
		grid.addColumn(ServiceInfo::getServiceStatus).setHeader("Status").setWidth("120px").setFlexGrow(0).setId("statusId");
	}

	private void createStopColumn() {
		grid.addColumn(new ComponentRenderer<>(serviceInfo -> {
			HorizontalLayout hl = new HorizontalLayout();
			hl.setAlignItems(FlexComponent.Alignment.CENTER);
			stopBtn.setText("stop");
			hl.add(stopBtn);
			return hl;
		})).setHeader("Stop").setWidth("120px").setFlexGrow(0).setId("stopId");
	}

	private void createStartColumn() {
		grid.addColumn(new ComponentRenderer<>(serviceInfo -> {
			HorizontalLayout hl = new HorizontalLayout();
			hl.setAlignItems(FlexComponent.Alignment.CENTER);
			startBtn.setText("start");
			hl.add(startBtn);
			return hl;
		})).setHeader("Start").setWidth("120px").setFlexGrow(0);
	}

	private void createTcpStatusColumn() {
		grid.addColumn(new ComponentRenderer<>(serviceInfo -> {
			tcpStatusBtn.setText("TCP Status");
			return tcpStatusBtn;
		})).setHeader("TCP Status");
	}

	private void createSyslogColumn() {
		grid.addColumn(new ComponentRenderer<>(serviceInfo -> {
			syslogBtn.setText("syslog");
			return syslogBtn;
		})).setHeader("Syslog");
	}

	private Dialog dialogServiceActive(String text) {
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(true);
		Div content = new Div();
		content.getStyle().set("white-space", "pre-line");
		content.setText(text);
		Button confirmButton = new Button("Yes", event -> {
			dialog.close();
			changeServiceActive();
		});
		Button cancelButton = new Button("No", event -> {
			dialog.close();
		});
		dialog.add(content, confirmButton, cancelButton);
		return dialog;
	}

	private Dialog dialog(String text) {
		if (text.equals("")) {
			text = "NO data";
		}
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(true);
		Div content = new Div();
		content.getStyle().set("white-space", "pre-line");
		content.setText(text);
		Button cancelButton = new Button("close", event -> {
			dialog.close();
		});
		dialog.add(content, cancelButton);
		return dialog;
	}

}
