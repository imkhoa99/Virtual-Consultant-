package psychiatrist
import java.io._
import java.util._
import java.text._

object Writer {

  /**Get the date and time of the session. **/
  def getDateAndTime = {
    val format = new SimpleDateFormat("EEE, dd/MM/yyyy, kk:mm a")
    val date = Calendar.getInstance().getTime()
    format.format(date)
  }

  def write(fileName: String, string: String) = {
    val writer = new FileWriter("src/psychiatrist/" + fileName, true)
    writer.write(string)
    writer.close()
  }
}