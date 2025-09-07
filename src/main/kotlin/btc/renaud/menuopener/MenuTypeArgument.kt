package btc.renaud.menuopener

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.LiteralMessage
import com.typewritermc.engine.paper.command.dsl.CommandTree
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import java.util.concurrent.CompletableFuture

/**
 * Brigadier argument type for [MenuType].
 */
class MenuTypeArgumentType : CustomArgumentType.Converted<MenuType, String> {
    override fun convert(nativeType: String): MenuType {
        return MenuType.entries.firstOrNull { it.name.equals(nativeType, true) }
            ?: throw SimpleCommandExceptionType(LiteralMessage("Unknown menu type '$nativeType'")).create()
    }

    override fun getNativeType() = StringArgumentType.word()

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val input = builder.remaining.lowercase()
        MenuType.entries
            .filter { it.name.lowercase().startsWith(input) }
            .forEach { builder.suggest(it.name.lowercase()) }
        return builder.buildFuture()
    }
}

fun CommandTree.menuType(name: String, block: (MenuType) -> Unit) {
    argument(name, MenuTypeArgumentType(), MenuType::class) { ref ->
        executes { block(ref()) }
    }
}
