package kb.project.c4model.system;

import cc.catalysts.structurizr.ModelPostProcessor;
import cc.catalysts.structurizr.ViewProvider;
import com.structurizr.model.*;
import com.structurizr.view.*;

import javax.annotation.Nonnull;
import kb.project.c4model.Personas;
import kb.project.c4model.ViewHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternetBanking implements ViewProvider, ModelPostProcessor {

  private final Container mobileApp;
  private final Container webApplication;
  private final Container apiApplication;
  private final SoftwareSystem system;

  @Autowired
  public InternetBanking(Model model, Personas personas, MainframeBanking mainframe, ExternalSystems external, Reporting reporting) {
    system = model.addSoftwareSystem(Location.Internal, "internetBanking",
            "Allows customers to view information about their bank accounts, and make payments.");

    Container singlePageApplication = system.addContainer("Single-Page Application",
            "Provides all of the Internet banking functionality to customers via their web browser.",
            "JavaScript and Angular");
    singlePageApplication.addTags(ViewHelper.WEB_APP);
    mobileApp = system.addContainer("Mobile App",
            "Provides a limited subset of the Internet banking functionality to customers via their mobile device.",
            "Xamarin");
    // Mobile app is coming as part of phase 2
    mobileApp.addTags(ViewHelper.MOBILE_APP, ViewHelper.PHASE_2);
    webApplication = system.addContainer("Web Application",
            "Delivers the static content and the Internet banking single page application.",
            "Java and Spring MVC");
    apiApplication = system.addContainer("API Application",
            "Provides Internet banking functionality via a JSON/HTTPS API.",
            "Java and Spring MVC");
    Container database = system.addContainer("Database",
            "Stores user registration information, hashed authentication credentials, access logs, etc.",
            "Relational Database Schema");
    database.addTags(ViewHelper.DATABASE);

    // Phase 1
    Person customer = personas.getExternalCustomer();
    customer.uses(singlePageApplication, "Uses", "HTTPS");
    webApplication.uses(singlePageApplication, "Delivers", "");
    webApplication.uses(apiApplication, "Uses API from", "HTTPS");
    apiApplication.uses(database, "Reads from and writes to", "JDBC");
    apiApplication.uses(mainframe.getMainframeApplication(), "Uses", "XML/HTTPS");
    apiApplication.uses(external.getOffice365(), "Sends e-mail using", "SMTP", InteractionStyle.Asynchronous);
    apiApplication.uses(reporting.getEventBus(), "Sends events to", "HTTPS", InteractionStyle.Asynchronous);
    external.getOffice365().uses(customer, "Sends email to", "SMTP", InteractionStyle.Asynchronous);

    // Phase 2 - mobile support
    mobileApp.uses(apiApplication, "Uses API from", "HTTPS");
    customer.uses(mobileApp, "Uses", "iOS/Android");

  }

  public Container getMobileApp() { return mobileApp; }
  public Container getWebApplication() { return webApplication; }
  public Container getApiApplication() { return apiApplication; }
  public SoftwareSystem getInternetBanking() {
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
    final SystemContextView contextView = viewSet.createSystemContextView(system, "internetbanking_sc", "Internet Banking system view");
    contextView.addNearestNeighbours(system);
    contextView.setPaperSize(PaperSize.A4_Landscape);
    contextView.setEnterpriseBoundaryVisible(true);

    final ContainerView containerView = viewSet.createContainerView(system, "internetbanking_c",
        "Internet Banking container view");
    containerView.setPaperSize(PaperSize.A4_Landscape);
    containerView.addAllContainersAndInfluencers();
    containerView.addDependentSoftwareSystems();

    final FilteredView phase1View = viewSet.createFilteredView(containerView,"phase1_internetbanking_c",
        "Internet Banking container view for phase 1", FilterMode.Exclude, ViewHelper.PHASE_2);

    // If you add a filtered view it will eliminate the main view, so adding one here that filters nothing
    // (thus, including all of phase 2)
    final FilteredView phase2View = viewSet.createFilteredView(containerView,"phase2_internetbanking_c",
        "Internet Banking container view for phase 2", FilterMode.Exclude,"");

  }

}
