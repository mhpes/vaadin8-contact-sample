package es.mhp.contacts;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;
import es.mhp.contacts.backend.Country;
import es.mhp.contacts.backend.Person;
import es.mhp.contacts.components.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@UIScope
@SpringView(name = "PersonView")
@SuppressWarnings("serial")
public class PersonView extends HorizontalLayout implements View {
	Grid<Person> grid;
	PersonEdit editForm;

	@Autowired
	PersonPresenter presenter;

	Binder<Person> binder = new Binder(Person.class);
	DataProvider<Person, String> dataProvider;
	ConfigurableFilterDataProvider<Person, Void, String> filterDataProvider;


	protected PersonPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		createLayout();
		getPresenter().enter();
		getPresenter().setView(this);
//		getPresenter().listPersons();
	}

//	public void listPersons(List<Person> persons) {
////		LIstDataProvider<Person> dataProvider = new ListDataProvider<persons>();
//		dataProvider.refreshAll();
////		grid.setItems(persons);
//	}

	public void setPerson(Person person) {
		binder.setBean(person);
		if (person.getSex().equals("Male")){
			editForm.picture.setSource(new ThemeResource("../mytheme/img/man.jpg"));
		}else if (person.getSex().equals("Female")){
			editForm.picture.setSource(new ThemeResource("../mytheme/img/woman.jpg"));
		}else {
			editForm.picture.setSource(new ThemeResource("../mytheme/img/user.jpg"));
		}
	}

	public PersonView() {
		this.setStyleName("view-style");
	}

	private void createLayout() {
		this.setSizeFull();
		HorizontalLayout filter = new HorizontalLayout();
		filter.setSizeFull();
		//datProvider
		dataProvider = DataProvider.fromFilteringCallbacks(
				// First callback fetches items based on a query
				query -> {
					//Filter
					String qFilter = query.getFilter().orElse(null);
					//Order
					Map<String, Boolean> sortOrders= query.getSortOrders().stream()
							.collect(Collectors.toMap(
									sort -> sort.getSorted(),
									sort -> SortDirection.ASCENDING.equals(
											sort.getDirection())));
					// The index of the first item to load
					int	offset = query.getOffset();
					// The number of items to load
					int	limit = query.getLimit();
					List<Person> persons = getPresenter().fetchPerson(offset, limit,sortOrders,qFilter);
					return	persons.stream();
				},
				// Second callback fetches the number of items for a query
				query -> {
					String qFilter = query.getFilter().orElse(null);
					return (int)getPresenter().count(qFilter); }

		);
		filterDataProvider = dataProvider.withConfigurableFilter();
		//filter
		TextField filterText = createSearch();
		editForm = new PersonEdit();
		//Nuevo Contacto
		Button newContact = new Button("New Contact", VaadinIcons.PLUS);
		newContact.addClickListener(evt -> {
			Person newPerson = new Person();
			setPerson(newPerson);
			editForm.setVisible(true);
			editForm.delete.setVisible(false);
		});
		newContact.setStyleName(ValoTheme.BUTTON_PRIMARY);
		newContact.setWidth("100%");
		filter.addComponents(filterText, newContact);
		filter.setExpandRatio(filterText, 8.0f);
		filter.setExpandRatio(newContact, 2.0f);
		VerticalLayout left = new VerticalLayout();

		//Grid
		grid = new Grid(Person.class);
		grid.setDataProvider(filterDataProvider);
		grid.setSizeFull();
		grid.removeAllColumns();
		grid.addColumn(person -> person.getFirstName()).setCaption("First Name").setSortProperty("firstName");
		grid.addColumn(person -> person.getLastName()).setCaption("Last Name").setSortProperty("lastName");
		grid.addColumn(person -> person.getEmail()).setCaption("Email").setSortProperty("email");
		grid.addColumn(person -> new ExternalResource(person.getCountry().getFlag()),new ImageRenderer()).setCaption("country");
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.addSelectionListener(evt -> {
			if (grid.asSingleSelect().getValue() != null) {
				getPresenter().editSelectedPerson(grid.asSingleSelect().getValue());
				editForm.setVisible(true);
			}
		});
		left.addComponents(filter,grid);
		//Form
		HorizontalLayout right = new HorizontalLayout();
		right.setSizeFull();
		right.setMargin(true);
		right.setSpacing(true);


		this.addComponents(left,editForm);
		//bind form
		bindForm();
//		binder.bindInstanceFields(editForm);
		//Actions form
		actionsForm();
		editForm.setVisible(false);
	}

	private TextField createSearch(){
		TextField filterText = new TextField("Filter contacts ...");
		filterText.setWidth("100%");

		filterText.addValueChangeListener(evt -> {
			String filter = evt.getValue();
			if (filter.trim().isEmpty()){
				filter = null;
			}
			//TODO: error to produce loop when setFilter
			filterDataProvider.setFilter(filter);
		});
		return filterText;
	}

	private void bindForm() {
		binder.forField(editForm.firstName)
				.asRequired("Every person must have a firstName")
				.bind(Person::getFirstName,Person::setFirstName);
		binder.forField(editForm.lastName)
				.asRequired("Every person must have a lastName")
				.bind(Person::getLastName,Person::setLastName);
		binder.forField(editForm.dateOfBirth).bind(Person::getDateOfBirth,Person::setDateOfBirth);
		binder.forField(editForm.phone).bind(Person::getPhone,Person::setPhone);
		binder.forField(editForm.email)
				// Explicit validator instance
				.withValidator(new EmailValidator(
						"This doesn't look like a valid email address"))
				.bind(Person::getEmail, Person::setEmail);
		binder.forField(editForm.sex).bind(Person::getSex,Person::setSex);
		binder.forField(editForm.notes).bind(Person::getNotes,Person::setNotes);
		binder.forField(editForm.country).bind(Person::getCountry,Person::setCountry);
		binder.forField(editForm.active.getBeginDateField()).bind(Person::getActiveFrom,Person::setActiveFrom);
		binder.forField(editForm.active.getEndDateField()).bind(Person::getActiveTo,Person::setActiveTo);
		ListDataProvider<Country> dataProvider = DataProvider.ofCollection(getPresenter().getCountries());
		editForm.country.setDataProvider(dataProvider);
		editForm.country.setItemCaptionGenerator(Country::getName);
	}
	private void actionsForm() {
		this.editForm.cancel.addClickListener(evt -> {
			grid.asSingleSelect().clear();
			binder.setBean(null);
			editForm.setVisible(false);

		});
		this.editForm.save.addClickListener(evt -> {
			if (binder.validate().isOk()) {
				getPresenter().savePerson(binder.getBean());
				filterDataProvider.refreshAll();
				editForm.setVisible(false);
				Notification.show("Person update successfully",
						Notification.Type.HUMANIZED_MESSAGE);
			}
		});

		this.editForm.delete.addClickListener(evt -> {
			final ConfirmDialog cd = new ConfirmDialog(UI.getCurrent());
			cd.confirm("Remove contact",
					"Are you sure?", "Ok",
					"Cancel", new Button.ClickListener() {

						private static final long serialVersionUID = 2376740681091516971L;

						@Override
						public void buttonClick(Button.ClickEvent e) {
							if (e.getButton().getData().equals(ConfirmDialog.USER_CONFIRM_OK)) {
								getPresenter().deletePerson(binder.getBean());
								editForm.setVisible(false);
								filterDataProvider.refreshAll();
								Notification.show("Person delete successfully",
										Notification.Type.HUMANIZED_MESSAGE);

							}
							cd.removeConfirm();
						}
					});



		});
	}


}
