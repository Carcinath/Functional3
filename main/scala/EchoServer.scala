import java.net._
import java.io._
import scala.io._
import scala.concurrent._

//With help from Dr. Beaty, Nathan Whitney, and Ernesto Estrada
object EchoServer {
  def read_and_write(in: BufferedReader, out:BufferedWriter): Unit = {
    val serverData = in.readLine()
    out.write(in.readLine())
    out.write("\r\n")

    try{
      readHome(serverData, out)
    }
    catch{
      case ex: FileNotFoundException =>{
        errorHandling(serverData, out)
      }
    }
    out.flush()
    in.close()
    out.close()
  }

  def serve(server: ServerSocket): Unit = {
    val s = server.accept()
    val in = new BufferedReader(new InputStreamReader(s.getInputStream))
    val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

    read_and_write(in, out)

    s.close()
  }

  def main(args: Array[String]) {
    val server = new ServerSocket(9999)
    while(true) {
      serve(server)
      Futures( serve(server) )
    }
  }

  def readHome(in: String, out:BufferedWriter): Unit = {
    val htmlFile = Source.fromFile("home.html")
    val serverIn = in.split(" ")

    out.write(s"${serverIn(2)} Home Page\r\n")
    out.write("Content-Type=text/html\r\n")
    out.write("\r\n")
    out.write(htmlFile.mkString)

  }

  def errorHandling(in: String, out:BufferedWriter): Unit = {
    val fourohfour = Source.fromFile("404.html")
    val serverIn = in.split(" ")

    out.write(s"${serverIn(2)} 404 File Not Found \r\n")
    out.write("Content-Type=text/html\r\n")
    out.write("\r\n")
    out.write(fourohfour.mkString)

  }
}
