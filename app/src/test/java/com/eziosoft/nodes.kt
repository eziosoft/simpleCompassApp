///*
// * Created by Bartosz Szczygiel on 3/19/21 2:52 PM
// *  Copyright (c) 2021 . All rights reserved.
// *  Last modified 3/19/21 2:52 PM
// */
//
//package com.eziosoft
//
//import kotlinx.coroutines.runInterruptible
//
//class NodesTest {
//    data class Node(
//        val left: Node? = null,
//        val right: Node? = null,
//        val parent: Node? = null,
//        val value: Int = 0
//    ) {
//        fun hasParent() = parent != null
//        fun hasLeft() = left != null
//        fun hasRight() = right != null
//
//    }
//
//    fun find(node: Node): Node {
//        val max = Int.MAX_VALUE
//
//        //down
//        val l = arrayListOf<Node>()
//        while(containsDown(node))
//        {
//
//        }
//    }
//
//
//    val checkedNodes = arrayListOf<Node>()
//    fun containsDown(node: Node): Boolean {
//        return (node.hasLeft() || node.hasRight())
//    }
//
//}