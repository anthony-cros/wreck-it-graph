package graph.implicits

import java.awt.Graphics2D
import java.awt.Color

class MoreGraphics2D(graph: Graphics2D) {  
  
  def drawDot(x: Int, y: Int) {
    graph.fillRect(x, y, 1, 1);
  }
  def drawDot3(x: Int, y: Int) {
    graph.fillRect(x-1, y-1, 3, 3);
  }
  def drawDot5(x: Int, y: Int) {
    graph.fillRect(x-2, y-2, 5, 5);
  }
  
}

object MoreGraphics2D{
  
  /**
   * 150518190955
   */
	implicit def graphics2D2MoreGraphics2D(graph: Graphics2D) = {
		new MoreGraphics2D(graph)
	}
  
}