import org.scalatest._
import math._

class RBOTests extends FlatSpec with Matchers {

  "Identical Sequences" should "have an RBO of 1.0 for all p" in {
    val ints1 = Seq(1,2,3,4,5)
    val ints2 = Seq(1,2,3,4,5)
    RBO.eval(ints1, ints2, 0.5) should be (1.0)
    RBO.eval(ints1, ints2, 0.995) should be (1.0)
    RBO.eval(ints1, ints2, 0.005) should be (1.0)
  }

  "Strings" should "works just like integers" in {
   RBO.eval(Seq(1,2,3,4,5), Seq(2,3,4,1,5), 0.9) should be 
    ( RBO.eval(Seq("1","2","3","4","5"), Seq("2","3","4","1","5"), 0.9) )
  }

  "composite values" should "also work" in {
    case class foo(x:Int, y:String)

    val foo1 = Seq(foo(1,"1"), foo(2,"2"), foo(3,"3"))
    val foo2 = Seq(foo(3,"3"), foo(2,"2"), foo(1,"1"))
    RBO.eval(foo1,foo2,1.0) should be (1.0)

  }

  "No matches after shortest seq length" should "not decrease RBO" in {
    RBO.eval(Seq(1,2,3), Seq(2,3,1,4), 0.5) - RBO.eval(Seq(1,2,3), Seq(2,3,1,4,9,16,83,437), 0.5) should be (0.0)
  }

  "Parameters outside of (0,1)" should "throw an exception" in {
    var caught = 0
    var succeeded = 0
    Seq(-1, 1.0001, 0.5).foreach { i => 
    	try {
    		RBO.eval(Seq(1),Seq(1), i)
    		succeeded += 1
    	} catch {
    		case _:Throwable => caught += 1
    	}
    }
    caught should be (2)
    succeeded should be (1)
  }

  "Reference types" should "also work" in {
  	class bar(i:Int) {
  		val _i = i
  		override def equals(other:Any) = {
  			other.asInstanceOf[bar]._i == i
  		}
  		override def hashCode() = {
  			_i.hashCode
  		}
  	}
  	RBO.eval(Seq(new bar(1), new bar(2), new bar(3)), Seq(new bar(3), new bar(2), new bar(1)), 1.0) should be (1.0)
  }

  "Increasing p on an inverted list" should "resolve to 1.0" in {

  	val res = Seq(0.1,0.5,0.75,1.0)
  	  .map(i => RBO.eval((1 to 10).reverse, (1 to 10), i))
  	(0 until res.length - 1).foreach { i =>
  		math.signum((res(i) - res(i+1))) should be (-1)
  	}
  	res(res.length -1) should be (1.0)

  }
}




