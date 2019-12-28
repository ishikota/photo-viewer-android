package com.ishikota.photoviewerandroid.infra.paging

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class PagingNetworkState private constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val LOADED = PagingNetworkState(Status.SUCCESS)
        val LOADING = PagingNetworkState(Status.RUNNING)
        fun error(msg: String?) = PagingNetworkState(Status.FAILED, msg)
    }
}
