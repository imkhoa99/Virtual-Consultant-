package psychiatrist
import scala.collection.mutable.Map
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.Assertions._
import scala.util.Random

class UnitTesting extends FlatSpec {
  "Brain.isOver" should "end the session when the user says 'good bye'." in {
      Brain.getInput("good bye")
      assert(Brain.isOver == true)
  }
 
    "Loader.loadExpression" should "load all collection of expression defined in the external files." in {
      Loader.loadExpressions
      assert(Brain.ambiguous == List("Psychiatrist: I don't seem to answer questions. But please go on.", "Psychiatrist: I do not answer questions, sorry. But, what is it you really want to know?"))
      assert(Brain.expressions == List("Psychiatrist: What is the reason for the fact that", "Psychiatrist: OK, I see. Can you elaborate more on that", "Psychiatrist: Keep feeling your body, please go on", "Psychiatrist: What is it that makes", "Psychiatrist: Let's discuss further why", "Psychiatrist: Tell me more about", "Psychiatrist: That is interesting. Please continue", "Psychiatrist: What makes", "Psychiatrist: Why is it happening?"))
      assert(Brain.question == List("Psychiatrist: I don't seem to answer questions. But please go on.", "Psychiatrist: I do not answer questions, sorry. But, what is it you really want to know?"))
      assert(Brain.haveMoreToSay == List("Psychiatrist: You are sure", "Psychiatrist: I see", "Psychiatrist: I'm curious to know more about that"))
      assert(Brain.blanky == List("Psychiatrist: Are you kidding me?", "Psychiatrist: Your sentence is blank. Please retype", "Psychiatrist: Hmm.."))
      assert(Brain.uncertain == (List("Psychiatrist: There is a bit not certain in your thoughts. I suppose.")))
      assert(Brain.moreInfo == List("Psychiatrist: Why not?", "Psychiatrist: Don't you really know?", "Psychiatrist: I'm not sure I understand you fully."))
    } 
    
    "Brain.analyze" should "ask the user to retype when it detects a blank input." in {
         Brain.getInput(" ")
         val y = Brain.blanky.toSeq
         assert(y.contains(Brain.analyze)) 
    }
    "Brain.analyze" should "analyze the input to change the pronoun used in the response." in {
      Brain.getInput("I feel happy")
      assert(Brain.analyze.contains("you"))
  } 
}