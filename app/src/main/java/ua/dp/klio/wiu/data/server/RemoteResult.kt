package ua.dp.klio.wiu.data.server


import com.google.gson.annotations.SerializedName

data class RemoteResult(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("totalResults")
    val totalResults: Int
)