package dev.keloran.privateer.logic

import kotlin.math.max

data class ShipDetails(val sails: Int, val crew: Int)
data class WinDetails(
  val minRolls: MinRolls,
  val impossible: Impossible
) {
  data class MinRolls(
    val aggressor: Int,
    val defender: Int
  )

  data class Impossible(
    val win: Boolean,
    val lose: Boolean
  )
}


class Logic {
  companion object {
    fun SimpleCombat(playerCannons: Int, enemyHull: Int): String {
      val minRoll: Int = max(1, (enemyHull - playerCannons + 1))
      if (minRoll > 6) {
        return "Impossible"
      } else if (minRoll < 2) {
        return "Automatic Win"
      }

      return String.format("You need to roll at least %d", minRoll)
    }

    fun AdvancedCombat(aggressor: ShipDetails, defender: ShipDetails): WinDetails {
      val ma = max(1, (defender.sails - aggressor.crew) + 1)
      val md = max(1, (aggressor.sails - defender.crew) + 1)

      if (ma > 6 || md < 2) {
        return WinDetails(
          WinDetails.MinRolls(0, 0),
          WinDetails.Impossible(win = true, false)
        )
      }

      if (md > 6 || ma < 2) {
        return WinDetails(
          WinDetails.MinRolls(0, 0),
          WinDetails.Impossible(win = false, true)
        )
      }

      return WinDetails(
        WinDetails.MinRolls(aggressor = ma, defender = md),
        WinDetails.Impossible(win = false, false)
      )
    }
  }
}
