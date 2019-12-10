package kb.project.c4model.structurizr.extensions;

import com.structurizr.view.ContainerView;

public class ContainerViewExtensions {

  /**
   * <p>Adds all {@link com.structurizr.model.Container}s of the given {@link ContainerView} as well as all external
   * influencers, that is all persons and all other software systems with incoming or outgoing dependencies.</p>
   *
   * This is similar to ContainerView's addAllInfluencers, but it doesn't remove external relationships
   */
  public static final void addAllInfluencersAndRelationships(ContainerView containerView) {

    // add all software systems with incoming or outgoing dependencies
    containerView.getModel().getSoftwareSystems()
        .stream()
        .filter(softwareSystem -> softwareSystem.hasEfferentRelationshipWith(containerView.getSoftwareSystem()) ||
            containerView.getSoftwareSystem().hasEfferentRelationshipWith(softwareSystem))
        .forEach(containerView::add);

    // then add all people with incoming or outgoing dependencies
    containerView.getModel().getPeople()
        .stream()
        .filter(person -> person.hasEfferentRelationshipWith(containerView.getSoftwareSystem()) ||
            containerView.getSoftwareSystem().hasEfferentRelationshipWith(person))
        .forEach(containerView::add);
  }

}
