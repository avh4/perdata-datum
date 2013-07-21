Feature: Storing a list of objects

Scenario: data persistence
  Given a storage service
  And a data class definition
  When I create a list ref for the data class
  And I add some data to the list ref
  Then a new list ref from the same repository will contain the data
