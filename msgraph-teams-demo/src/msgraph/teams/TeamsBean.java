package msgraph.teams;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

import com.microsoft.graph.MicrosoftGraphChat;
import com.microsoft.graph.MicrosoftGraphChatType;

@Named
@ViewScoped
public class TeamsBean implements Serializable {

  private final TeamsMessagesDataModel model;
  private List<MicrosoftGraphChat> chats;

  public TeamsBean() {
    model = new TeamsMessagesDataModel();
    Optional.ofNullable(getChats()).map(all -> all.get(0))
      .map(MicrosoftGraphChat::getId)
      .ifPresent(model::setChatId);
  }

  public TeamsMessagesDataModel getTableModel() {
    return model;
  }

  public List<MicrosoftGraphChat> getChats() {
    if (chats == null || chats.isEmpty()) {
      chats = loadChats();
    }
    return chats;
  }

  private List<MicrosoftGraphChat> loadChats() {
    return new MsGraphTeams().getMessages().getValue()
       .stream().filter(chat -> chat.getChatType() != MicrosoftGraphChatType.ONEONONE)
       .toList();
  }

}
