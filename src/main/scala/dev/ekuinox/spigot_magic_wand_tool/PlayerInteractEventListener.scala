package dev.ekuinox.spigot_magic_wand_tool

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class PlayerInteractEventListener(plugin: SpigotMagicWandTool) extends Listener {

  /**
   * InteractEventが同時に2回呼ばれるのを防ぐため
   */
  object InteractEventTimer {
    private val KEY = "MAGIC_WAND_INTERACT_EVENT_TIMER"
    private val DELAY = 10
    def isActiveTimer(player: Player): Boolean = try { player.getMetadata(InteractEventTimer.KEY).get(0).asBoolean() } catch { case _: Throwable => false }
    def enableTimer(player: Player): Unit = {
      player.setMetadata(KEY, new FixedMetadataValue(plugin, true))
      new BukkitRunnable {
        override def run(): Unit = player.setMetadata(KEY, new FixedMetadataValue(plugin, false))
      }.runTaskLater(plugin, DELAY)
    }
  }
  import InteractEventTimer._
  import MagicWand._

  /**
   * このListenerをPluginManagerに登録する
   */
  def register(): Unit = {
    plugin.getServer.getPluginManager.registerEvents(this, plugin)
  }

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {


    val player = event.getPlayer
    if (isActiveTimer(player)) return

    val item = player.getInventory.getItemInMainHand
    if (isMatches(item)) return

    val clickedBlock = Option(event.getClickedBlock)
    if (clickedBlock.isEmpty) return

    player.sendMessage(s"clickedLocation => ${clickedBlock.get.getLocation}")

    enableTimer(player)
  }

}