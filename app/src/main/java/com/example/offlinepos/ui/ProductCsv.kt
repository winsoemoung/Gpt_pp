package com.example.offlinepos.ui

import com.example.offlinepos.data.local.ProductEntity

object ProductCsv {
    const val HEADER = "name,sku,barcode,category,costPriceMinor,sellPriceMinor,stockQuantity,lowStockThreshold,unitName,isActive"

    fun encode(product: ProductEntity): String = listOf(
        product.name, product.sku, product.barcode.orEmpty(), product.category,
        product.costPriceMinor.toString(), product.sellPriceMinor.toString(),
        product.stockQuantity.toString(), product.lowStockThreshold.toString(),
        product.unitName, product.isActive.toString(),
    ).joinToString(",") { escape(it) }

    fun parseRow(line: String): List<String> {
        val out = mutableListOf<String>()
        val current = StringBuilder()
        var quoted = false
        var i = 0
        while (i < line.length) {
            when (val c = line[i]) {
                '"' -> if (quoted && i + 1 < line.length && line[i + 1] == '"') { current.append('"'); i++ } else quoted = !quoted
                ',' -> if (quoted) current.append(c) else { out += current.toString(); current.clear() }
                else -> current.append(c)
            }
            i++
        }
        out += current.toString()
        return out
    }

    private fun escape(value: String): String = if (value.any { it == ',' || it == '"' || it == '\n' }) "\"${value.replace("\"", "\"\"")}\"" else value
}
