package msgraph;

import java.io.Serializable;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named
@ViewScoped
public class MailsBean implements Serializable {
  private MailDataModel mailDataModel;

  public MailsBean() {
    mailDataModel = new MailDataModel();
  }

  public MailDataModel getMailDataModel() {
    return mailDataModel;
  }
}
