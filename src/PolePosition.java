///=============================================================================
///@file    PolePosition.java
///@brief   Pole Position demo main game application.
///
///@author  Héctor Morales Piloni
///@date    September 10, 2007
///=============================================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PolePosition extends JFrame 
{
	//game application window size
	final static int APP_WIDTH = 640;
	final static int APP_HEIGHT = 480;
	final static int SKY_HEIGHT = 280;
	final static int ROAD_HEIGHT = 200;
	//game colors
	final static Color SKY_COLOR = new Color(32, 32, 224);
	final static Color GRASS_COLOR = new Color(0, 150, 0);
	final static Color ROAD_COLOR = new Color(100, 100, 100);

	public PolePosition() 
	{
		//constructor chaining
		this(null);
	}

	public PolePosition(String title) 
	{
		//create a JFrame with title
		super(title);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(0, 0, APP_WIDTH, APP_HEIGHT + 30);
		setResizable(false);
	}

	public static void main(String args[]) 
	{
		//create the animated canvas
		final Animator animator = new Animator();

		//create an instance of PolePosition
		PolePosition polePos = new PolePosition("PolePosition demo by VerMan");
		polePos.add(animator);
		polePos.setVisible(true);

		//add a KeyListener to road layer
		polePos.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				animator.onKeyPressed(e.getKeyCode());
			}
		});

		polePos.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				animator.onKeyReleased(e.getKeyCode());
			}
		});
	}
}
