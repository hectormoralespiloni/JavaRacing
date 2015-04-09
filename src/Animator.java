///=============================================================================
///@file    Animator.java
///@brief   Animated canvas class.
///
///@author  Héctor Morales Piloni
///@date    September 10, 2007
///=============================================================================

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;

class Animator extends Canvas implements Runnable 
{
	private Thread m_Animator;
	private boolean m_Playing;
	private Player m_Player;
	private Landscape m_Landscape;
	private Road m_Road;
	private Banner m_Banner;
	private HUD m_HUD;
	private Color m_BorderStart;	//used to animate the road
	private Color m_DivisionStart;	//used to animate the road
	private int roadYPos;

	///=========================================================================
	///Default constructor
	///=========================================================================
	public Animator() 
	{
		m_Animator = null;
		m_Playing = false;

		start();
	}

	///=========================================================================
	///Starts the animation & current thread
	///=========================================================================
	private void start() 
	{
		if (m_Animator == null && !m_Playing) 
		{
			m_Animator = new Thread(this);
			m_Animator.start();
			m_Playing = true;
		}
	}

	///=========================================================================
	///Stops the animation
	///=========================================================================
	private void stop() 
	{
		m_Playing = false;
	}

	///=========================================================================
	///Initializes the canvas (bg, road, etc)
	///=========================================================================
	private void init() 
	{
		//setup player
		m_Player = new Player(9);
		m_Player.addFrame(1, "images/car1.png");
		m_Player.addFrame(2, "images/car2.png");
		m_Player.addFrame(3, "images/car3.png");
		m_Player.addFrame(4, "images/car4.png");
		m_Player.addFrame(5, "images/car5.png");
		m_Player.addFrame(6, "images/car6.png");
		m_Player.addFrame(7, "images/car7.png");
		m_Player.addFrame(8, "images/car8.png");
		m_Player.addFrame(9, "images/car9.png");
		m_Player.setX(PolePosition.APP_WIDTH / 2 - Player.WIDTH / 2);
		m_Player.setY(PolePosition.APP_HEIGHT - Player.HEIGTH - 10);
		m_Player.on();

		//setup landscape
		m_Landscape = new Landscape();

		//setup road
		m_Road = new Road();
		m_BorderStart = Color.WHITE;
		m_DivisionStart = Color.WHITE;
		m_Road.setBorderChange(30);
		m_Road.setBorderColor(m_BorderStart);
		m_Road.setDivisionColor(m_DivisionStart);
		//m_Road.initBanners();
		
		//setup banner
		m_Banner = new Banner(2);
		m_Banner.addFrame(1, "images/banner1.png");
		m_Banner.addFrame(2, "images/banner2.png");
		m_Banner.setScale(0);
		m_Banner.off();
		
		//setup HUD
		m_HUD = new HUD();
	}

	///=========================================================================
	///Animates the landscape layer
	///=========================================================================
	private void updateLandscape() 
	{
		//scroll background only when player is accelerating 
		if (!m_Player.isAccelerating()) 
		{
			return;
		}

		int xPos = m_Road.getOffset();
		m_Landscape.scroll(-xPos);
	}

	///=========================================================================
	///To animate the road we could cycle the color palette or use a simple
	///trick changing the start (y-coord) at which the road-border changes
	///its color and achieve a similar effect. Subsequently we change the color
	///every 30 scan lines, for instance, but the start will be pushed down
	///=========================================================================
	private void updateRoad() 
	{
		int xPos, y, delta;

		y = m_Road.getBorderChange();

		//update border change & scroll (i.e. animate) only when player is accelerating
		if (m_Player.getSpeed() > 1) 
		{
			//animation speed is a function of player's speed
			y -= m_Player.getSpeed() / 10;

			//the road is automatically scrolled along with curves in the
			//same direction (again, as a function of player's speed)
			//we first clamp the speed to [0,2], multiply by -1 and 
			//finally add a dela (e.g. 3) to get clamped values of [3, 1]
			//that is, on MAX speed we divide by 1 (i.e. max scroll); on
			//MIN speed we divide by 3 (i.e. small scroll)
			delta = m_Player.getSpeed() / (Player.MAX_SPEED / 2);
			delta *= -1;
			delta += 3;
			xPos = m_Road.getOffset();
			m_Road.setScroll(xPos / delta);
		}

		if (y < 0) 
		{
			m_BorderStart = (m_Road.getBorderColor() == Color.WHITE) ? Color.RED : Color.WHITE;
			m_DivisionStart = (m_Road.getDivisionColor() == Color.WHITE) ? PolePosition.ROAD_COLOR : Color.WHITE;
			y = 30;
		}

		m_Road.setBorderColor(m_BorderStart);
		m_Road.setDivisionColor(m_DivisionStart);
		m_Road.setBorderChange(y);

		//update distance as a function of player's speed
		delta = m_Player.getSpeed() / Player.MAX_SPEED;
		m_Road.setDistance(m_Road.getDistance() + delta);
	}

	///=========================================================================
	///Animates the player's car
	///=========================================================================
	private void updatePlayer() 
	{
		//update speed
		if (m_Player.isAccelerating()) 
		{
			m_Player.setSpeed(m_Player.getSpeed() + Player.ACCEL);
		} 
		else 
		{
			m_Player.setSpeed(m_Player.getSpeed() - Player.ACCEL);
		}

		if (m_Player.isBraking()) 
		{
			m_Player.setSpeed(m_Player.getSpeed() - Player.BRAKE);
		}

		//driving on grass is slower than on asphalt
		int pos = m_Road.getScroll();
		int half_road = PolePosition.APP_WIDTH / 2;
		int half_player = Player.WIDTH / 2;
		if (pos < (-half_road + half_player) || pos > (half_road - half_player)) 
		{
			m_Player.setSpeed(m_Player.getSpeed() - 10);
		}

		//check if player has stopped
		if (m_Player.getSpeed() < 1) 
		{
			if (m_Player.isBraking()) 
			{
				m_Player.selFrame(Player.BRAKE_NONE);
			} 
			else 
			{
				m_Player.selFrame(Player.STEER_NONE);
			}
			return;
		}

		//change steer direction
		int steerDirection = m_Player.getSteerDirection();
		int frame = m_Player.getFrame();
		switch (steerDirection) 
		{
			//=========================
			//STEER LEFT
			//=========================
			case Player.STEER_LEFT:
				if (m_Player.isBraking()) 
				{
					m_Player.selFrame(Player.BRAKE_LEFT);
				} 
				else 
				{
					//animate the tires
					if (frame == Player.STEER_LEFT) 
					{
						m_Player.selFrame(Player.STEER_LEFT2);
					} 
					else 
					{
						m_Player.selFrame(Player.STEER_LEFT);
					}
				}
				m_Road.setScroll(Road.STEER_LEFT);
				break;

			//=========================
			//STEER RIGHT
			//=========================
			case Player.STEER_RIGHT:
				if (m_Player.isBraking()) 
				{
					m_Player.selFrame(Player.BRAKE_RIGHT);
				} 
				else 
				{
					//animate the tires
					if (frame == Player.STEER_RIGHT) 
					{
						m_Player.selFrame(Player.STEER_RIGHT2);
					} 
					else 
					{
						m_Player.selFrame(Player.STEER_RIGHT);
					}
				}
				m_Road.setScroll(Road.STEER_RIGHT);
				break;

			//=========================
			//STEER NONE
			//=========================
			case Player.STEER_NONE:
				if (m_Player.isBraking()) 
				{
					m_Player.selFrame(Player.BRAKE_NONE);
				} 
				else 
				{
					//animate the tires
					if (m_Player.getFrame() == Player.STEER_NONE) 
					{
						m_Player.selFrame(Player.STEER_NONE2);
					} 
					else 
					{
						m_Player.selFrame(Player.STEER_NONE);
					}
				}
				break;
		}
	}
	
	private void updateBanner()
	{		
		if(!m_Banner.isActive())
		{
			//draw a banner every 100Km
			if ( (m_Road.getDistance() >= 50 && m_Road.getDistance() <= 60) ||
				 (m_Road.getDistance() >= 150 && m_Road.getDistance() <= 160) )
			{
				Random r = new Random();
				int position = r.nextInt(2);
				m_Banner.on();
				m_Banner.selFrame(r.nextInt(2)+1);
				m_Banner.setPosition(position);
				roadYPos = 0;
			}
		}

		//animate only when banner is active
		if (m_Banner.isActive() && m_Player.getSpeed()>1) 
		{
			//update banner scale according to y-position
			m_Banner.setScale(m_Banner.getScale() + 2);
			m_Banner.setScaledW(m_Banner.getW() * m_Banner.getScale() / 100);
			m_Banner.setScaledH(m_Banner.getH() * m_Banner.getScale() / 100);

			//get new banner x-position
			int invertYPos = PolePosition.ROAD_HEIGHT - roadYPos;
			float deltaZ = (invertYPos) * Road.CONST_Z;
			int deltaS = m_Road.getOffset() * m_Road.getScale(invertYPos-1);
			if(m_Banner.getPosition() == Banner.POS_LEFT)
			{
				m_Banner.setX(deltaS + m_Road.getScroll() + (int)deltaZ - m_Banner.getScaledW());
			}
			else
			{
				m_Banner.setX(PolePosition.APP_WIDTH + deltaS + m_Road.getScroll() - (int)deltaZ);
			}

			//update banner y-position
			int yTemp = PolePosition.SKY_HEIGHT + roadYPos;
			if(m_Banner.getScale() > 150)
			{
				m_Banner.setY(yTemp + 10 - m_Banner.getScaledH());
				roadYPos+=10;
			}
			else
			{
				m_Banner.setY(yTemp + 1 - m_Banner.getScaledH());
				roadYPos+=1;
			}
			
			if(roadYPos >= PolePosition.ROAD_HEIGHT)
			{
				m_Banner.off();
				m_Banner.setScale(0);
			}
		}
	}

	///=========================================================================
	///Implements method run() of interface Runnable
	///=========================================================================
	public void run() 
	{
		init();

		while (m_Playing) 
		{
			//animate landscape
			updateLandscape();

			//animate banner
			updateBanner();
			
			//animate road
			updateRoad();

			//animate player's car
			updatePlayer();

			//update screen
			repaint();

			//sleep for a little while
			try 
			{
				Thread.sleep(40);
			} 
			catch (InterruptedException ex) 
			{
				System.out.println(ex.toString());
			}
		}
	}

	///=========================================================================
	///Handles keyboard events within the canvas
	///@param int key	the key that was pressed
	///=========================================================================
	public void onKeyPressed(int key) 
	{
		switch (key) 
		{
			case KeyEvent.VK_LEFT:
				m_Player.setSteerDirection(Player.STEER_LEFT);
				break;

			case KeyEvent.VK_RIGHT:
				m_Player.setSteerDirection(Player.STEER_RIGHT);
				break;

			case KeyEvent.VK_A:
				m_Player.accelerate();
				break;

			case KeyEvent.VK_Z:
				m_Player.brake(true);
				break;
		}
	}

	///=========================================================================
	///Handles keyboard events within the canvas
	///@param int key	the key that was released
	///=========================================================================
	public void onKeyReleased(int key) 
	{
		switch (key) 
		{
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
				m_Player.setSteerDirection(Player.STEER_NONE);
				break;

			case KeyEvent.VK_A:
				m_Player.deaccelerate();
				break;

			case KeyEvent.VK_Z:
				m_Player.brake(false);
				break;
		}
	}

	///=========================================================================
	///Overrides method paint() of class Canvas.
	///=========================================================================
	@Override
	public void paint(Graphics g) 
	{
		//create a back buffer (off-screen)
		BufferedImage backbuffer = new BufferedImage(PolePosition.APP_WIDTH, PolePosition.APP_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) backbuffer.getGraphics();

		//clear the back buffer
		g2.setColor(PolePosition.SKY_COLOR);
		g2.fillRect(0, 0, PolePosition.APP_WIDTH, PolePosition.SKY_HEIGHT);
		g2.setColor(PolePosition.GRASS_COLOR);
		g2.fillRect(0, PolePosition.SKY_HEIGHT, PolePosition.APP_WIDTH, PolePosition.ROAD_HEIGHT);

		//draw the landscape
		m_Landscape.draw(g2);

		//draw the road
		m_Road.draw(g2);
		
		//draw the banner
		m_Banner.draw(g2);

		//draw the player's car
		m_Player.draw(g2);
		
		//draw the HUD
		m_HUD.draw(g2);

		if (!m_Playing) 
		{
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Arial", Font.BOLD, 20));
			g2.drawString("GAME OVER", 320 - 80, 240 - 10);
		}

		//draw the back buffer
		g.drawImage(backbuffer, 0, 0, this);
	}

	///=========================================================================
	///Overrides update method of class Canvas
	///because the default implementation always calls clearRect() 
	///causing an unwanted flicker.
	///=========================================================================
	@Override
	public void update(Graphics g) 
	{
		paint(g);
	}
}
