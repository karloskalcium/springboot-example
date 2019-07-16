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
public class Reporting implements ViewProvider, ModelPostProcessor {

  private final Container eventBus;
  private final SoftwareSystem system;

  @Autowired
  public Reporting(Model model, Personas personas, MainframeBanking mainframe) {
    system = model.addSoftwareSystem(Location.Internal, "Reporting", "Reporting and data platform");

    eventBus = system.addContainer("Event bus", "Queue for events", "Kafka");
    eventBus.addTags(ViewHelper.EVENT_BUS);

    Container reportingSchema = system.addContainer("Reporting schema", "Schema used to report from",
            "Redshift");
    reportingSchema.addTags(ViewHelper.DATABASE);

    Container reportingProjector = system.addContainer("Reporting projector",
            "Translates events into reporting schema", "Java");
    reportingProjector.uses(eventBus, "Transforms events for reporting purposes", "HTTPS");
    reportingProjector.uses(reportingSchema, "Updates reporting schema", "JDBC");

    Container legacyCDC = system.addContainer("Change data capture", "Transforms legacy database changes into events",
            "Java");
    legacyCDC.uses(mainframe.getMainframeDatabase(), "Captures database changes", "JDBC",
            InteractionStyle.Asynchronous);
    legacyCDC.uses(eventBus, "Pushes legacy events onto bus", "HTTPS", InteractionStyle.Asynchronous);

    Container tableau = system.addContainer("Tableau", "Tableau web", "");
    tableau.setUrl("https://www.tableau.com/products/cloud-bi");
    tableau.addTags(ViewHelper.WEB_APP, ViewHelper.EXTERNAL);
    tableau.uses(reportingSchema, "Generate reports based on data from");

    // Human interactions
    personas.getBiAnalyst().uses(tableau, "Builds reports and analytics");
    personas.getBusinessStakeholder().uses(tableau, "views reports");
  }

  public Container getEventBus() { return eventBus; }
  public SoftwareSystem getReporting() { return system; }

  /**
   * Set relationships
   * @param model
   */
  @Override
  public void postProcess(@Nonnull Model model) {
  }

  @Override
  public void createViews(ViewSet viewSet) {

    final SystemContextView contextView = viewSet.createSystemContextView(system, "reporting_sc", "Reporting system view");
    contextView.addNearestNeighbours(system);
    contextView.setPaperSize(PaperSize.A4_Landscape);
    contextView.setEnterpriseBoundaryVisible(false);

    final ContainerView containerView = viewSet.createContainerView(system, "reporting_c", "Reporting containers view");
    containerView.setPaperSize(PaperSize.A4_Landscape);
    containerView.addAllContainersAndInfluencers();
    containerView.addDependentSoftwareSystems();
  }

}
