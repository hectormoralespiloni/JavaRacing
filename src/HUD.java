///=============================================================================
///@file    HUD.java
///@brief   Head Up Display class.
///
///@author  Héctor Morales Piloni
///@date    December 10, 2008
///=============================================================================

import java.awt.*;

public class HUD 
{
	private Sprite m_CircuitMap;
	private Sprite m_Speedometer;
	
	///=========================================================================
	///Default constructor
	///=========================================================================	
	public HUD()
	{
		m_CircuitMap = new Sprite(1);
		m_CircuitMap.addFrame(1, "images/map.png");
		m_CircuitMap.setX(10);
		m_CircuitMap.setY(10);
		
		m_Speedometer = new Sprite(1);
		m_Speedometer.addFrame(1, "images/speedometer.png");
		m_Speedometer.setX(PolePosition.APP_WIDTH - 160);
		m_Speedometer.setY(PolePosition.APP_HEIGHT - 50);
	}
	
	///=========================================================================
	///Draws the HUD
	///@param Graphics g an awt.graphics object to draw into
	///=========================================================================
	public void draw(Graphics g)
	{
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("TIME LEFT", PolePosition.APP_WIDTH/2-40, 30);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("50", PolePosition.APP_WIDTH/2-20, 70);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Nitro x 0", 10, PolePosition.APP_HEIGHT-10);
		g.drawString("100 Km/h", PolePosition.APP_WIDTH-110, PolePosition.APP_HEIGHT-10);
		m_CircuitMap.draw(g);
		m_Speedometer.draw(g);
	}
}
