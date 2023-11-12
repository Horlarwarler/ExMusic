package com.crezent.common.util

import android.util.Log

class  LinkedList <T> {

    var length = 0

    private var head : Node<T>? = null

    private var tail : Node<T>? = null
    data class Node <T> (
        val value : T,
        var next: Node<T>? = null
    )

    init {
//        val newNode = Node(
//            value = value,
//        )
//        head = newNode
//        tail = newNode
//        length++
    }

    fun getHead():Node<T>?{
        return  head
    }

    fun getTail():Node<T>? {
        return  tail;
    }

    fun append( value: T){
        val newNode = Node(value = value,)
        if (length == 0){
            head = newNode
            tail = newNode
        }
        else {
            tail?.next = newNode
            tail =  newNode
        }
        length++
    }

    fun prepend( value: T){
        val newNode = Node(value = value)
        if (length == 0){
            head = newNode
            tail = newNode
        }
        else {
            newNode.next = head
            head = newNode
        }
        length++
    }

    fun removeFirst(): Node<T>? {
        if (length == 0) return null
        val temp = head
        head = head?.next
        temp?.next = null
        length --
        if (length == 0){
            head = null
            tail = null
        }

        return  temp


    }

    fun printLinkList(){
        println("---------------------------")
        var temp = head;
        while (temp != null){
            Log.d("Temp","${temp.value}")
            //println("Temp ${temp.value}")
            temp = temp.next
        }
        println("---------------------------")

    }
}