## Setup

### Variables

In order to use this product you must configure multiple variables.

Add the following block to your `config/variables.yaml` file of our 
main Business Project that will make use of this product:

```
@variables.yaml@ 
```

Afterwards set the values as shown in the Azure App setup below.

> [!NOTE]
> The format of variable is changed from version 13.
> E.g. 
> The variable path `microsoft-connector` is renamed to `microsoftConnector`.
> The variable path `teams-notification` is renamed to `teamsNotification`.

### Azure App

1. Register an application on Azure as described in the Microsoft's Java Tutorial https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
1. Navigate to `Overview` in the menu and copy the 'Application (client) ID' into your variable called `appId` within the `microsoftConnector` section.
1. Navigate to `Authentication` in the Azure App menu.
	1. Add a `Redirect URI` in the `Web` Section.
		- Axon Ivy has an authentication callback URI which follows the pattern `{scheme}://{host}:{port}/oauth2/callback`. This URI must be registered in the Azure App.
		- Consequently, for the Axon Ivy Designer this URI can always be set to the following value: `http://localhost:8081/oauth2/callback`
		- For the Axon Ivy Engine, the URI must contain the full URI where your Engine is reachable plus the callback path. E.g.: `https://my.workflows.ch/oauth2/callback`
 ![set-redirect](doc/img/azure_authCallback.png)

1. Navigate to `Certificate & secrets` in the Azure App menu.
  1. Create a new secret by pressing `New client secret`. And select any validity period.
  ![new-secret](doc/img/azure_createSecret.png)
  1. copy the value of the generated secret into your variable called `secretKey` within the `microsoftConnector` section.
  ![copy-secret](doc/img/azure_copySecret.png)

1. Navigate to `API Permissions` in the Azure App menu.
Add permissions via `Add a permission` > `Microsoft Graph` > `Delegated permissions`.
Grant each of the permissions outlined in the `permissions` block of your variables.yaml file.
    ![add-perms](doc/img/azure_addPermission.png)

1. Done. Start any process that connects with Microsoft 365.
