package dev.untitled1.actorworkflow.extension;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringActorFactory {

  private final ActorSystem actorSystem;

  private final SpringExt springExt;

  @Autowired
  public SpringActorFactory(final ActorSystem actorSystem, final SpringExt springExt) {
    this.actorSystem = actorSystem;
    this.springExt = springExt;
  }

  public ActorRef createActor(final String actorName) {
    return actorSystem.actorOf(springExt.props(actorName), actorName + "_" + UUID.randomUUID());
  }

}
