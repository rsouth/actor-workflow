package dev.untitled1.actorworkflow.actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import dev.untitled1.actorworkflow.extension.SpringActorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
public class BaseActorTest {

  //    @Autowired
  protected static ActorSystem actorSystem;
  protected static TestKit testKit;
  @Autowired
  protected SpringActorFactory springActorFactory;

  @BeforeAll
  public static void setup() {
    actorSystem = ActorSystem.create("TestActorSystem");
    testKit = new TestKit(actorSystem);
  }

  @AfterAll
  public static void teardown() {
    actorSystem.terminate();
  }

}
