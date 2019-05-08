export class ConvertUtil {

  /**
   * Converts input rad value to degrees
   */
  public static radToDegree(rad: number): number {
    return rad * 180 / Math.PI;
  }

  /**
   * Converts input m/s value to knots
   */
  public static msTokt(rad: number): number {
    return rad * 1.94384;
  }

}
