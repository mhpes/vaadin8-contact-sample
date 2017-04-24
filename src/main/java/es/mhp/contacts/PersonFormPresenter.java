package es.mhp.contacts;

import com.vaadin.ui.Notification;
import es.mhp.contacts.PersonView;
import es.mhp.contacts.backend.Country;
import es.mhp.contacts.backend.DataService;
import es.mhp.contacts.backend.Person;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MHP on 18/04/2017.
 */
@Component
public class PersonFormPresenter {
    DataService service = DataService.createDemoService();


}
