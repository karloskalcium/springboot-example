package kb.project.c4model.system;

import cc.catalysts.structurizr.ModelPostProcessor;
import cc.catalysts.structurizr.ViewProvider;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.PaperSize;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;
import javax.annotation.Nonnull;
import kb.project.c4model.Personas;
import kb.project.c4model.ViewHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainframeBanking implements ViewProvider, ModelPostProcessor {

  private final Container mainframeDatabase;
  private final Container mainframeApplication;
  private final SoftwareSystem system;

  @Autowired
  public MainframeBanking(Model model, Personas personas, ExternalSystems external) {
    system = model.addSoftwareSystem(Location.Internal, "mainframeBanking",
            "Stores all of the core banking information about customers, accounts, transactions, etc.");

    mainframeDatabase = system.addContainer("Mainframe database", "Legacy data store", "Oracle");
    mainframeApplication = system.addContainer("Mainframe application", "Legacy monolithic mainframe app",
            "COBOL");
    mainframeApplication.uses(mainframeDatabase, "Data storage", "");
    external.getAtmNetwork().uses(mainframeApplication, "Verifies transactions using", "XML (proprietary)");

    // People relationships
    personas.getCustomerServiceStaff().uses(mainframeApplication, "Uses");
    personas.getBackOfficeStaff().uses(mainframeApplication, "Uses");
  }

  public Container getMainframeApplication() { return mainframeApplication; }
  public Container getMainframeDatabase() { return mainframeDatabase; }
  public SoftwareSystem getMainframeBanking() {
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
    final SystemContextView contextView = viewSet.createSystemContextView(system, "mainframe_sc", "Mainframe Banking system view");
    contextView.addNearestNeighbours(system);
    contextView.setPaperSize(PaperSize.A4_Landscape);
    contextView.setEnterpriseBoundaryVisible(true);

    final ContainerView containerView = viewSet.createContainerView(system, "mainframe_c", "Mainframe Banking container view");
    containerView.setPaperSize(PaperSize.A5_Landscape);
    containerView.addAllContainersAndInfluencers();
    containerView.addDependentSoftwareSystems();
  }

}
