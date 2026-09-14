package com.dshbox.terminal

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class TerminalDshCliTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private fun paths(dshDir: File?): TerminalPaths = TerminalPaths(
        prootBinary = tmp.newFile("libproot.so"),
        prootLoader = tmp.newFile("loader"),
        nativeLibDir = tmp.newFolder("nativelib"),
        debianRootfs = tmp.newFolder("base"),
        nodeDir = tmp.newFolder("node"),
        dshDir = dshDir,
        workspaceBind = tmp.newFolder("user-data"),
        prootTmpDir = tmp.newFolder("proot-tmp"),
        failsafeHome = tmp.newFolder("home"),
        failsafeTmpDir = tmp.newFolder("hometmp"),
    )

    @Test
    fun `binds dsh wrapper when layer is present`() {
        val dsh = tmp.newFolder("dsh")
        val p = paths(dsh)
        val binds = TerminalDshCli.ensureBindArgs(p)
        assertEquals(1, binds.size)
        assertTrue(binds[0].endsWith(":${TerminalDshCli.GUEST_PATH}"))
        val shim = File(p.debianRootfs, "root/.dshcli/dsh")
        assertTrue(shim.isFile)
        assertEquals(TerminalDshCli.SCRIPT, shim.readText())
        assertTrue(binds[0].startsWith("--bind=${shim.absolutePath}:"))
    }

    @Test
    fun `skips bind when dsh layer is absent`() {
        assertTrue(TerminalDshCli.ensureBindArgs(paths(dshDir = null)).isEmpty())
    }

    @Test
    fun `skips bind when dsh layer is not a directory`() {
        val missing = File(tmp.root, "no-such-dsh")
        assertTrue(TerminalDshCli.ensureBindArgs(paths(dshDir = missing)).isEmpty())
    }
}
