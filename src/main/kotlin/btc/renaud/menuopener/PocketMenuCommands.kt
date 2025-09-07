package btc.renaud.menuopener

import com.typewritermc.core.extension.annotations.TypewriterCommand
import com.typewritermc.engine.paper.command.dsl.*

/**
 * Registers the `/tw pocketmenu` command.
 */
@TypewriterCommand
fun CommandTree.pocketMenuCommand() = literal("pocketmenu") {
    withPermission("tw.pocketmenu.use")
    argument("type", MenuTypeArgumentType(), MenuType::class) { typeRef ->
        literal("--consume") {
            executesOpen(typeRef, consume = true)
        }
        executesOpen(typeRef, consume = false)
    }
}

private fun CommandTree.executesOpen(
    typeRef: ArgumentReference<MenuType>,
    consume: Boolean
) {
    executePlayerOrTarget { target ->
        MenuOpener.open(target, typeRef(), MenuOpenOptions(consumeLevels = consume))
    }
}
