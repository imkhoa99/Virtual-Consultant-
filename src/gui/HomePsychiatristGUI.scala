package gui
import scala.swing._
import scala.swing.event._
import java.awt.Font._
import psychiatrist._
import scala.xml._
import java.io.File
import scala.util.Random

object HomePsychiatristGUI extends SimpleSwingApplication {
  def top = new MainFrame {


    /**Initialize the psychiatrist's brain. The psychiatrist uses the brain for input analysis. **/
    val psychiatrist = Brain
    
    

    /**The Input section, used for displaying the part where the user types the input. **/
    val inputLable = new Label("Input:") {
      font          = new Font("Monaco", BOLD, 30)
    }

    val converseLable = new Label("Conversation With The Psychiatrist:") {
      font          = new Font("Monaco", BOLD, 30)
    }
    
    /**Display the field where the session takes place. **/
    val conversation = new TextArea() {
      font          = new Font("Serif", BOLD, 20)
      editable      = false
      wordWrap      = true
      lineWrap      = true
    }

    val input = new TextArea() {
      font          = new Font("Serif", BOLD, 20)
      wordWrap      = true
      lineWrap      = true
    }

    val scrolling = new ScrollPane(conversation) {
      preferredSize = new Dimension(1000, 700)
    }

    val scrollInput = new ScrollPane(input) {
      preferredSize = new Dimension(1000, 150)
    }

    // Layout:
    contents = new BoxPanel(Orientation.Vertical) {
      contents += converseLable
      contents += scrolling
      contents += inputLable
      contents += scrollInput 
      contents += Button("Close") { sys.exit(0) }
      border = Swing.TitledBorder(Swing.LineBorder(java.awt.Color.RED), "Enjoy the program")
    }
    
    listenTo(input.keys)

    /**Setup the initial state of the GUI **/
    title = "Psychiatrist"
    Loader.loadExpressions
    
   /**First, a welcome message is printed, then the player gets to 
   * elaborate on their feeling**/
  val file = new File("src/psychiatrist/expressions.xml")
  val expression = XML.loadFile(file)
  val opening = {
    (expression \\ "start").map(_.attributes("x"))
  }
    conversation.append("Psychiatrist: " + (opening(Random.nextInt(opening.length)) + "\n"))
    input.requestFocusInWindow()

   /**Listen to events**/
    reactions += {
      case keyEvent: KeyPressed =>
        if (keyEvent.source == input && keyEvent.key == Key.Enter) {
          if (input.text.nonEmpty) {
            sessionStart
            input.text = ""
          }
        }
    }
    
    /**Load the goodbye phases. 
   * 
   */
  val goodbye = new File("src/psychiatrist/Signaling.xml")
  val x = XML.loadFile(goodbye)
  val closing = {
    (x \\ "phrases").map(_.attributes("x"))
  }
  
  /**Load the collection of inspirational quote into the brain. 
   * The program pops up one for soothing during the analysis process.
   */
    val load = new File("src/psychiatrist/advice.xml")
    val loadAdvice = XML.loadFile(load)
    val loading = (loadAdvice \\ "advice").map(_.attributes("x"))

  /**The program starts to receive the input and then analyzes based on the input.
   * The session ends when the user says 'good bye'.
   * **/
    def sessionStart() = {
      Loader.loadExpressions
      Brain.getInput(input.text.trim)
      if (psychiatrist.isOver) {
        conversation.append("User: " + input.text.trim + "\n")
        conversation.append(closing(Random.nextInt(closing.length)) + "\n")
        
        /**The program pops up a message asking the user if he/she wants to save the conversation into the file.**/
        val res1 = Dialog.showConfirmation(contents.head, "Do you want to save the conversation for archive?",optionType=Dialog.Options.YesNo)
        if (res1 == Dialog.Result.Ok) {
          Dialog.showInput(contents.head,
                           "Please enter filename:",
                           initial = "(for example: 'archive.txt')") match {
            case Some(filename) => {
              Writer.write(filename, Writer.getDateAndTime + "\n\n")
              Writer.write(filename, conversation.text)
              Writer.write(filename, "\n" + "-"*100 + "\n")
            }
            case None =>
          }
        } 

        val res2 = Dialog.showConfirmation(contents.head, "Do you want to start another session?")
        if (res2 == Dialog.Result.Ok) {
          conversation.text = "Psychiatrist" + (opening(Random.nextInt(opening.length)))
          input.requestFocusInWindow()
        }
        else
          input.enabled = false
      }
      else {
        conversation.append("User: " + input.text.trim() + "\n")
        conversation.append(loading(Random.nextInt(loading.length)) + "\n")
        conversation.append(Brain.analyze + "\n")
        scrolling.revalidate()
      }
    }
  }
}