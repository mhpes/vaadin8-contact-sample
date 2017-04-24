package es.mhp.contacts;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import es.mhp.contacts.backend.DataService;
import es.mhp.contacts.backend.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@SpringUI
@Theme("valo")
public class ContactsUI extends UI {

	@Autowired
	private SpringViewProvider viewProvider;

	@WebListener
	public static class MyContextLoaderListener extends ContextLoaderListener {
	}

	private Navigator navigator;

	private HorizontalLayout viewContainer;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle("Contacts");
		VerticalLayout root = createUILayout();

		navigator = new Navigator(this, viewContainer);
		navigator.addProvider(viewProvider);
		setContent(root);
		navigator.navigateTo("PersonView");
	}

	private VerticalLayout createUILayout() {
		VerticalLayout generalLayout = new VerticalLayout();
		//TITLE
		Label title = new Label("Contacs");
		title.setStyleName(ValoTheme.LABEL_H1);
		//VIEW
		Layout viewLayout = createView();
		generalLayout.addComponents(title,viewLayout);
		generalLayout.setComponentAlignment(title,Alignment.MIDDLE_CENTER);
		return generalLayout;
	}

	private Layout createView() {
		viewContainer = new HorizontalLayout();
		viewContainer.addStyleName("view-container");
		viewContainer.setSizeFull();
		return viewContainer;
	}


}
