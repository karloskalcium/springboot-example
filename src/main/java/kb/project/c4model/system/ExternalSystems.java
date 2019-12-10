package kb.project.c4model.system;

import com.structurizr.model.InteractionStyle;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import kb.project.c4model.Personas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalSystems {

  private final SoftwareSystem office365;
  private final SoftwareSystem atmNetwork;

  @Autowired
  public ExternalSystems(Model model, Personas personas) {
    office365 = model.addSoftwareSystem(Location.External, "Office365", "Microsoft Cloud office suite");
    office365.setUrl("https://products.office.com/en-us/compare-all-microsoft-office-products?&activetab=tab%3aprimaryr2");
    atmNetwork = model.addSoftwareSystem(Location.External, "ATM Network", "Interbank network that connects ATMs");
  }

  public SoftwareSystem getOffice365() { return office365; }
  public SoftwareSystem getAtmNetwork() { return atmNetwork; }

}
