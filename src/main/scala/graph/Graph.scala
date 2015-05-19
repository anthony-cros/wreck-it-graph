package graph

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.Color
import javax.imageio.ImageIO
import java.io.File

case class Params(
    width: Int = 100,
    height: Int = 100,
    widthMultiplier: Double = 1.0,
    heightMultiplier: Double = 1.0) {
  def effectiveWidth = (width * widthMultiplier).toInt
  def effectiveHeight = (height * heightMultiplier).toInt
}

/**
 * See Example.scala for usage.
 */
class Graph(
    params: Params,
    action: (Graphics2D, Params) => Unit,
    outputFile: String) {

  init()
  
  def init() = {
    
    // prepare graph
    val bufferedImage = new BufferedImage(
      (params.width * params.widthMultiplier).toInt,
      (params.height * params.heightMultiplier).toInt, 
      BufferedImage.TYPE_INT_RGB);
    val graph: Graphics2D = bufferedImage.createGraphics();
    
    // clear background by drawing a white rectangle on the whole surface
    graph.setBackground(Color.WHITE);
    graph.clearRect(
        0, 0,
        bufferedImage.getWidth, bufferedImage.getHeight);
    
    // default initial drawing color to black 
    graph.setColor(Color.BLACK);
    
    // run UDP
    action(graph, params)
    
    // finish things up
    graph.dispose();
    ImageIO.write(
        ( // else images are upside down (coordinates are from the top-left corner by default, instead of bottom-left)
        verticalflip(bufferedImage)),
        "png",
        new File(outputFile));
  }
  
  def verticalflip(bufferedImage: BufferedImage) = {  
      val width = bufferedImage.getWidth();  
      val height = bufferedImage.getHeight();
      
      // clone image
      val flipped: BufferedImage = new BufferedImage(
          width, height,
          bufferedImage.getType());  
      val graph = flipped.createGraphics();
      
      // actual flip operation
      graph.drawImage(
          bufferedImage,
          width, height, 0, 0,
          width, 0, 0, height,
          null);
      graph.dispose();
      
      flipped;  
  }
}
