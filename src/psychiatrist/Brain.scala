package psychiatrist
import scala.io.StdIn._
import scala.io
import java.io.Reader
import scala.io.Source
import psychiatrist._
import scala.util.Random
import scala.xml._
import java.io.File

object Brain {
  
  /**Store the input gained from the user*/
  private var input = ""
  
  /**Store the expression used to answer when the program has found "important keywords".
   */
  var keywordFound = Seq[String]()
  def updateSentence(keyword: Seq[String]) = {
    keywordFound = keyword
  } 
  
  /**Store the expressions used to answer reasonable input*/
  var expressions = Seq[String]()
  def updateExpressions(expression: Seq[String]) = {
    expressions = expression
  }
  
  /**Store the expressions used to deal with input that contains a question mark.
   * The program needs to understand more about the user's problem.
   * Therefore, the session encourages the user for more information.
   */
  var question = Seq[String]()
  def updateQuestion(questions: Seq[String]) = {
    question = questions
  }
  
  /**Store the expressions used to deal with input that seems to be ambiguous. 
   * The program encourages the user to give more details of the input such that it can understand. 
   */
  var ambiguous = Seq[String]()
  def updateAmbiguity(ambiguity: Seq[String]) = {
    ambiguous = ambiguity
  }
  
  /**Store the expressions used to deal with inputs that are not invalid but is difficult to follow.
   */
  var haveMoreToSay = Seq[String]()
  def encouragement(encouraging: Seq[String]) = {
    haveMoreToSay = encouraging
  }
  
   /**Store the expressions used to deal with inputs that are not invalid but more info needed. */
  var moreInfo = Seq[String]()
  def talkMore(information: Seq[String]) = {
    moreInfo = information
  }
  
  /**Store the expressions used to deal with inputs that is uncertain*/
  var uncertain = Seq[String]()
  def uncertainty(uncertainties: Seq[String]) = {
    uncertain = uncertainties 
  } 
  
  /**Store the expressions that pop up when the user gives blank input*/
  var blanky = Seq[String]()
  def blank(blanks: Seq[String]) = {
    blanky = blanks
  }
  
  /**Get input from the user*/
  def getInput(command: String) = {
    input = command.trim.toLowerCase()
  }
  
  /**Decide when the session is over. 
   * The session is over when the user says "Quit" to the program */
   def isOver: Boolean = {
    if (input.contains("good bye"))
      true 
    else 
      false
  }
  
  def welcome = {
    val commence = new File("src/psychiatrist/expressions.xml")
    val file = XML.loadFile(commence)
    val commencing = {
      (file \\ "commence").map(_.attributes("x"))
    }
    commencing(Random.nextInt(commencing.length)).mkString(" ")
  }
  
  def analyze = {
    /**Parse the input received from the user into smaller components for analysis.
     */
      def changePronoun(word: String) = Brain2.changeSubject
                                            .find( _._2.contains(word) )
                                            .getOrElse((word, ""))._1
     
     /**Define the feeling of the user based on the input.
   * The input is reviewed for syntactical patterns in order to establish the minimal context necessary to respond.
   * The method tries to find out important keyword.
   */
     
    val file = new File("src/psychiatrist/library.xml")
    val loadFile = XML.loadFile(file)
    val readSad = {
      (loadFile \\ "solve").map(_.attributes("x").toString)
  }
    
     /**Collections of fast-answered phases. More information is needed for analysis*/
     val needToSayMoreDetail = List("yes", "okay", "ok", "yeah")
     val negative = List("no", "i don't know")
     val indifference = List("maybe", "guess")
 
     if (input.trim.contains("?") || input.trim.contains("you")) {
       question(Random.nextInt(question.length)).toString
     } else if (input.split(' ').exists(needToSayMoreDetail.contains(_))) {
       haveMoreToSay(Random.nextInt(haveMoreToSay.length)).toString
     } else if (input.split(' ').exists(negative.contains(_))) {
       moreInfo(Random.nextInt(moreInfo.length)).toString
     } else if (input.isEmpty) {
       blanky(Random.nextInt(blanky.length)).toString
     } else if (input.split(' ').exists(indifference.contains(_))) {
       uncertain(Random.nextInt(uncertain.length)).toString 
     } else if (input.split(' ').exists(readSad.contains(_))) {
       keywordFound(Random.nextInt(keywordFound.length)).toString
     } else
       expressions(Random.nextInt(expressions.length)).toString + " " + (input + "?").toLowerCase.split(" ").map(changePronoun).mkString(" ")
  }   
}
object Brain2 {
  val changeSubject = Map[String, Array[String]](
    "you"       -> Array("i", "me", "we", "us"),
    "are"       -> Array("am"),
    "those"     -> Array("these"),
    "that"      -> Array("this"),
    "your"      -> Array("my", "our"),
    "you are"   -> Array("i'm", "we're"),
    "you will"  -> Array("i'll", "we'll"),
    "you had"   -> Array("i'd", "we'd"),
    "you have"  -> Array("i've", "we've"),
    
  )
}

