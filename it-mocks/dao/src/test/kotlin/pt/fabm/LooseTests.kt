package pt.fabm

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.Test

class LooseTests {
  @Test
  fun someTest() {

    //val toIngnore: Maybe<String> = Maybe.just("hi there")
    val toIngnore: Maybe<String> = Maybe.fromCallable {
      println("when is null it's an empty maybe")
      null
    }

    val hello = Completable.fromCallable {
      println("hi there")
    }

    toIngnore.flatMapCompletable {
      hello
    }.subscribe()
  }
}
