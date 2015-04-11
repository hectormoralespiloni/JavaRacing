Java PolePosition (September 2007)
----------------------------------

![](https://github.com/hectormoralespiloni/JavaRacing/blob/master/racing_full.jpg)

1. SUMMARY 
	This is a demo of the oldie classic Pole Position (with Nintendo Top Racer
	sprites).

2. REQUIREMENTS TO RUN THE JAR
	* Java 2 (1.5+) Runtime Environment

3. HOW TO PLAY THE DEMO
	* The executable file (.jar) is located in the "dist" folder
	* Arrows to steer left/right
	* "A" to accelerate
	* "Z" to brake
	
4. HOW TO COMPILE
	* The easiest way to go is download the Netbeans IDE from: netbeans.org
	There's already an nbproject folder for netbeans; 
	you just have to select the PolePosition folder in netbeans to open it.

5. CODE STURCTURE
	* The images folder contains all the png with transparency used in the game.
	* There are 7 classes:
	    * Animator: 	is a Canvas implementing Runnable where we render our objects
			and perform some game logic (update road, landscape, key actions etc)
	    * Banner:	this class inherits from Sprite, it holds the advertisements seen 
			during the gameplay. Hardcoded in the last minute because of the rush =(
	    * Landscape:	this class contains the logic to draw and scroll the background
	    * Player: 	this class inherits from sprite and it just adds the set/get
			state functionality (accelerating, braking, steering, etc)
	    * PolePosition:	this is the main application class. It has a threaded canvas (runnable)
			which acts as the main game loop.
	    * Road:		this class draws and animates the road; as a last minute change it also 
			initializes, renders and processes (logic) the banners.
	    * Sprite: 	this class manages the basic sprite stuff such as get and set
			its position, collision detection and drawing the sprite.