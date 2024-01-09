package dev.keloran.privateer.logic

import kotlin.math.max

data class ShipDetails(val sails: Int, val crew: Int)
data class WinDetails(
  val minRolls: MinRolls ?= null,
  val impossible: Impossible? = null,
  val other: Other? = null
) {
  data class MinRolls(
    val aggressor: Int? = null,
    val defender: Int? = null
  )

  data class Impossible(
    val win: Boolean? = null,
    val lose: Boolean? = null
  )

  data class Other(
    val aggressor: Boolean? = null,
    val defender: Boolean? = null
  )
}


class Logic {
  companion object {
    fun SimpleCombat(playerCannons: Int, enemyHull: Int): String {
      val minRoll: Int = max(1, (enemyHull - playerCannons + 1))
      if (minRoll > 6) {
        return "Impossible"
      } else if (minRoll < 1 || minRoll == 1) {
        return "Automatic Win"
      }

      return String.format("You need to roll at least %d", minRoll)
    }

    fun AdvancedCombat(aggressor: ShipDetails, defender: ShipDetails): WinDetails {
      val ma = max(1, (defender.sails - aggressor.crew) + 1)
      val md = max(1, (aggressor.sails - defender.crew) + 1)

      if (ma > 6 || md < 1) {
        return WinDetails(
          impossible = WinDetails.Impossible(win = true)
        )
      }

      if (md > 6 || ma < 1) {
        return WinDetails(
          impossible = WinDetails.Impossible(lose = true)
        )
      }

      if (md == 1) {
        return WinDetails(
          minRolls = WinDetails.MinRolls(aggressor = ma),
          other = WinDetails.Other(defender = true)
        )
      }

      if (ma == 1) {
        return WinDetails(
          minRolls = WinDetails.MinRolls(defender = md),
          other = WinDetails.Other(aggressor = true)
        )
      }

      return WinDetails(
        WinDetails.MinRolls(aggressor = ma, defender = md),
      )
    }
  }
}
