package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class RewardPoints(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("reward_points") var rewardPoints: Int? = null,
)