package org.jetbrains.plugins.scala.lang.transformation.declarations

import com.intellij.psi.PsiElement
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScReturn
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScFunctionDefinition
import org.jetbrains.plugins.scala.lang.psi.impl.ScalaCode._
import org.jetbrains.plugins.scala.lang.transformation.AbstractTransformer
import org.jetbrains.plugins.scala.project.ProjectContext

class MakeResultExpressionExplicit extends AbstractTransformer {
  override protected def transformation(implicit project: ProjectContext): PartialFunction[PsiElement, Unit] = {
    case e: ScFunctionDefinition if e.hasExplicitType && !e.hasUnitResultType =>
      e.returnUsages.foreach {
        case _: ScReturn => // skip
        case it => it.replace(code"return $it")
      }
  }
}
