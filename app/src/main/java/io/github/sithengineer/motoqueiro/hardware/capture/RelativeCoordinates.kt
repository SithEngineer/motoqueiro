package io.github.sithengineer.motoqueiro.hardware.capture

class RelativeCoordinates(val xx: Float, val yy: Float, val zz: Float, val timestamp: Long) {

  override fun toString(): String {
    return "x=$xx, y=$yy, z=$zz, timestamp=$timestamp"
  }
}
