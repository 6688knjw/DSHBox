package com.dshbox.terminal

import java.io.File

/**
 * Makes the bundled DSH CLI available as `dsh` inside the interactive
 * sandbox terminal.
 *
 * Debian's `/etc/profile` (sourced by `bash --login`) resets PATH to the
 * stock `/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:...` list, so
 * putting the npm `.bin` directory on the process environment is not
 * enough. Bind a tiny wrapper onto `/usr/bin/dsh` — that path survives
 * the login PATH reset and is not covered by the node-layer bind at
 * `/usr/local`.
 */
object TerminalDshCli {

    const val GUEST_PATH = "/usr/bin/dsh"

    fun ensureBindArgs(paths: TerminalPaths): List<String> {
        val dshDir = paths.dshDir ?: return emptyList()
        if (!dshDir.isDirectory) return emptyList()
        val dir = File(paths.debianRootfs, SHIM_DIR)
        dir.mkdirs()
        val shim = File(dir, "dsh")
        if (!shim.isFile || shim.readText() != SCRIPT) {
            shim.writeText(SCRIPT)
        }
        shim.setExecutable(true, false)
        return listOf("--bind=${shim.absolutePath}:$GUEST_PATH")
    }

    private const val SHIM_DIR = "root/.dshcli"

    internal const val SCRIPT =
        "#!/bin/sh\n" +
            "exec /usr/local/bin/node /opt/dshapp/runtime/node_modules/@deepseek-ai/dsh/lib/bin.js \"\$@\"\n"
}
