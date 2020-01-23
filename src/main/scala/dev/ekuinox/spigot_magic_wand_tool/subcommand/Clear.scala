package dev.ekuinox.spigot_magic_wand_tool.subcommand
import dev.ekuinox.spigot_magic_wand_tool.{SpigotMagicWandTool, permisison}
import dev.ekuinox.spigot_magic_wand_tool.location.LocationsManager
import org.bukkit.command.{Command, CommandSender}
import org.bukkit.entity.Player

object Clear extends SubCommand {
  override val name: String = "clear"

  override def run(plugin: SpigotMagicWandTool, sender: CommandSender, command: Command, label: String, args: Array[String]): Unit = {
    if (!sender.isInstanceOf[Player]) return

    val player = sender.asInstanceOf[Player]

    if (!player.hasPermission(permisison.Write)) return

    for {
      locations <- LocationsManager.get(player)
    } {
      locations.foreach(location => plugin.particleManager.stopParticle(player, location))
      LocationsManager.clear(player)
    }
  }
}