package lekt07_fragmenter;

import android.view.animation.Interpolator;

/**
 * Created by j on 30-09-14.
 */
class AnimationInterpolator implements Interpolator {
  private final float mTension = 10;

  /**
   * Maps a value representing the elapsed fraction of an animation to a value that represents
   * the interpolated fraction. This interpolated value is then multiplied by the change in
   * value of an animation to derive the animated value at the current elapsed animation time.
   *
   * @param t A value between 0 and 1.0 indicating our current point
   *        in the animation where 0 represents the start and 1.0 represents
   *        the end
   * @return The interpolation value. This value can be more than 1.0 for
   *         interpolators which overshoot their targets, or less than 0 for
   *         interpolators that undershoot their targets.
   */
  public float getInterpolation(float t) {
    // _o(t) = t * t * ((tension + 1) * t + tension)
    // o(t) = _o(t - 1) + 1
    //t -= 1.0f;
    //return t * t * ((mTension + 1) * t + mTension) + 1.0f;
    return t * (1-t) * mTension;
  }
}
