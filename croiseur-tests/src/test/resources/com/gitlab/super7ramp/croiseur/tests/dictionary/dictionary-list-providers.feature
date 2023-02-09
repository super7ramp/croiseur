Feature: List Dictionary Providers

  Word lists - or "dictionaries" in croiseur jargon - used for crossword generation are provided
  via modules called "dictionary providers". The application offers a way to list the installed
  dictionary providers.

  Scenario: List Dictionary Providers - Default deployment

  This scenario tests the output of the application with the default deployment where all
  available providers are included.

    When user requests to list the available dictionary providers
    Then the application presents the following dictionary providers:
      | Provider                | Description                                                    |
      | Local Hunspell Provider | Provides access to local dictionaries in the Hunspell format.  |
      | Local Text Provider     | Provides access to local dictionaries in a simple text format. |
      | Local XML Provider      | Provides access to local dictionaries in an XML format.        |

  @no-auto-deploy
  Scenario: List Dictionary Providers - No provider

  This scenario tests the output of the application when no dictionary provider is installed.

    Given an application deployed without dictionary provider
    When user requests to list the available dictionary providers
    Then the application presents the dictionary error "No dictionary found"
