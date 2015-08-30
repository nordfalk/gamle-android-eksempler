package lekt07_fragmenter;

import android.view.animation.Interpolator;

/**
 * Created by j on 30-09-14.
 */
class ParrabelInterpolator implements Interpolator {
  private final float mTension = 10;

  /**
   * Maps a value representing the elapsed fraction of an animation to a value that represents
   * the interpolated fraction. This interpolated value is then multiplied by the change in
   * value of an animation to derive the animated value at the current elapsed animation time.
   *
   * @param t A value between 0 and 1.0 indicating our current point
   *          in the animation where 0 represents the start and 1.0 represents
   *          the end
   * @return The interpolation value. This value can be more than 1.0 for
   * interpolators which overshoot their targets, or less than 0 for
   * interpolators that undershoot their targets.
   */
  public float getInterpolation(float t) {
    // bemærk, for t=1 skullle vi egentlig ende på 1,
    // så det her er ikke nogen helt velfungerende interpolator, da den ender på 0!
    return t * (1 - t) * mTension;
  }
}
