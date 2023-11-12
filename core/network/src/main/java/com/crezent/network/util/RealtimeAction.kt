package com.crezent.network.util

sealed interface RealtimeAction {
    class FollowUser(val username: String, val otherUsername: String): RealtimeAction
    class UnFollowUser(val username: String, val otherUsername: String): RealtimeAction

     fun asString(): String {
         return when(this){
           is FollowUser ->  {
               "follow:{\"username\":\"$username\",\"otherUsername\":\"$otherUsername\"}\n"
           }
           is UnFollowUser ->{
               "unFollow:{\"username\":\"$username\",\"otherUsername\":\"$otherUsername\"}\n"

           }
       }
    }

}