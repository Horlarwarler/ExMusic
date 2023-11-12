package com.crezent.user_domain.use_case

import com.crezent.network.api.BaseApi
import com.crezent.network.util.RealtimeAction

class RealtimeActionUseCase (
    private val baseApi: BaseApi
) {
     suspend operator fun invoke(
         shouldFollow:Boolean,
         username:String,
         otherUsername:String
     ){
         val realtimeAction = if (shouldFollow) {
             RealtimeAction.FollowUser(username = username, otherUsername = otherUsername)
         } else {
             RealtimeAction.UnFollowUser(username = username, otherUsername = otherUsername)
         }
         baseApi.sendAction(realtimeAction)
     }
}