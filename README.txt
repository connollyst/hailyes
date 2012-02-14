- Register self on server
	- do we already have a uuid for this user?
		- yes:
			- get old copy by uuid
			- remove from rough location map if out of date
			- update by uuid
			- add uuid to rough location map if changed
		- no:
			- assign uuid
			- add by uuid
			- add uuid to rough location map
		- thoughs...
			- when adding uuids, make sure we don't clash
			- am i a driver or a passenger?
			- store uuid on client
		
- Poll for updates periodically from client
	- v2.0: maybe adjust based on settings, battery, etc.

- Remove stale users on server side somehow
	- a timestamp of last communication with a regular check?

- Query for folks around me
	- translate location to rough location
	- lookup list of folks in rough location
		- and lookup folks in nearby rough locations
			- perhaps this should be in a followup query for performance?

Graphic Art Requirements:
- app button icon (works as banner icon too)
- splash screen
- button icons
	- <look for cab>:		Hail!
	- <look for passenger>:	?????
	- center/compass
	- settings
- drop icons
	- current user
		- signal strength
		- maybe just drawn on canvas with alpha adjusted
	- driver
		- signal strength
		- should be less visible/cluttered if I'm another driver
	- passenger
		- signal strength
			- maybe not if I'm a passenger
		- should be less visible/cluttered if I'm another passenger
- busy icon
- bad GPS icon

Purchase quane.com