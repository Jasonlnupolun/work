/**
  * Created by Administrator on 2017/2/9.
  */
package object learning {
  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  def thread(body: =>Unit): Thread = {
    val t = new Thread {
      override def run() = body
    }
    t.start()
    t
  }

}
