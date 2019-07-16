package kb.project.c4model.system;

import cc.catalysts.structurizr.ModelPostProcessor;
import cc.catalysts.structurizr.ViewProvider;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.PaperSize;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;
import javax.annotation.Nonnull;
import kb.project.c4model.Personas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ATM implements ViewProvider, ModelPostProcessor {

  private final SoftwareSystem system;

  @Autowired
  public ATM(Model model, Personas personas, ExternalSystems external) {
    system = model.addSoftwareSystem(Location.Internal, "ATM",
            "Allows customers to withdraw cash");

    system.uses(external.getAtmNetwork(), "Sends transaction information using");
    personas.getExternalCustomer().uses(system, "Withdraws cash using");
  }

  public SoftwareSystem getATM() {
    return system;
  }

  /**
   * Set relationships
   * @param model
   */
  @Override
  public void postProcess(@Nonnull Model model) {

  }

  @Override
  public void createViews(ViewSet viewSet) {
    /*
    final SystemContextView contextView = viewSet.createSystemContextView(system, "atm_sc", "ATM system view");
    contextView.addNearestNeighbours(system);
    contextView.setPaperSize(PaperSize.A4_Landscape);
    contextView.setEnterpriseBoundaryVisible(true);
    */

    /*
    final ContainerView containerView = viewSet.createContainerView(system, "atm_c", "ATM container view");
    containerView.setPaperSize(PaperSize.A5_Landscape);
    containerView.addAllContainersAndInfluencers();
    containerView.addDependentSoftwareSystems();
    */

  }

}
