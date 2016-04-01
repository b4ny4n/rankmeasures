import math.pow
import scala.collection.mutable.{Set => MSet}

object RBO {

  def main(args:Array[String]) = {
    val s1 = args(0).split(",")
    val s2 = args(1).split(",")
    val p = args(2).toDouble
    println(eval(s1,s2,p))
  }

  private def innerEval[T](s1:Seq[T], s2:Seq[T], p:Double): Double = {
    
    val maxLen = math.max(s1.length, s2.length)
    val minLen = math.min(s1.length, s2.length)
    val listA = if (s1.length > s2.length) s1 else s2
    val listB = if (listA.eq(s1)) s2 else s1
    
    var unseenA:MSet[T] = MSet()
    var unseenB:MSet[T] = MSet()
    var accumSum = 0.0
    var accumOverlap = 0
    var Xs = 0.0
     
    (1 to maxLen).foreach { depth => 
      val a = listA(depth-1)
      val b:Option[T] = if (listB.length >= depth) Some(listB(depth-1)) else None
      if (b.isDefined && a.equals(b.get)) {
        accumOverlap += 1  
      } else {
        if (unseenB.contains(a)) {
            accumOverlap += 1
            unseenB -= a
          } else {
            unseenA += a
          }
      }
      if (b.isDefined) { 
        if (unseenA.contains(b.get)) {
          accumOverlap += 1
          unseenA -= b.get
        } else {
          unseenB += b.get
        }
      } 
      accumSum += (accumOverlap*1.0/depth) * pow(p,depth)
      
      // special handling for equation 32
      if (depth == minLen)
        Xs = accumOverlap*1.0
      
      if (depth > minLen) 
      	accumSum +=  pow(p,depth)*(Xs*(depth - minLen))/(minLen*depth)
    }

    //accumOverlap*1.0/maxLen * pow(p,maxLen)  +  ((1-p)/p) * accumSum
    ((1 - p)/p) * accumSum  + (pow(p,maxLen) * ((accumOverlap - Xs)*1.0/maxLen + Xs*1.0/minLen ))
  }

  def eval[T](s1:Seq[T], s2:Seq[T], p:Double): Double = {
  if (p < 0 || p > 1) {
    throw new Exception("p parameter must be in range 0 <= p <= 1")
    } else {
      innerEval(s1,s2,p)
    }
  }
}

