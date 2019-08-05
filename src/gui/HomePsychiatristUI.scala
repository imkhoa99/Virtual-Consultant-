package gui
import scala.io.StdIn._
import scala.io
import java.io.Reader
import java.io.Writer
import scala.io.Source
import psychiatrist._
import scala.util.Random
import scala.xml._
import java.io.File

object HomePsychiatristUI extends App {
  /**Load the data used to answer the user input from external source.
   * The source can be modifiable. 
   */
  Loader.loadExpressions
  
  /**Load the goodbye phases. 
   * 
   */
  val goodbye = new File("src/psychiatrist/Signaling.xml")
  val x = XML.loadFile(goodbye)
  val closing = {
    (x \\ "phrases").map(_.attributes("x").toString)
  }
  
  /**Run the program. First, a welcome message is printed, then the player gets to 
   * elaborate on their feeling*/
  val file = new File("src/psychiatrist/expressions.xml")
  val expression = XML.loadFile(file)
  val opening = {
    (expression \\ "start").map(_.attributes("x").toString)
  }
  "Psychiatrist" + println(opening(Random.nextInt(opening.length)))
  
  Brain.getInput(readLine(Brain.welcome))
  
  /**The AI repeatedly requests the user for input. 
   * While analyzing the input, the program pops up a well-known advice to help the user feel inspirational. 
   * The program analyzes the input, then gives advice based on the situation the user is having.
   */
  while (!Brain.isOver) {
    val load = new File("src/psychiatrist/advice.xml")
    val loadAdvice = XML.loadFile(load)
    val loading = (loadAdvice \\ "advice").map(_.attributes("x").toString)
    println(loading(Random.nextInt(loading.length))).toString
    Brain.getInput(readLine(Brain.analyze))
  }

  /**The session ends when the user says 'quit'*/
  println(closing(Random.nextInt(closing.length)))
}