package org.jetbrains.plugins.scala.failed.typeInference

import org.jetbrains.plugins.scala.base.ScalaLightCodeInsightFixtureTestCase

class TypeAliasInferenceTest extends ScalaLightCodeInsightFixtureTestCase {

  override protected def shouldPass: Boolean = false

  def testSCL13137(): Unit = {
    checkTextHasNoErrors(
      """
        |trait A[T] {
        |  def f(x : T => T) : Unit = { }
        |  def g(a: A[_]) : Unit = a.f(z => z)
        |}
      """.stripMargin)
  }

  def testSCL13139(): Unit = {
    checkTextHasNoErrors(
      """
        |trait A {
        |  type T
        |  def f(a: A)(x : a.T => a.T)
        |  def g(a: A) : Unit = a.f(a)((z : a.T) => z)
        |}
      """.stripMargin)
  }
}
