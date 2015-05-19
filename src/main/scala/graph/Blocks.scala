package graph

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import scala.io.Source
import javax.imageio.ImageIO
import graph.implicits.MoreGraphics2D.graphics2D2MoreGraphics2D

object Blocks {

  val inputFile = "/tmp/blocks.tsv" // generated via external program (format: id \t x \t y)

  new Graph(
    params = Params(
        width = 80000, height = 300,
        widthMultiplier = 0.03, heightMultiplier = 4.0),
    action = (graph: Graphics2D, params: Params) => {
      
      // read up input data
      val source = Source.fromFile(inputFile) // TODO: add sort
      val lines = source.getLines()
      
      // convenience case class
      case class Coordinates(id: String = "N/A", x: Double, y: Double)        
      
      val INIT = Coordinates(x = 0.0, y = 0.0)
      var previous = null.asInstanceOf[Coordinates] // to draw lines from previous to current
      var first = true // we'll need to skip the first line as a result (no previous)
      for (line <- lines) {
        
        // get coordinates
        val current = {
          val Array(id, x, y) = line split "\t"
          Coordinates(id.replaceAll(">", ""), x.toDouble, y.toDouble)
        }
        val Coordinates(id, x, y) = current
        
        if (first) { // can't draw a line yet
          first = false            
        } else { // draw line
      		  previous = if (id != previous.id) {
            
            graph.drawDot5( // implicit conversion
                (previous.x*params.widthMultiplier).toInt,
                (previous.y*params.heightMultiplier).toInt)
            
            INIT
          } else { // reset if first ID of a series
          	previous
          }
          
          // set corresponding line color (based on ID)
          graph.setColor(id match {
            case "best" => Color.BLACK
            case "gpu1em" => Color.MAGENTA
            case "gpu2em" => Color.YELLOW
            
            case "fk1" => Color.GRAY
            case "fk2" => Color.CYAN
            case "or1" => Color.RED
            case "ir1" => Color.YELLOW
            case "si1" => Color.GREEN
            case "si2" => Color.ORANGE
            
            case r"[a-z][a-z][0-9]" => Color.BLUE
            
            case r"^cpu.+" => Color.GREEN
            case r"^c48xl.+" => Color.DARK_GRAY
            case r"^r34xl.+" => Color.PINK
            
            case _ => Color.BLACK
          })
          
          graph.drawLine(
            (previous.x*params.widthMultiplier).toInt,
            (previous.y*params.heightMultiplier).toInt,
            (x*params.widthMultiplier).toInt,
            (y*params.heightMultiplier).toInt.toInt)   
        }
        
        previous = current // get ready for next loop
      }       
      
      source.close()        
    },
    outputFile = "/tmp/blocks.png")

  println("done.")
    
    
  /**
   * Enable pattern matching on regexes (usage: case r"\d+" => ...).
   * See http://stackoverflow.com/questions/4636610/regular-expression-and-pattern-matching-in-scala.
   */
  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

}
