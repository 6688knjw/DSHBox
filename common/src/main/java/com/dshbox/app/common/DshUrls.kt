package com.dshbox.app.common

/**
 * DSH web 入口 URL。0.1.2-rc.1 起进程级 launchToken 必须出现在首次请求上，
 * 否则页面只提示 `dsh web authentication required`。
 */
object DshUrls {

    /**
     * 在 [base] 上追加 `?token=<值>`。
     * token 为空（旧版 DSH / 尚未解析到）时原样返回；已带 `token=` 时不重复追加。
     * token 字符集是 base64url（A-Za-z0-9_-），无需 URL 编码。
     */
    fun withLaunchToken(base: String, token: String?): String {
        if (token.isNullOrEmpty() || base.contains("token=")) return base
        val sep = if (base.contains('?')) '&' else '?'
        return "$base${sep}token=$token"
    }
}
