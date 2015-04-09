///=============================================================================
///@file    Road.java
///@brief   Road class.
///
///@author  Héctor Morales Piloni
///@date    September 10, 2007
///=============================================================================

import java.awt.*;

class Road 
{
	//scroll-deltas used when steering to animate the road
	final public static int STEER_LEFT = 10;
	final public static int STEER_RIGHT = -10;
	final private int ROAD_LENGTH = 200;	//length of the road in Km
	final private int BORDER_BASE = 20;		//road-border base width

	///=========================================================================
	///We define the road as a set of offsets
	///each offset represents how far from the center line is the road.
	///Positive offsets represent right-turns and negative offsets represent
	///left-turns. 
	///Later, we'll scale these offsets to draw curves; the scale will be a 
	///function of the distance (in our case y-coordinate, as y approximates z)
	///@TODO lacks physics: each element represents 10km in the circuit
	///@TODO define an array of ROAD to have as many circuits as we want!
	///=========================================================================
	final private int[] ROAD = 
	{
		 0, -1, -2, -3, -4, -5, -6, -7, -8, -9,
		-9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
		-9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
		-9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
		-9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
		-8, -7, -6, -5, -4, -3, -2, -1,  0,  1,
		 2,  3,  4,  5,  4,  3,  2,  1,  2,  3,
		 4,  5,  6,  7,  8,  9,  9,  9,  9,  9,
		 9,  9,  9,  9,  9,  9,  9,  9,  8,  7,
		 6,  5,  5,  4,  3,  2,  1,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		 1,  1,  2,  2,  2,  2,  2,  2,  3,  3,
		 0,  0,  0,  0,  0,  1,  2,  3,  4,  5,
		 5,  5,  5,  5,  5,  6,  6,  6,  6,  6,
		 5,  4,  3,  3,  3,  3,  3,  3,  3,  2,
		 1,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		-1, -2, -3, -4, -5, -6, -7, -8, -9, -9,
		-8, -7, -7, -7, -7, -7, -7, -7, -7, -7,
		-6, -5, -5, -5, -5, -4, -3, -2, -1,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	};
	
	///=========================================================================
	///The scale to represent curves must be a function of distance as we stated
	///above; so we use a basic quadratic function to compute this scale.
	///The distance is clamped to [0,1] and squared; then we multiply this 
	///by a scale constant which is computed as follows:
	/// sc = (WIDTH/2) / MAX_ROAD_OFFSET
	/// sc - Epsilon
	///where MAX_ROAD_OFFSET is the maximum number in the road array and
	///epsilon is a small amount we substract (1% of HEIGHT) in order to avoid
	///having a curve that goes to the WIDTH of the canvas
	///=========================================================================
	final public static int CONST_S = (PolePosition.APP_WIDTH / 2) / 10 - (PolePosition.ROAD_HEIGHT / 100);
	
	///=========================================================================
	///We substract a "delta" at each scan-line of the road; this is a trick
	///to make the road appear smaller with the distance instead of having to 
	///compute screen-coordinates for 3D perspective projection. 
	///
	/// np = WIDTH / ROAD_HEIGHT    This is the maximum number of pixels we
	///								can substract at each scan-line
	/// np/2						This is the number of pixels we should 
	///								substract at each side (left/right)
	/// np/2 - Epsilon				Finally, we substract a small amount in
	///								order to have a "flat" road ending (i.e.
	///								/ \  instead of /\)
	///=========================================================================
	final public static float CONST_Z = (PolePosition.APP_WIDTH / PolePosition.ROAD_HEIGHT) / 2.0f - 0.0f;

	///=========================================================================
	///HARD-CODED!
	///TODO: make a class (i.e. addBanner(), removeBanner(), etc... fix this fucking shit!)
	///=========================================================================
	//private Banner m_Banner;
	private int[] m_Scale;			//predefined array of scales per scanline
	private int m_BorderChange;		//used to animate the road
	private int m_Offset = 0;		//to keep track of how much we've scrolled
	private int m_Distance;			//player's position on the road
	private Color m_BorderColor;	//color of the road-border
	private Color m_DivisionColor;	//color of the road-division line

	///=========================================================================
	///Default constructor
	///=========================================================================
	public Road() 
	{
		m_Scale = new int[PolePosition.ROAD_HEIGHT];

		for (int i = 0; i < PolePosition.ROAD_HEIGHT; ++i) 
		{
			//clamp distance to [0,1]
			float tmp = i * (1.0f / PolePosition.ROAD_HEIGHT);

			//scale as a function of distance
			m_Scale[i] = (int) (java.lang.Math.pow(tmp, 3) * CONST_S);
		}
	}

	///=========================================================================
	///Sets the y-coord ad which the first border-color change will occur
	///@param int borderChange	y-coord for the first color change
	///=========================================================================
	public void setBorderChange(int borderChange) 
	{
		m_BorderChange = borderChange;
	}

	///=========================================================================
	///getBorderChange()
	///@return	the y-coordinate at which the first border-color change occurs
	///=========================================================================
	public int getBorderChange() 
	{
		return m_BorderChange;
	}

	///=========================================================================
	///Sets the road-border color
	///@param Color borderColor	the color of the border
	///=========================================================================
	public void setBorderColor(Color borderColor) 
	{
		m_BorderColor = borderColor;
	}

	///=========================================================================
	///getBorderColor
	///@return the road-border color
	///=========================================================================	
	public Color getBorderColor() 
	{
		return m_BorderColor;
	}

	///=========================================================================
	///Sets the road-division color
	///@param Color divisionColor the color of the border
	///=========================================================================
	public void setDivisionColor(Color divisionColor) 
	{
		m_DivisionColor = divisionColor;
	}

	///=========================================================================
	///getDivisionColor
	///@return the road-division color
	///=========================================================================	
	public Color getDivisionColor() 
	{
		return m_DivisionColor;
	}

	///=========================================================================
	///getOffset
	///@return the road-offset according to the player's position on the road
	///=========================================================================
	public int getOffset() 
	{
		return ROAD[m_Distance];
	}
	
	///=========================================================================
	///getScale
	///@param int scanline number
	///@return the road-scale of given scanline
	///=========================================================================
	public int getScale(int scanLine) 
	{
		return m_Scale[scanLine];
	}

	///=========================================================================
	///Sets the current player's position on the road
	///@param float distance	desired distance in km
	///=========================================================================
	public void setDistance(int distance) 
	{
		//check if we have completed the circuit
		if (distance >= ROAD_LENGTH) 
		{
			distance = 0;
		}

		m_Distance = distance;
	}

	///=========================================================================
	///getDistance
	///@return current player's position on the road in km
	///=========================================================================
	public int getDistance() 
	{
		return m_Distance;
	}

	///=========================================================================
	///scrolls the road in curves
	///@param int delta		amount in pixels to scroll
	///=========================================================================	
	public void setScroll(int delta) 
	{
		m_Offset += delta;

		//Do not scroll more than half-road width!
		if (m_Offset < -PolePosition.APP_WIDTH / 2) 
		{
			m_Offset = -PolePosition.APP_WIDTH / 2;
		}

		if (m_Offset > PolePosition.APP_WIDTH / 2) 
		{
			m_Offset = PolePosition.APP_WIDTH / 2;
		}
	}

	///=========================================================================
	///getScroll()
	///@return	the amount of pixels (offset) the road has been scrolled
	///=========================================================================
	public int getScroll() 
	{
		return m_Offset;
	}

	///=========================================================================
	///Draws the road
	///@param Graphics g an awt.graphics object to draw into
	///=========================================================================
	public void draw(Graphics g) 
	{
		float deltaZ = 0.0f;
		int deltaS = 0;
		int delta = ROAD[m_Distance];
		int lineStart = 0;
		int lineEnd = 0;
		int borderWidth = 0;
		int b = 0;
		boolean startCount = false;

		for (int i = PolePosition.APP_HEIGHT - 1, j = 0; i >= PolePosition.SKY_HEIGHT; --i, ++j) 
		{
			//the first borderChange will be changing from 30 down to 0 all the time
			if (j == m_BorderChange) 
			{
				startCount = true;
			}

			if (startCount) 
			{
				b++;
			}

			deltaZ += CONST_Z;
			deltaS = delta * m_Scale[j];
			lineStart = (int) deltaZ + deltaS + m_Offset;
			lineEnd = PolePosition.APP_WIDTH + deltaS + m_Offset - (int) deltaZ;

			g.setColor(PolePosition.ROAD_COLOR);
			g.drawLine(lineStart, i, lineEnd, i);

			//as soon as we get a border color change, we continue changing color every 30 scan-lines
			if (b == 30) 
			{
				b = 0;
				m_BorderColor = (m_BorderColor == Color.WHITE ? Color.RED : Color.WHITE);
				m_DivisionColor = (m_DivisionColor == Color.WHITE ? PolePosition.ROAD_COLOR : Color.WHITE);
			}

			//compute new border width as a function of distance
			borderWidth = (PolePosition.APP_WIDTH - 2 * (int) deltaZ) * BORDER_BASE / PolePosition.APP_WIDTH;
			g.setColor(m_BorderColor);
			g.drawLine(lineStart, i, lineStart + borderWidth, i);
			g.drawLine(lineEnd, i, lineEnd + borderWidth, i);

			//draw division lines
			lineStart = PolePosition.APP_WIDTH / 2 + deltaS + m_Offset - borderWidth;
			lineEnd = PolePosition.APP_WIDTH / 2 + deltaS + m_Offset + borderWidth;
			g.setColor(m_DivisionColor);
			g.drawLine(lineStart, i, lineEnd, i);
		}
	}
}

