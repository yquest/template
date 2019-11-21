package pt.fabm.shell

import io.vertx.reactivex.core.cli.CLI
import io.vertx.reactivex.ext.shell.command.CommandBuilder
import io.vertx.reactivex.ext.shell.command.CommandProcess

class ParentArgument(
  override val node: String,
  private val children: MutableList<LevelArgument> = mutableListOf()
) : LevelArgument {
  override val isTerminal: Boolean = false

  fun command(node: String, handler: (CommandProcess) -> Unit) {
    children.add(CommandArgument(node, handler))
  }

  fun node(node: String, init: ParentArgument.() -> Unit) {
    val child = ParentArgument(node)
    children.add(child)
    child.init()
  }

  fun node(node: String): ParentArgument {
    val values = ParentArgument(node)
    this.children.add(values)
    return values
  }

  fun createCommandBuilder(): CommandBuilder {
    val cli = CLI.create(node)
    val commandBuilder = CommandBuilder.command(cli)
    commandBuilder.completionHandler { completion ->

      val lineTokens = completion.lineTokens().filter { it.isText }
      if (lineTokens.isEmpty()) {
        completion.complete(children.map { it.node })
        return@completionHandler
      }
      var current = this
      val iter = lineTokens.iterator()
      loop@ while (iter.hasNext()) {
        val value = iter.next().value()
        val found = current.children.find { it.node == value }
        when {
          found == null -> {
            val candidates = current.children.filter { it.node.startsWith(value) }
              .map { it.node }

            if (value.trim().isEmpty()) {
              completion.complete(current.children.map { it.node })
              return@completionHandler
            }

            if (candidates.size == 1) completion.complete(candidates[0].substring(value.length), true)
            else completion.complete(candidates)
            return@completionHandler
          }
          found.isTerminal -> {
            completion.complete("", true)
            return@completionHandler
          }
          else -> {
            current = found as ParentArgument
            if (!iter.hasNext()) completion.complete(current.children.map { it.node })
            else continue@loop
          }
        }
      }
    }
    commandBuilder.processHandler { process ->

      val commandLine = process.commandLine()
      val args = commandLine.allArguments()
      var current = this
      for (i in args.indices) {
        val found = current.children.find { it.node == args[i] }
        if (found == null) {
          print("no path found for ${args.joinToString(" ")}")
          return@processHandler
        } else if (!found.isTerminal) {
          current = found as ParentArgument
          continue
        } else {
          (found as CommandArgument).handler(process)
        }
      }
    }
    return commandBuilder
  }
}

