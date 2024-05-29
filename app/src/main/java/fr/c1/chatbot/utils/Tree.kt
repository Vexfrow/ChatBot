package fr.c1.chatbot.utils

import android.content.Context
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private const val TAG = "Tree"

data class Node(
    val id: Int,
    val text: String,
    val action: String? = null
)

data class Link(
    val from: Int,
    val to: Int,
    val answer: Int
)

data class JsonData(
    val robot: List<Node>,
    val humain: List<Node>,
    val link: List<Link>
)

data class TreeNode(
    val id: Int,
    val text: String,
    val action: String? = null,
    val children: MutableList<TreeNode> = mutableListOf(),
    val links: MutableList<Pair<String, TreeNode>> = mutableListOf()
)

fun parseJson(context: Context, resourceId: Int): JsonData {
    val inputStream = context.resources.openRawResource(resourceId)
    val json = inputStream.bufferedReader().use { it.readText() }

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<JsonData> = moshi.adapter(JsonData::class.java)
    return jsonAdapter.fromJson(json) ?: throw IllegalArgumentException("Invalid JSON")
}

fun buildTree(jsonData: JsonData): TreeNode? {
    val nodes = (jsonData.robot + jsonData.humain).associateBy { it.id }
    val treeNodes = nodes.mapValues { (_, node) ->
        TreeNode(id = node.id, text = node.text, action = node.action)
    }

    jsonData.link.forEach { link ->
        val parent = treeNodes[link.from]
        val child = treeNodes[link.to]
        val answerText = nodes[link.answer]?.text
        if (parent != null && child != null && answerText != null) {
            parent.links.add(Pair(answerText, child))
        }
    }

    return treeNodes[0] // Root node with id 0
}

fun printTree(node: TreeNode, level: Int = 0) {
    // Utiliser Log.d pour afficher les messages dans Logcat
    Log.d("TreePrinter", "${"|  ".repeat(level)}${node.text}")
    node.links.forEach { (linkText, child) ->
        Log.d("TreePrinter", "${"|  ".repeat(level + 1)}-> $linkText")
        printTree(child, level + 2)
    }
}