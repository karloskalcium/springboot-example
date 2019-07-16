package kb.project.c4model;

import cc.catalysts.structurizr.ModelPostProcessor;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.Tags;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Personas implements ModelPostProcessor {

  //private final Person admin;
  private final Person backOfficeStaff;
  private final Person biAnalyst;
  private final Person businessStakeholder;
  private final Person customerServiceStaff;
  //private final Person externalBusinessUser;
  private final Person externalCustomer;

  // public Person getAdmin() {return admin;}

  public Person getBackOfficeStaff() {
    return backOfficeStaff;
  }

  public Person getBiAnalyst() {
    return biAnalyst;
  }

  public Person getBusinessStakeholder() {
    return businessStakeholder;
  }

  public Person getCustomerServiceStaff() {
    return customerServiceStaff;
  }

  //public Person getExternalBusinessUser() {return externalBusinessUser;}

  public Person getExternalCustomer() {
    return externalCustomer;
  }

  @Autowired
  public Personas(Model model) {
    // admin = model.addPerson(Location.Internal, "Admin", "the administrator of the system");
    biAnalyst = model.addPerson(Location.Internal, "Business Intelligence Analyst",
            "someone building reports");
    businessStakeholder = model.addPerson(Location.Internal, "Business stakeholder",
            "Stakeholder/decision-maker in the organization");
    backOfficeStaff = model.addPerson(Location.Internal, "Back office",
            "Administration and support staff within the bank");
    customerServiceStaff = model.addPerson(Location.Internal, "Customer Service Staff",
            "Customer service staff within the bank");
    /*externalBusinessUser = model.addPerson(Location.External, "external business user",
            "An external partner"); */
    externalCustomer = model.addPerson(Location.External, "external customer",
            "A customer of the bank");

    // Relationships amongst people
    externalCustomer.interactsWith(customerServiceStaff, "Asks questions to", "Telephone");
  }

  @Override
  public void postProcess(@Nonnull Model model) {

    model.getPeople().stream().filter(p -> p.getLocation() == Location.External).forEach(p -> p.addTags(ViewHelper.EXTERNAL));
    model.getPeople().stream().filter(p -> p.getLocation() == Location.Internal).forEach(p -> p.addTags(ViewHelper.INTERNAL));
    model.getPeople().stream().forEach(p -> p.addTags(Tags.PERSON));
  }
}