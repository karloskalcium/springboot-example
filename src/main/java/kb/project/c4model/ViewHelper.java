package kb.project.c4model;

import cc.catalysts.structurizr.ModelPostProcessor;
import cc.catalysts.structurizr.ViewProvider;
import com.structurizr.model.Enterprise;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Tags;
import com.structurizr.util.*;
import com.structurizr.view.*;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;

/** Set up view defaults */
@Component
public class ViewHelper implements ViewProvider, ModelPostProcessor {

  public static final String EXTERNAL = "external";
  public static final String INTERNAL = "internal";
  public static final String DATABASE  = "database";
  public static final String WEB_APP = "webapp";
  public static final String EVENT_BUS = "eventbus";
  public static final String MOBILE_APP = "mobileapp";
  public static final String PHASE_2 = "phase2";

  /**
   *  Set up global styling and create global views
   * @param viewSet
   */
  @Override
  public void createViews(ViewSet viewSet) {

    addBrand(viewSet);
    configureStyles(viewSet);
    createGlobalViews(viewSet);
  }

  private void addBrand(ViewSet viewSet) {

    try {
      Branding branding = viewSet.getConfiguration().getBranding();
      branding.setLogo(ImageUtils.getImageAsDataUri(new File("src/main/resources/logo.png")));
    } catch(IOException e) {
      System.err.println("Error loading logo.");
    }
  }

  private void createGlobalViews(ViewSet viewSet) {

    final SystemLandscapeView overallServicesLandscapeView = viewSet.createSystemLandscapeView("overall_landscape",
        "Overall landscape of systems");
    overallServicesLandscapeView.setPaperSize(PaperSize.A3_Landscape);
    overallServicesLandscapeView.addAllElements();
  }

  private void configureStyles(ViewSet viewSet) {
    // colors from https://visme.co/blog/wp-content/uploads/2016/09/website34.jpg
    Styles styles = viewSet.getConfiguration().getStyles();
    styles.addElementStyle(Tags.ELEMENT).background("#10E7DC").color("#ffffff").width(450).height(300);
    styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#0074E1");
    styles.addElementStyle(Tags.PERSON).background("#1B9CE5").shape(Shape.Person);
    styles.addElementStyle(DATABASE).shape(Shape.Cylinder);
    styles.addElementStyle(EVENT_BUS).shape(Shape.Pipe).width(1000);
    styles.addElementStyle(MOBILE_APP).background("#6CDAEE").shape(Shape.MobileDevicePortrait).width(300).height(450);
    styles.addElementStyle(WEB_APP).background("#6CDAEE").shape(Shape.WebBrowser);
    styles.addElementStyle(EXTERNAL).background("#F79E02");
    styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(4);
    styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);
    styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true).routing(Routing.Orthogonal);
  }

  /**
   * Tag manipulation and other global settings
   * @param model
   */
  @Override
  public void postProcess(@Nonnull Model model) {

    model.setEnterprise(new Enterprise("Big Bank Inc."));

    model.getSoftwareSystems().stream().filter(ss -> ss.getLocation() == Location.External).forEach(ss -> ss.addTags(EXTERNAL));
    model.getSoftwareSystems().stream().filter(ss -> ss.getLocation() == Location.Internal).forEach(ss -> ss.addTags(INTERNAL));

    // Set the Tags.SOFTWARE_SYSTEM tag on all software systems, which is not set by default due to a bug.
    model.getSoftwareSystems().stream().forEach(ss -> ss.addTags(ss.getTagsAsSet().stream().toArray(String[]::new)));
  }
}
