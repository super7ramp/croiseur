Feature: List Dictionaries

  Word lists - or "dictionaries" in croiseur jargon - used for crossword generation are provided
  via modules called "dictionary providers". The application offers a way to list the
  dictionaries provided by these providers.

  # TODO Provider description is useless in this feature, it'd better to have dictionary description instead

  Scenario: List Dictionaries - No filter

    When user requests to list the available dictionaries
    Then the application presents the following dictionaries:
      | Provider            | Provider Description                                           | Name                                | Locale |
      | Local XML Provider  | Provides access to local dictionaries in an XML format.        | General German dictionary           | de-DE  |
      | Local Text Provider | Provides access to local dictionaries in a simple text format. | The UK Advanced Cryptics Dictionary | en-GB  |
      | Local XML Provider  | Provides access to local dictionaries in an XML format.        | General British English dictionary  | en-GB  |
      | Local XML Provider  | Provides access to local dictionaries in an XML format.        | General Spanish dictionary          | es-ES  |
      | Local XML Provider  | Provides access to local dictionaries in an XML format.        | General French dictionary           | fr-FR  |
      | Local XML Provider  | Provides access to local dictionaries in an XML format.        | General Italian Dictionary          | it-IT  |

  Scenario: List Dictionaries - Filter by Locale
    When user requests to list the available dictionaries of locale en-GB
    Then the application presents the following dictionaries:
      | Provider            | Provider Description                                           | Name                                | Locale |
      | Local Text Provider | Provides access to local dictionaries in a simple text format. | The UK Advanced Cryptics Dictionary | en-GB  |
      | Local XML Provider  | Provides access to local dictionaries in an XML format.        | General British English dictionary  | en-GB  |

  Scenario: List Dictionaries - Filter by Provider
    When user requests to list the available dictionaries provided by Local Text Provider
    Then the application presents the following dictionaries:
      | Provider            | Provider Description                                           | Name                                | Locale |
      | Local Text Provider | Provides access to local dictionaries in a simple text format. | The UK Advanced Cryptics Dictionary | en-GB  |

  Scenario: List Dictionaries - Filter by Locale and Provider
    When user requests to list the available dictionaries of locale en-GB provided by Local XML Provider
    Then the application presents the following dictionaries:
      | Provider           | Provider Description                                    | Name                               | Locale |
      | Local XML Provider | Provides access to local dictionaries in an XML format. | General British English dictionary | en-GB  |

  @no-auto-deploy
  Scenario: List Dictionaries - No Dictionary Provider

  This scenario tests the output of the application when no dictionary provider is installed.

    Given an application deployed without dictionary provider
    When user requests to list the available dictionaries
    Then the application presents the dictionary error "No dictionary found"
