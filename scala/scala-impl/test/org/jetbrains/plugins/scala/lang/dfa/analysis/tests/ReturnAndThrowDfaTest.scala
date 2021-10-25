package org.jetbrains.plugins.scala.lang.dfa.analysis.tests

import org.jetbrains.plugins.scala.lang.dfa.Messages._
import org.jetbrains.plugins.scala.lang.dfa.analysis.ScalaDfaTestBase

class ReturnAndThrowDfaTest extends ScalaDfaTestBase {

  def testThrowStatement(): Unit = test(codeFromMethodBody(returnType = "Int") {
    """
      |private def otherMethod(x: Int): Int = {
      |  if (x < 5) throw IllegalStateException
      |  x
      |}
      |
      |val a = otherMethod(8)
      |a == 8
      |val b = otherMethod(3)
      |b == 3
      |// we don't support recognizing a thrown exception in interprocedural analysis yet
      |
      |3 == 3
      |throw IllegalArgumentException
      |2 == 2
      |
      |""".stripMargin
  })(
    "a == 8" -> ConditionAlwaysTrue,
    "3 == 3" -> ConditionAlwaysTrue
  )

  def testReturnStatement(): Unit = test(codeFromMethodBody(returnType = "Int") {
    """
      |private def otherMethod(x: Int): Int = {
      |  if (x < 5) return 0
      |  x
      |}
      |
      |val a = otherMethod(8)
      |a == 8
      |val b = otherMethod(3)
      |b == 3
      |
      |3 == 3
      |return 2
      |2 == 2
      |
      |""".stripMargin
  })(
    "a == 8" -> ConditionAlwaysTrue,
    "3 == 3" -> ConditionAlwaysTrue
  )
}