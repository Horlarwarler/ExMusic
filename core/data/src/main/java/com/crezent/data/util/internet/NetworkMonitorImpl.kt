package com.crezent.data.util.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

class NetworkMonitorImpl (
    context: Context
): NetworkMonitorInterface {

    private val connectivityManager: ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)
    override fun observeNetwork(): Flow<NetworkState> {
        return callbackFlow {
            val callBack = object : ConnectivityManager.NetworkCallback(){

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        if(confirmInternetExists()){
                            send(NetworkState.Connected)
                        }
                        else{
                            send(NetworkState.Losing)
                        }
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                   launch {
                       send(NetworkState.Losing)
                   }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(NetworkState.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(NetworkState.Unavailable)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callBack)
            awaitClose{
                connectivityManager.unregisterNetworkCallback(callBack)
            }
        }.distinctUntilChanged()
    }

    private fun confirmInternetExists() : Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8",53))
            socket.close()
            true
        } catch ( e: Exception){
            e.printStackTrace()
            false
        }
    }
}