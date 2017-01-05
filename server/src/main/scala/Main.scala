import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.{ActorSelectionRoutee, RoundRobinRoutingLogic, Router}

object Main {
  def main(args: Array[String]): Unit = {
    val system      = ActorSystem("Server")
    val server = system.actorOf(Props(new Server()), name = "Server")
    server ! "GO"
    system.awaitTermination()
  }
}

class Server() extends Actor {
  private val router = {
    val routees =
      Vector(
        ActorSelectionRoutee(
          context.actorSelection(
            "akka.tcp://SbtRemote@127.0.0.1:5150/user/SbtActor"
          )
        )
      )
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case "GO" => {
      router.route("PING", self)
    }
    case "PONG" => {
      println("Got PONG")
    }
  }
}
