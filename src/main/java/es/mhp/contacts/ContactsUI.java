package es.mhp.contacts;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.annotation.WebListener;

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

	private Panel viewContainer;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle("Contacts");
		VerticalLayout root = createUILayout();

		setNavigator(new Navigator(this, viewContainer));
		getNavigator().addProvider(viewProvider);
		setContent(root);
		getNavigator().navigateTo("PersonView");
	}

	private VerticalLayout createUILayout() {
		VerticalLayout generalLayout = new VerticalLayout();
		//TITLE
		Label title = new Label("Contacts");
		title.setStyleName(ValoTheme.LABEL_H1);
		// NAVIGATION
		Layout menu = createNavigation();
		//VIEW
		Panel viewLayout = createView();

		generalLayout.addComponents(title, menu, viewLayout);
		generalLayout.setComponentAlignment(title,Alignment.MIDDLE_CENTER);
		return generalLayout;
	}

	private Layout createNavigation() {
		HorizontalLayout menuLayout = new HorizontalLayout();
		Button personView = new Button("People");
		personView.addClickListener(e -> getNavigator().navigateTo(PersonView.PERSON_VIEW));

		Button dragAndDropView = new Button("Drag&Drop");
		dragAndDropView.addClickListener(e -> getNavigator().navigateTo(DragAndDropView.DRAG_AND_DROP_VIEW));

		menuLayout.addComponents(personView, dragAndDropView);
		return menuLayout;
	}

	private Panel createView() {
		viewContainer = new Panel();
		viewContainer.addStyleName("view-container");
		viewContainer.setSizeFull();
		return viewContainer;
	}

}