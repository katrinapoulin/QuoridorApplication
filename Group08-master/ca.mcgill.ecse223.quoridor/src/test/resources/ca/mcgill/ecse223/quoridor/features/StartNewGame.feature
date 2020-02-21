Feature: Start New Game
  As a Quoridor player, I want to start a new game of Quoridor against some opponent. 

  Background: 
    Given The game is not running

  Scenario: Initiate a new game
    When A new game is being initialized
    And White player chooses a username1
    And Black player chooses a username1
    And Total thinking time is set
    Then The game shall become ready to start

  Scenario: Start clock 
  	Given The game is ready to start1
  	When I start the clock
  	Then The game shall be running
  	And The board shall be initialized
