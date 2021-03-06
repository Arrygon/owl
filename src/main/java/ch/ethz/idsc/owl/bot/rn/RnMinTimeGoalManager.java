// code by jph and jl
package ch.ethz.idsc.owl.bot.rn;

import java.util.Collection;
import java.util.List;

import ch.ethz.idsc.owl.data.DontModify;
import ch.ethz.idsc.owl.glc.adapter.CatchyTrajectoryRegionQuery;
import ch.ethz.idsc.owl.glc.adapter.StateTimeTrajectories;
import ch.ethz.idsc.owl.glc.core.GlcNode;
import ch.ethz.idsc.owl.glc.core.GoalInterface;
import ch.ethz.idsc.owl.math.flow.Flow;
import ch.ethz.idsc.owl.math.region.RegionWithDistance;
import ch.ethz.idsc.owl.math.region.SphericalRegion;
import ch.ethz.idsc.owl.math.state.StateTime;
import ch.ethz.idsc.owl.math.state.TimeInvariantRegion;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Sign;

/** objective is minimum time.
 * 
 * <p>The distance cost function is suitable for entities that are capable
 * and may need to linger in one spot (u == {0, 0}) because in that case
 * the cost == distance traveled evaluates a non-zero, positive value.
 * 
 * <p>The goal region underlying the target area as well as the heuristic is
 * {@link SphericalRegion}. */
@DontModify
public class RnMinTimeGoalManager extends CatchyTrajectoryRegionQuery implements GoalInterface {
  /** creates a spherical region in R^n with given center and radius.
   * 
   * @param regionWithDistance
   * @param controls */
  public static GoalInterface create(RegionWithDistance<Tensor> regionWithDistance, Collection<Flow> controls) {
    return new RnMinTimeGoalManager(regionWithDistance, RnControls.maxSpeed(controls));
  }

  // ---
  private final RegionWithDistance<Tensor> regionWithDistance;
  private final Scalar maxSpeed;

  /** @param regionWithDistance
   * @param maxSpeed positive */
  public RnMinTimeGoalManager(RegionWithDistance<Tensor> regionWithDistance, Scalar maxSpeed) {
    super(new TimeInvariantRegion(regionWithDistance));
    this.regionWithDistance = regionWithDistance;
    this.maxSpeed = Sign.requirePositive(maxSpeed);
  }

  @Override // from CostIncrementFunction
  public Scalar costIncrement(GlcNode glcNode, List<StateTime> trajectory, Flow flow) {
    return StateTimeTrajectories.timeIncrement(glcNode, trajectory);
  }

  @Override // from HeuristicFunction
  public Scalar minCostToGoal(Tensor x) {
    // max(0, ||x - center|| - radius) / maxSpeed
    return regionWithDistance.distance(x).divide(maxSpeed);
  }
}