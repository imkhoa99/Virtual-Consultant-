package psychiatrist
import scala.xml._
import scala.util.Random

object Loader {
  def loadExpressions = {
  /**Store the expression to be used to give responses to the user*/
  val xmlData = XML.loadFile("src/psychiatrist/expressions.xml")
  val xmlLibrary = XML.loadFile("src/psychiatrist/library.xml")
  
  /**Store the expression used for answering uncertain input*/ 
  Brain.uncertain = (xmlData \\ "indifferent").map(_.attributes("x")).map(_.toString)
  
  /**Store the expressions that pop up when the user gives blank input*/
  Brain.blanky = (xmlData \\ "blank").map(_.attributes("x")).map(_.toString)
  
  /**Store the expression used for answering reasonable input */
  Brain.expressions = (xmlData \\ "expression").map(_.attributes("x")).map(_.toString)
  
  /**Store the expression used for answering ambiguous input/contains a question mark. 
   * Such statement are used to encourage the user to specify more details such that the program can understand.
   */
  Brain.ambiguous = (xmlData \\ "solved").map(_.attributes("x")).map(_.toString)
  Brain.question = (xmlData \\ "solved").map(_.attributes("x")).map(_.toString)
  
  /**Store the expressions used to deal with inputs that are not invalid but is difficult to follow.
   */
  Brain.haveMoreToSay = (xmlData \\ "feeling").map(_.attributes("x")).map(_.toString)
  
  /**Store the expressions used to deal with inputs that are not invalid but more info needed. */
  Brain.moreInfo = (xmlData \\ "discourage").map(_.attributes("x")).map(_.toString)
  
  /**Store the expression used to answer when the program has found "important keywords".
   */
  Brain.keywordFound = (xmlLibrary \\ "cope").map(_.attributes("x")).map(_.toString)
  }
}
