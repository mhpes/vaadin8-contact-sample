package es.mhp.contacts;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import es.mhp.contacts.backend.Country;
import es.mhp.contacts.components.DateRange;
import es.mhp.contacts.components.DateRangeField;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings("serial")
public class PersonEdit extends VerticalLayout {
    protected Image picture;
    protected TextField firstName;
    protected TextField lastName;
    protected TextField email;
    protected TextField phone;
    protected DateField dateOfBirth;
    protected TextArea notes;
    protected RadioButtonGroup<String> sex;
    protected Button save;
    protected Button cancel;
    protected Button delete;
    protected DateRangeField active;
    protected ComboBox<Country> country;

    @Autowired
    PersonFormPresenter presenter;

    protected PersonFormPresenter getPresenter() {
        return presenter;
    }

    public PersonEdit() {
        this.setSizeFull();
        this.setMargin(true);
        this.setSpacing(true);
        HorizontalLayout buttons = new HorizontalLayout();
        //picture
        picture = new Image();
        picture.setHeight("90px");
        picture.setWidth("90px");
        //Form
        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setSizeUndefined();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        phone = new TextField("Phone");
        active = new DateRangeField("Activo") ;
        dateOfBirth = new DateField("Date of Birth");
        List<String> values = Arrays.asList("Male","Female");
        sex = new RadioButtonGroup<String>("sex");
        sex.setItems("Male","Female");
        sex.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        country = new ComboBox("Country");

        form.addComponents(firstName,lastName,email,phone,sex,active,country,dateOfBirth);
        HorizontalLayout h = new HorizontalLayout();
        h.addComponents(form,picture);
        //notes
        notes = new TextArea("notes");
        notes.setSizeFull();
        //buttons
        buttons.setSizeFull();
        save = new Button("save", VaadinIcons.CHECK);
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        cancel = new Button("cancel");
        cancel.addStyleName(ValoTheme.BUTTON_LINK);
        delete = new Button("delete",VaadinIcons.TRASH);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);
        buttons.addComponents(save,cancel,delete);
        //add all
        this.addComponents(h,notes,buttons);
	}
}
