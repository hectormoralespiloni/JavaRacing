///=============================================================================
///@file    Landscape.java
///@brief   Landscape class.
///
///@author  Héctor Morales Piloni
///@date    September 15, 2007
///=============================================================================

import java.awt.*;

class Landscape 
{
	private final int HEIGHT = 1;		//1 tile height
	private final int WIDTH = 15;		//15 tiles width, 13 to cover the screen + 2 offscreen
	private final int MAX_TILES = 6;	//6 different tiles representing the landscape
	private final int TILE_WIDTH = 50;	//tile x-size
	private final int TILE_HEIGHT = 50;	//tile y-size
	private Sprite[] m_Tiles;			//the main tileset
	private int m_Offset = 0;			//to keep track of how much we've scrolled
	private int[] m_TilePos;			//to keep track of the order to draw the tiles
	private int[] m_LandscapeMap = {	//map to build the landscape
		0,
		0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5, 0,
		0
	};

	///=========================================================================
	///Default constructor
	///=========================================================================
	Landscape() 
	{
		m_Tiles = new Sprite[MAX_TILES];
		m_TilePos = new int[WIDTH];

		//initialize tileset
		for (int i = 0, j = 1; i < MAX_TILES; ++i, ++j) 
		{
			m_Tiles[i] = new Sprite(1);
			m_Tiles[i].on();
			m_Tiles[i].addFrame(1, "images/sky" + j + ".png");
		}

		///=====================================================================
		///initialize the tileset position
		///originally it goes from 0 to WIDTH-1; later after we scroll the 
		///positions might change, so we cycle those positions to represent an
		///infinite scrolling background (e.g. after scrolling 10 tiles left
		///the array would be: [10,11,12,0,1,2,3,4,5,6,7,8,9]
		///=====================================================================
		for (int i = 0; i < WIDTH; ++i) 
		{
			m_TilePos[i] = i;
		}
	}

	///=========================================================================
	///scrolls the landscape to the left/right
	///@param int delta	amount of pixels to scroll 
	///					positive: right scroll
	///					negative: left scroll
	///=========================================================================
	public void scroll(int delta) 
	{
		m_Offset += delta;

		if (m_Offset < -TILE_WIDTH || m_Offset > TILE_WIDTH) 
		{
			//reset offset 
			m_Offset = 0;

			//cycle tiles position
			for (int i = 0; i < WIDTH; ++i) 
			{
				//get original position
				int pos = m_TilePos[i];

				//left-scroll
				if (delta < 0) 
				{
					//cycle to the left by incrementing
					if (pos++ >= WIDTH - 1) 
					{
						pos = 0;
					}
				}

				//right-scroll
				if (delta > 0) 
				{
					//cycle to the right by decrementing
					if (pos-- <= 0) 
					{
						pos = WIDTH - 1;
					}
				}

				//set new tile position
				m_TilePos[i] = pos;
			}
		}
	}

	///=========================================================================
	///draws the landscape
	///@param Graphics g	awt.Graphics object to draw into
	///=========================================================================
	public void draw(Graphics g) 
	{
		//since HEIGHT is 1, we don't care about y-coord
		for (int i = 0, x = -1; i < this.WIDTH; ++i, ++x) 
		{
			int t = m_LandscapeMap[m_TilePos[i]];

			//since x starts at -1 we're actually drawing 1 tile offscreen
			//to the left and 1 tile offscreen to the right of the screen
			m_Tiles[t].setX(x * TILE_WIDTH + m_Offset);
			m_Tiles[t].setY(PolePosition.SKY_HEIGHT - TILE_HEIGHT);
			m_Tiles[t].draw(g);
		}
	}
}
