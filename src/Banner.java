///=============================================================================
///@file    Banner.java
///@brief   Banners (advertisements) class.
///
///@author  Héctor Morales Piloni
///@date    September 16, 2007
///=============================================================================

import java.awt.*;

class Banner extends Sprite 
{
	final public static int WIDTH = 100;	//banner max width
	final public static int HEIGHT = 80;	//banner max height
	final public static int POS_LEFT = 0;	//banner left position
	final public static int POS_RIGHT = 1;	//banner right position
	final public static int BANNER1 = 1;
	final public static int BANNER2 = 2;
	private int m_Width;
	private int m_Height;
	private int m_Scale;
	private int m_Position;

	///=========================================================================
	///Default constructor
	///=========================================================================
	public Banner(int numFrames) 
	{
		super(numFrames);

		//set default banner size
		m_Width = Banner.WIDTH;
		m_Height = Banner.HEIGHT;
	}

	///=========================================================================
	///Sets banner scale
	///@param int scale	banner scale %
	///=========================================================================
	public void setScale(int scale) 
	{
		//sets current scale size
		m_Scale = scale;
	}

	///=========================================================================
	///getScale()
	///@return banner scale %
	///=========================================================================
	public int getScale() 
	{
		return m_Scale;
	}

	public void setScaledW(int width) 
	{
		m_Width = width;
	}
	
	public int getScaledW()
	{
		return m_Width;
	}

	public void setScaledH(int height) 
	{
		m_Height = height;
	}

	public int getScaledH()
	{
		return m_Height;
	}

	public void setPosition(int pos)
	{
		m_Position = pos;
	}
	
	public int getPosition()
	{
		return m_Position;
	}

	///=========================================================================
	///draws a banner with scaling
	///@param Graphics g	a awt.Graphics object to draw into
	///=========================================================================
	@Override
	public void draw(Graphics g) 
	{
		if(isActive())
		{
			g.drawImage(sprites[getFrame()], getX(), getY(), m_Width, m_Height, null);
		}
	}
}
