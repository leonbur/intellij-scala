package org.jetbrains.plugins.scala.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

import com.intellij.lang.ASTNode
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.openapi.util.Key
import org.jetbrains.annotations.Nullable

import java.util.List;

trait ScalaPsiElement extends PsiElement {
    def childSatisfyPredicateForPsiElement(predicate : PsiElement => Boolean) : PsiElement = {
      childSatisfyPredicateForPsiElement(predicate, getFirstChild, (e : PsiElement) => e.getNextSibling)
    }

    def childSatisfyPredicateForPsiElement(predicate : PsiElement => Boolean, startsWith : PsiElement) : PsiElement = {
      childSatisfyPredicateForPsiElement(predicate, startsWith, (e : PsiElement) => e.getNextSibling)
    }

    def childSatisfyPredicateForPsiElement(predicate : PsiElement => Boolean, startsWith : PsiElement, direction : PsiElement => PsiElement) : PsiElement = {
      def inner(e : PsiElement) : PsiElement = if (e == null || predicate(e)) e else inner(direction(e))

      if (startsWith != null) inner(startsWith) else inner(getFirstChild)
    }



    def childSatisfyPredicateForElementType(predicate : IElementType => Boolean, startsWith : PsiElement) : PsiElement = {
      childSatisfyPredicateForElementType(predicate, startsWith, (e : PsiElement) => e.getNextSibling)
    }

    def childSatisfyPredicateForElementType(predicate : IElementType => Boolean, startsWith : PsiElement, direction : PsiElement => PsiElement) : PsiElement = {
      def inner(e : PsiElement) : PsiElement = if (e == null || predicate(e.getNode.getElementType)) e else inner(direction(e))

      if (startsWith != null) inner(startsWith) else inner(getFirstChild)
    }

    def childSatisfyPredicateForElementType(predicate : IElementType => Boolean) : PsiElement = {
      childSatisfyPredicateForElementType(predicate, getFirstChild, (e : PsiElement) => e.getNextSibling)
    }



     def childrenOfType[T >: Null <: PsiElement] (tokSet : TokenSet) : Iterable[T] = new Iterable[T] () {
     def elements = new Iterator[T] () {
        private def findChild (child : ASTNode) : ASTNode = child match {
           case null => null
           case _ => if (tokSet.contains(child.getElementType())) child else findChild (child.getTreeNext)
        }

        var n : ASTNode = findChild (getNode.getFirstChildNode)

        def hasNext = n != null

        def next : T =  if (n == null) null else {
          val res = n
          n = findChild (n.getTreeNext)
          res.getPsi().asInstanceOf[T]
        }
      }
    }



    def childrenSatisfyPredicateForPsiElement[T >: Null <: ScalaPsiElementImpl](predicate : PsiElement => Boolean) = new Iterable[T] () {
     def elements = new Iterator[T] () {
        private def findChild (child : ASTNode) : ASTNode = child match {
           case null => null
           case _ => if (predicate(child.getPsi)) child else findChild (child.getTreeNext)
        }

        var n : ASTNode = findChild (getNode.getFirstChildNode)

        def hasNext = n != null

        def next : T =  if (n == null) null else {
          val res = n
          n = findChild (n.getTreeNext)
          res.getPsi().asInstanceOf[T]
        }
      }
    }

//    def childSatisfyPredicateFor(predicate : T => Boolean, startsWith : T) : PsiElement = {
//      childSatisfyPredicateForPsiElement(predicate, startsWith, (e : T) => e.getNextSibling)
//    }

    /*def childSatisfyPredicate[T](predicate : T => Boolean, startsWith : PsiElement, direction : T => T) : PsiElement = {

      def inner(curChild : PsiElement, e : T) : PsiElement = if (e == null || predicate(e)) e else inner(direction curChild, e)

      inner(startsWith, )
    }*/

    def childSatisfyPredicateForASTNode(predicate : ASTNode => Boolean) : PsiElement = {
      def inner(e : PsiElement) : PsiElement = if (e == null || predicate(e.getNode)) e else inner(e.getNextSibling)

      inner(getFirstChild)
    }


    def hasChild(elemType : IElementType) : Boolean = {
      return getChild(elemType) != null
    }

    [Nullable]
    def getChild(elemType : IElementType) : PsiElement = {
      getChild(elemType, getFirstChild, (e : PsiElement) => e.getNextSibling)
    }

    [Nullable]
    def getChild(elemType : IElementType, startsWith : PsiElement) : PsiElement = {
      getChild(elemType, startsWith, (e : PsiElement) => e.getNextSibling)
    }

    [Nullable]
    def getChild(elemType : IElementType, startsWith : PsiElement, direction : PsiElement => PsiElement) : PsiElement = {
      def inner (e : PsiElement) : PsiElement = e match {
         case null => null
         case _ => if (e.getNode.getElementType == elemType) e else inner (direction(e))
      }

      inner (startsWith)
   }
}