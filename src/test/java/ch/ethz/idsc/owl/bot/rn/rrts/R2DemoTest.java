// code by jph
package ch.ethz.idsc.owl.bot.rn.rrts;

import ch.ethz.idsc.owl.bot.rn.RnNodeCollection;
import ch.ethz.idsc.owl.bot.rn.RnTransitionSpace;
import ch.ethz.idsc.owl.rrts.adapter.EmptyTransitionRegionQuery;
import ch.ethz.idsc.owl.rrts.adapter.LengthCostFunction;
import ch.ethz.idsc.owl.rrts.core.DefaultRrts;
import ch.ethz.idsc.owl.rrts.core.Rrts;
import ch.ethz.idsc.owl.rrts.core.RrtsNode;
import ch.ethz.idsc.owl.rrts.core.RrtsNodeCollection;
import ch.ethz.idsc.owl.rrts.core.TransitionRegionQuery;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class R2DemoTest extends TestCase {
  public void testSimple() {
    RnTransitionSpace rnts = new RnTransitionSpace();
    RrtsNodeCollection nc = new RnNodeCollection(Tensors.vector(0, 0), Tensors.vector(10, 10));
    TransitionRegionQuery trq = EmptyTransitionRegionQuery.INSTANCE;
    Rrts rrts = new DefaultRrts(rnts, nc, trq, LengthCostFunction.IDENTITY);
    RrtsNode root = rrts.insertAsNode(Tensors.vector(0, 0), 0).get();
    assertEquals(root.children().size(), 0);
    RrtsNode n1 = rrts.insertAsNode(Tensors.vector(1, 0), 0).get();
    assertEquals(root.children().size(), 1);
    assertEquals(n1.costFromRoot(), RealScalar.of(1));
    RrtsNode n2 = rrts.insertAsNode(Tensors.vector(1, 1), 0).get();
    assertEquals(root.children().size(), 1);
    assertEquals(n1.children().size(), 1);
    assertEquals(n1.children().iterator().next(), n2);
    assertEquals(n1.costFromRoot(), RealScalar.of(1));
    assertEquals(n2.children().size(), 0);
    assertEquals(n2.costFromRoot(), RealScalar.of(2));
  }
}
