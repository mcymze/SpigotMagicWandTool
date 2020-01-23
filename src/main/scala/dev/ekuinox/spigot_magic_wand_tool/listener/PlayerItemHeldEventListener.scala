package dev.ekuinox.spigot_magic_wand_tool.listener

import dev.ekuinox.spigot_magic_wand_tool.{LocationsManager, MagicWand, SpigotMagicWandTool}
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack

class PlayerItemHeldEventListener(plugin: SpigotMagicWandTool) extends Listener(plugin) {

  @EventHandler
  def onPlayerItemHeld(event: PlayerItemHeldEvent): Unit = {
    val player = event.getPlayer
    val inventory = player.getPlayer.getInventory

    val stopParticles = () => {
      for { locations <- LocationsManager.get(player) } {
        locations.foreach(plugin.particleManager.stopParticle(player, _))
      }
    }

    val startParticles = () => {
      for { locations <- LocationsManager.get(player) } {
        locations.foreach(plugin.particleManager.startParticle(player, player.getWorld, _))
      }
    }

    val isMagicWand = (itemStack: ItemStack) => (itemStack != null) && MagicWand.isMatches(itemStack, plugin)

    val execute = (isMagicWand(inventory.getItem(event.getPreviousSlot)), isMagicWand(inventory.getItem(event.getNewSlot))) match {
      case (true, false) => stopParticles
      case (false, true) => startParticles
      case _ => () => {}
    }

    execute()

  }

}