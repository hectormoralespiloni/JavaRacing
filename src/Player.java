///=============================================================================
///@file    Player.java
///@brief   Game player class.
///@author  Héctor Morales Piloni
///@date    September 15, 2007
///=============================================================================

class Player extends Sprite 
{
	final public static int WIDTH = 180;	//sprite width
	final public static int HEIGTH = 96;	//sprite height
	
	//these constants represent the player state (2 sprites per state)
	final public static int STEER_NONE = 1;		//No steering
	final public static int STEER_NONE2 = 7;	//No steering #2
	final public static int BRAKE_NONE = 4;		//No braking
	final public static int BRAKE_LEFT = 6;		//Braking while steering left
	final public static int BRAKE_RIGHT = 5;	//Braking while steering right
	final public static int STEER_RIGHT = 2;	//Steering right
	final public static int STEER_RIGHT2 = 8;	//Steering right #2
	final public static int STEER_LEFT = 3;		//Steering left
	final public static int STEER_LEFT2 = 9;	//Steering left #2
	final public static int MAX_SPEED = 300;	//km per hour
	final public static int ACCEL = 5;			//km per second
	final public static int BRAKE = 30;			//km per second

	private int m_Steer;
	private int m_Speed;
	private boolean m_Accelerating;
	private boolean m_Braking;

	///=========================================================================
	///Default constructor
	///@param int numFrames number of frames desired
	///=========================================================================
	Player(int numFrames) 
	{
		super(numFrames);
		m_Steer = STEER_NONE;
		m_Speed = 0;
		m_Accelerating = false;
		m_Braking = false;
	}

	///=========================================================================
	///Changes steering direction
	///@param int direction steer direction
	///=========================================================================
	public void setSteerDirection(int direction) 
	{
		m_Steer = direction;
	}

	///=========================================================================
	///Changes steering direction
	///@returns steer direction
	///=========================================================================
	public int getSteerDirection() 
	{
		return m_Steer;
	}

	///=========================================================================
	///Sets accelerating flag to true to increment speed
	///=========================================================================
	public void accelerate() 
	{
		m_Accelerating = true;
	}

	///=========================================================================
	///Setd accelerating flag to false to decrement speed
	///=========================================================================
	public void deaccelerate() 
	{
		m_Accelerating = false;
	}

	///=========================================================================
	///Checks if player is accelerating (pushing the gas pedal)
	///@returns true when accelerating, false otherwise
	///=========================================================================
	public boolean isAccelerating() 
	{
		return m_Accelerating;
	}

	///=========================================================================
	///Sets player's speed
	///@param int speed desired speed in Km/h
	///=========================================================================
	public void setSpeed(int speed) 
	{
		//speed kills!
		if (speed > MAX_SPEED) 
		{
			return;
		}

		if (speed < 0) 
		{
			return;
		}

		m_Speed = speed;
	}

	///=========================================================================
	///getSpeed
	///@returns the current player's speed
	///=========================================================================
	public int getSpeed() 
	{
		return m_Speed;
	}

	///=========================================================================
	///Sets the braking flag to true to stop the car
	///@param boolean status	true/false uses or not the breaks
	///=========================================================================
	public void brake(boolean status) 
	{
		m_Braking = status;
	}

	///=========================================================================
	///Checks if player is braking
	///@returns true if player is braking, flase otherwise
	///=========================================================================
	public boolean isBraking() 
	{
		return m_Braking;
	}
}
