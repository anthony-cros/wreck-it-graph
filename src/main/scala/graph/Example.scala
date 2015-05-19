package graph

import java.awt.Graphics2D
import java.awt.Color
/**
 * For extending Graphics2D: see call to .drawDot() that is not actually in Graphics2D below
 */
import graph.implicits.MoreGraphics2D.graphics2D2MoreGraphics2D

/**
 * Some gorgeous abstract art. Not for sale. 
 */
object Example extends App {
  new Graph(
      params = Params(width = 200, height = 300),
      action = (graph: Graphics2D, params: Params) => {
        
        // draw line across diagonal
        graph.drawLine(
            0, 0,
            params.effectiveWidth, params.effectiveHeight)
            
        // draw blue rectangle
        graph.setColor(Color.BLUE)
        graph.fillRect(100, 100, 50, 50)
        
        // draw dot
        graph.setColor(Color.RED)
        graph.drawDot(80, 80) // uses implicit conversion underneath the cover        
      },
      outputFile = "/tmp/example.png")
  
  println("done.")
}