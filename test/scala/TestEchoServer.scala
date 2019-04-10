import java.io._
import java.net._

import org.scalatest._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mockito.MockitoSugar

class TestEchoServer extends FlatSpec with Matchers with MockitoSugar {
  "Bytes in" should "be bytes out" in {
    val serversocket = mock[ServerSocket]
    val socket = mock[Socket]
    val bytearrayinputstream = new ByteArrayInputStream("This is a test".getBytes())
    val bytearrayoutputstream = new ByteArrayOutputStream()

    when(serversocket.accept()).thenReturn(socket)
    when(socket.getInputStream).thenReturn(bytearrayinputstream)
    when(socket.getOutputStream).thenReturn(bytearrayoutputstream)

    EchoServer.serve(serversocket)

    bytearrayoutputstream.toString() should be("This is a test")
    verify(socket).close()
  }

  "Read and write" should "echo" in {
    val in = mock[BufferedReader]
    val out = mock[BufferedWriter]

    when(in.readLine()).thenReturn("This is a test")

    EchoServer.read_and_write(in, out)

    verify(out).write("This is a test")
    verify(out).flush()
    verify(out).close()
    verify(in).close()
  }

  "Error Handling" should "echo" in {
    val in = mock[String]
    val out = mock[BufferedWriter]

    when(in.split(" ").last).thenReturn("This is a test")

    verify(out).write("This is a test \r\n")
    verify(out).write("Still a test\r\n")
    verify(out).write("Nine out of ten mothers agree this is a test")
    verify(out).write("\r\n")

    verify(out).flush()
    verify(out).close()

  }

  "Read Home" should "echo" in{
    val in = mock[String]
    val out = mock[BufferedWriter]

    when(in.split(" ").last).thenReturn("This is a Test")

    verify(out).write("We're not sure what the tenth mother thinks. \r\n")
    verify(out).write("Probably that this whole programming thing is witchcraft\r\n")
    verify(out).write("We're not really worried about it to be honest.")
    verify(out).write("\r\n")

    verify(out).flush()
    verify(out).close()
  }
}
