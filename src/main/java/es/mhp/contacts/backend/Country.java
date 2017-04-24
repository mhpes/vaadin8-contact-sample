package es.mhp.contacts.backend;

/**
 * Created by MHP on 19/04/2017.
 */
public class Country {
    private Long Id;
    private String name;
    private String flag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }




    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
