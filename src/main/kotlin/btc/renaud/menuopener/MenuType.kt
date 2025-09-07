package btc.renaud.menuopener

import org.bukkit.event.inventory.InventoryType

/**
 * Enum representing supported vanilla menu types.
 */
enum class MenuType {
    WORKBENCH,
    ENCHANTING,
    ANVIL,
    SMITHING,
    GRINDSTONE,
    CARTOGRAPHY,
    STONECUTTER,
    LOOM,
    BREWING;

    /**
     * Map the menu type to the Bukkit [InventoryType] when applicable.
     */
    val inventoryType: InventoryType?
        get() = when (this) {
            WORKBENCH -> InventoryType.WORKBENCH
            ENCHANTING -> InventoryType.ENCHANTING
            ANVIL -> InventoryType.ANVIL
            SMITHING -> InventoryType.SMITHING
            GRINDSTONE -> InventoryType.GRINDSTONE
            CARTOGRAPHY -> InventoryType.CARTOGRAPHY
            STONECUTTER -> InventoryType.STONECUTTER
            LOOM -> InventoryType.LOOM
            BREWING -> InventoryType.BREWING
        }
}
