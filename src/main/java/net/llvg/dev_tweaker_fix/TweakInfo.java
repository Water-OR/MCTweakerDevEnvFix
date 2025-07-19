package net.llvg.dev_tweaker_fix;

/**
 * <p>
 * A bundle of tweaker {@code clazz} and {@code order}.<br>
 * Be used in tweaker sorting.
 * </p>
 */
final class TweakInfo
  implements Comparable<TweakInfo>
{
    private final String tweak;
    private final int order;
    
    public TweakInfo(
      String tweak,
      int order
    ) {
        this.tweak = tweak;
        this.order = order;
    }
    
    public String getTweak() {
        return tweak;
    }
    
    @Override
    public int compareTo(TweakInfo o) {
        return Integer.compare(order, o.order);
    }
}
