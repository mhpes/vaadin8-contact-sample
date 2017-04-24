package es.mhp.contacts.backend;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataService {

    // Create dummy data by randomly combining first and last names
    static String[] fnames = { "Peter", "Alice", "John", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene", "Lisa",
            "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
            "Jennifer" };
    static String[] lnames = { "Smith", "Johnson", "Williams", "Jones",
            "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Thompson", "Young", "King", "Robinson" };
    static String[] fsex = { "Male", "Female"};
    private static DataService instance;

    public static DataService createDemoService() {
        if (instance == null) {

            final DataService contactService = new DataService();

            Random r = new Random(0);
//            Calendar cal = Calendar.getInstance();
            Country c = new Country();
            c.setId(5L);
            c.setName("Spain");
            c.setFlag("http://footballpool.dataaccess.eu/images/flags/es.gif");
            for (int i = 0; i < 100; i++) {
                Person person = new Person();
                person.setFirstName(fnames[r.nextInt(fnames.length)]);
                person.setSex(fsex[r.nextInt(fsex.length)]);
                person.setLastName(lnames[r.nextInt(fnames.length)]);
                person.setEmail(person.getFirstName().toLowerCase() + "@"
                        + person.getLastName().toLowerCase() + ".com");
                person.setPhone("+ 358 555 " + (100 + r.nextInt(900)));
//                cal.set(1930 + r.nextInt(70),
//                        r.nextInt(11), r.nextInt(28));
                person.setActiveFrom(LocalDate.of(1953,2,2));
                person.setActiveTo(LocalDate.now());
                LocalDate date = LocalDate.of(1930 + r.nextInt(70),
                        r.nextInt(11)+1,
                        r.nextInt(27)+1);
                person.setDateOfBirth(date);

                person.setCountry(c);

                contactService.save(person);
            }
            instance = contactService;
        }

        return instance;
    }

    private HashMap<Long, Person> contacts = new HashMap<>();
    private long nextId = 0;



    public synchronized List<Person> findAll(String order,Boolean asc,String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Person contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(DataService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Person>() {

            @Override
            public int compare(Person o1, Person o2) {

                if (order.endsWith("firstName")) {
                    if (Boolean.TRUE.equals(asc)) {
                        return o1.getFirstName().compareTo(o2.getFirstName());
                    } else {
                        return o2.getFirstName().compareTo(o1.getFirstName());
                    }
                }else if (order.endsWith("lastName")){
                        if (Boolean.TRUE.equals(asc)){
                            return o1.getLastName().compareTo(o2.getLastName());
                        }else {
                            return o2.getLastName().compareTo(o1.getLastName());
                        }
                }else if (order.endsWith("email")){
                    if (Boolean.TRUE.equals(asc)){
                        return o1.getEmail().compareTo(o2.getEmail());
                    }else{
                        return o2.getEmail().compareTo(o1.getEmail());
                    }
                }else{
                    return (int)(o2.getId() - o1.getId());
                }
            }
        });
        return arrayList;
    }

    public synchronized long count(String stringFilter) {
        long count = 0;
        for (Person contact : contacts.values()) {
            boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                    || contact.toString().toLowerCase()
                    .contains(stringFilter.toLowerCase());
            if (passesFilter) {
                count++;
            }
        }
        return count;
    }

    public synchronized void delete(Person value) {
        contacts.remove(value.getId());
    }

    public synchronized Person save(Person entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Person) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        contacts.put(entry.getId(), entry);
        return entry;
    }

    public synchronized Person getFirst() {
        Person first = null;
        for (Person contact : contacts.values()) {
            first = contact;
            break;
        }
        return first;
    }

    public synchronized Person getById(Long id) {
        return contacts.get(id);
    }

    public List<Country> getCountries() {

        List<Country> tmp = new ArrayList<Country>();

        String requestURL = "http://footballpool.dataaccess.eu/data/info.wso/Teams/JSON/debug";
        try {
            URL request = new URL(requestURL);
            URLConnection connection = request.openConnection();
            connection.setDoOutput(true);
            Scanner scanner = new Scanner(request.openStream());
            String response = scanner.useDelimiter("\\Z").next();
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTeam = jsonArray.getJSONObject(i);
                Country c = new Country();
                c.setId(Long.valueOf(jsonTeam.getInt("iId")));
                c.setName(jsonTeam.getString("sName"));
                c.setFlag(jsonTeam.getString("sCountryFlag"));
//                teamDTO.setWikipediaUrl(jsonTeam.getString("sWikipediaURL"));
                tmp.add(c);
            }
            scanner.close();
        } catch (Exception e) {
            com.vaadin.ui.Notification.show("Error");
        }

        return tmp;
    }
}
