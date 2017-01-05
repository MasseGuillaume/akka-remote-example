import akka.actor.{Actor, ActorSystem, Props}

object Main {
  def main(args: Array[String]): Unit = {
    val system      = ActorSystem("SbtRemote")
    val remoteActor = system.actorOf(Props(new SbtActor()), name = "SbtActor")
    system.awaitTermination()
  }
}

class SbtActor() extends Actor {
  def receive = {
    case "PING" => {
      println("got PING")
      sender ! "PONG"
    }
  }
}
