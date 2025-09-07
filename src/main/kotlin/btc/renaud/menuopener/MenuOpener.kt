package btc.renaud.menuopener

import com.typewritermc.engine.paper.plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.BrewerInventory
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Utility responsible for opening vanilla menus for players.
 */
object MenuOpener : Listener {
    private data class EnchantSession(
        val forceLevel: Int,
        val consumeLevels: Boolean,
        val previousLevel: Int,
    )

    private val enchantingSessions = mutableMapOf<UUID, EnchantSession>()

    init {
        // Register events when object is loaded.
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    /**
     * Opens the given [type] menu for [player] with [options].
     */
    @Suppress("DEPRECATION")
    fun open(player: Player, type: MenuType, options: MenuOpenOptions = MenuOpenOptions()) {
        when (type) {
            MenuType.WORKBENCH -> player.openWorkbench(null, true)
            MenuType.ENCHANTING -> openEnchanting(player, options)
            MenuType.ANVIL -> player.openAnvil(null, true)
            MenuType.SMITHING -> player.openSmithingTable(null, true)
            MenuType.GRINDSTONE -> player.openGrindstone(null, true)
            MenuType.CARTOGRAPHY -> player.openCartographyTable(null, true)
            MenuType.STONECUTTER -> player.openStonecutter(null, true)
            MenuType.LOOM -> player.openLoom(null, true)
            MenuType.BREWING -> openBrewing(player)
        }
    }

    private fun openBrewing(player: Player) {
        val inv = Bukkit.createInventory(null, InventoryType.BREWING) as BrewerInventory
        inv.fuel = ItemStack(Material.BLAZE_POWDER, 64)
        player.openInventory(inv)
    }

    @Suppress("DEPRECATION")
    private fun openEnchanting(player: Player, options: MenuOpenOptions) {
        val view = player.openEnchanting(null, true)
        val top = view?.topInventory
        if (options.withLapis && top is EnchantingInventory) {
            top.secondary = ItemStack(Material.LAPIS_LAZULI, 64)
        }
        enchantingSessions[player.uniqueId] = EnchantSession(
            forceLevel = options.forceEnchantLevel,
            consumeLevels = options.consumeLevels,
            previousLevel = player.level
        )
        player.level = options.forceEnchantLevel
    }

    @EventHandler
    @Suppress("DEPRECATION")
    fun onPrepareEnchant(event: PrepareItemEnchantEvent) {
        val session = enchantingSessions[event.enchanter.uniqueId] ?: return
        val costs = event.expLevelCostsOffered
        for (i in costs.indices) {
            costs[i] = session.forceLevel
        }
        // No need to reassign; mutating the array is sufficient.
    }

    @EventHandler
    fun onEnchant(event: EnchantItemEvent) {
        val session = enchantingSessions[event.enchanter.uniqueId] ?: return
        val player = event.enchanter
        if (session.consumeLevels) {
            val remaining = (session.previousLevel - event.expLevelCost).coerceAtLeast(0)
            player.level = remaining
        } else {
            event.expLevelCost = 1
            player.level = session.previousLevel
        }
    }

    @EventHandler
    fun onBrew(event: BrewEvent) {
        event.contents.fuel = ItemStack(Material.BLAZE_POWDER, 64)
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val session = enchantingSessions.remove(event.player.uniqueId) ?: return
        val player = event.player as? Player ?: return
        if (!session.consumeLevels) {
            player.level = session.previousLevel
        }
    }
}

/**
 * Options controlling how menus are opened.
 */
data class MenuOpenOptions(
    val title: String? = null,
    val forceEnchantLevel: Int = 30,
    val withLapis: Boolean = false,
    val consumeLevels: Boolean = false,
)
