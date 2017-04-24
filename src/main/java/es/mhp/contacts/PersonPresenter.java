package es.mhp.contacts;

import com.vaadin.ui.Notification;
import es.mhp.contacts.backend.Country;
import es.mhp.contacts.backend.DataService;
import es.mhp.contacts.backend.Person;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MHP on 18/04/2017.
 */
@Component
public class PersonPresenter {
    DataService service = DataService.createDemoService();

    protected PersonView view;

    public void enter(){

    }

    public void setView(PersonView personView) {
        this.view = personView;
    }

//    public void listPersons(String text) {
////        view.listPersons(service.findAll(text));
//        service.findAll(text);
//    }
//    public void listPersons() {
//        view.listPersons(service.findAll());
//    }

    public void editSelectedPerson(Person selected) {
        Person p = service.getById(selected.getId());
        view.setPerson(p);
    }

    public void savePerson(Person person) {
		Person newPerson = service.save(person);
//		listPersons();

	}

    public void deletePerson(Person person) {
		service.delete(person);
	}

	public Person getPerson(Long id){
        return service.getById(id);
    }

    public List<Person> fetchPerson(int offset,int limit,Map<String,Boolean> sortOrder,String filter) {
        List<Person> persons = null;
        if (sortOrder.size() > 0){
            for (Map.Entry<String, Boolean> entry : sortOrder.entrySet()) {
                //System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
                persons = service.findAll(entry.getKey(), entry.getValue(),filter);
            }
        }else{
            persons = service.findAll("id",Boolean.TRUE,filter);
        }
        return persons.stream().limit(limit).skip(offset%limit).collect(Collectors.toList());

    }
    public long count(String filter){
	    return service.count(filter);
    }

    public List<Country> getCountries() {
        return service.getCountries();
    }

}
