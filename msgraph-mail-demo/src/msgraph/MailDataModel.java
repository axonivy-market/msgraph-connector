package msgraph;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.microsoft.graph.MicrosoftGraphMessage;

import msgraph.wrappers.MsGraphMailRest;

public class MailDataModel extends LazyDataModel<MicrosoftGraphMessage> {

  private static final long serialVersionUID = -3136997076795549127L;

  @Override
  public String getRowKey(MicrosoftGraphMessage mail) {
    return mail.getId();
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return 0;
  }

  @Override
  public List<MicrosoftGraphMessage> load(int first, int pageSize,
      Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
    var messages = new MsGraphMailRest().getMessages(first, pageSize);
    if (messages.getCount() == null) {
      setRowCount(messages.getValue().size());
    } else {
      setRowCount(messages.getCount());
    }
    return messages.getValue();
  }

}
