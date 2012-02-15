==============
Outline
==============

com.quane.hail.yes.android.app
	MainActivity - The main activity, see cabs, people, and hail for a cab
	ConfigureActivity - An activity for configuring the app
	
com.quane.hail.yes.android.app.ui
	MainController - Updates the MainActivity UI as necessary, handles business logic behind user interactions
	ConfigureController - Updates the ConfigureActivity UI as necessary, handles business logic behind user interactions

com.quane.hail.yes.android.app.ui.overlay
	DriverOverlay - Overlays 0 or many driver icons over the Google map. 
	RiderOverlay - Overlays 0 or many rider icons over the Google map.
	MeOverlay - Overlays exactly one icon over the Google map, where the current user is.
	
com.quane.hail.yes.android.app.service
	ServerCommunicator - Solely responsible for all communication with the server
	AppLocationListener - Listens for GPS updates from the phone, including enabling/disabling
	Timer - Queries the server for updates on a schedule to keep the local data up to date

==============
TODO
==============
May be able to improve performance by not invalidating the overlay all the time.  