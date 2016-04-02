# rankmeasures

## RBO
This library contains a scala implementation of [Rank Biased Overlap (RBO)](http://codalism.com/research/papers/wmz10_tois.pdf), specifically the extrapolated form which handles non infinite, and potentially uneven length sequences. This implementation is adapted from Ritesh Agarwal's python implementation ([here](https://github.com/ragrawal/measures/blob/master/measures/rankedlist/RBO.py)), with minor cleanup & fixes.

The RBO.eval function enforces only that the p parameter is in the range 0 <= p <= 1, and assumes callers will supply sequences not containing duplicated values.

Runtime is roughly linear to the size of the input sequences:
![Runtime in milliseconds for sequences of given length](httsp://github.com/b-a-n-y-a-n/rankmeasures/blob/master/meanruntime.png "")

**Examples:**
```scala
RBO.eval(List(1,2,3,4,5), List(2,3,4,1,5), 0.9)
```
*Double = 0.828*

```scala
RBO.eval(Array("a","b","c","d","e"), Array("b","c","d","a","e"),0.9)
```
*Double = 0.828*

```
