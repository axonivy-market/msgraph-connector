package msgraph.teams.notification;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.process.eventstart.AbstractProcessStartEventBean;
import ch.ivyteam.ivy.service.ServiceException;
import ch.ivyteam.ivy.workflow.IWorkflowManager;

public class TeamNotificationStart extends AbstractProcessStartEventBean {

  private TeamsNotifier listener;

  public TeamNotificationStart() {
    super("TeamNotificationStart",
      "Installs the hook to inform on new Axon Ivy Tasks via Microsoft Teams");
  }

  @Override
  public void poll() {
    getEventBeanRuntime().poll().disable(); // no poll; we only use start/stop hooks
  }

  @Override
  public void start() throws ServiceException {
    super.start();
    IWorkflowManager wfManager = IWorkflowManager.instance();
    this.listener = new TeamsNotifier();
    wfManager.addWorkflowListener(listener);
    Ivy.log().info("teamsNotification installed");
  }

  @Override
  public void stop() throws ServiceException {
    if (this.listener != null) {
      IWorkflowManager.instance().removeWorkflowListener(listener);
      Ivy.log().info("teamsNotification stopped");
    }
    super.stop();
  }

}
