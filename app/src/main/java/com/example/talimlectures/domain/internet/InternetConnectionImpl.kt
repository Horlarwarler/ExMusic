package com.example.talimlectures.domain.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

class InternetConnectionImpl(
    context: Context
): LiveData<NetworkState>() {

    val networkConnections :ArrayList<Network> = ArrayList()
    var connectivityManager: ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)
    private lateinit var connectivityManagerCallBack: ConnectivityManager.NetworkCallback

    private fun announceStatus(){
        if(networkConnections.isNotEmpty()){
            postValue(NetworkState.isConnected)
        }
        else{
            postValue(NetworkState.isDisconnected)
        }
    }

    private fun connectivityManagerCallback() =
        object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkCapability = connectivityManager.getNetworkCapabilities(network)
                val hasNetworkConnection = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false
                if (hasNetworkConnection){
                   CoroutineScope(Dispatchers.IO).launch {
                       if(checkInternet.check()){
                           networkConnections.add(network)
                           announceStatus()
                       }
                   }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                networkConnections.remove(network)
                announceStatus()
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                    CoroutineScope(Dispatchers.IO).launch {
                        if(checkInternet.check()){
                            networkConnections.add(network)
                            announceStatus()
                        }
                    }
                } else {
                    networkConnections.remove(network)
                }
                announceStatus()
            }
        }
    override fun onActive() {
        super.onActive()
        connectivityManagerCallBack = connectivityManagerCallback()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallBack)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallBack)
    }

    object checkInternet {

        fun check() : Boolean {
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



}