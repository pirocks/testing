package ui.view.planet;

import engine.planets.Grid;
import engine.planets.Planet;
import engine.planets.TerrainType;
import ui.view.Controller;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by bob on 5/18/2016.
 *
 */
public class GridViewPanel extends JPanel implements MouseInputListener
{

	private Controller controller;
	private Grid[][] grids;

	public GridViewPanel(Planet planet, Controller controller)
	{
		grids  = planet.getGrids();
		this.controller = controller;
		addMouseListener(this);
//		setLayout(new GridLayout(grids.length,grids[0].length){{setVgap(0);setHgap(0);}});
//		for(Grid[] row:grids)
//			for(Grid grid:row)
//			{
//				add(new GridPanel(controller,grid));
//			}
		setPreferredSize(new Dimension(100*grids.length,100*grids[0].length));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = 0;
		int y = 0;
		g.setColor(Color.green);
		for(Grid[] row:grids) {
			x = 0;
			for (Grid grid : row) {
				g.setColor(getColor(grid.getTerrainType()));
				g.fillRect(100*x,100*y,100,100);
				x++;
			}
			y++;
		}
	}

	private Color getColor(TerrainType terrainType)
	{
		switch (terrainType) {
			case Land:
				return (Color.GREEN);
			case Sea:
				return (Color.BLUE);
			case Coast:
				return (Color.cyan);
			case Mountains:
				return (Color.DARK_GRAY);
			case Hills:
				return (Color.GRAY);
			case Wasteland:
				return (Color.LIGHT_GRAY);
		}
		return null;
	}

	/**
	 * Invoked when the mouse button has been clicked (pressed
	 * and released) on a component.
	 *
	 * @param e
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 *
	 * @param e
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param e
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Invoked when the mouse enters a component.
	 *
	 * @param e
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Invoked when the mouse exits a component.
	 *
	 * @param e
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button is pressed on a component and then
	 * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
	 * delivered to the component where the drag originated until the
	 * mouse button is released (regardless of whether the mouse position
	 * is within the bounds of the component).
	 * <p>
	 * Due to platform-dependent Drag&amp;Drop implementations,
	 * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
	 * Drag&amp;Drop operation.
	 *
	 * @param e
	 */
	@Override
	public void mouseDragged(MouseEvent e) {

	}

	/**
	 * Invoked when the mouse cursor has been moved onto a component
	 * but no buttons have been pushed.
	 *
	 * @param e
	 */
	@Override
	public void mouseMoved(MouseEvent e) {

	}
}