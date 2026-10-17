package com.example.offlinepos.hardware

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.example.offlinepos.ui.ReceiptData
import com.example.offlinepos.ui.StoreProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object BluetoothPrinterHelper {
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    const val PRINTER_WIDTH_DOTS = 576 // 80mm standard: 576 dots (72 bytes)
    private const val PREFS = "bluetooth_printer_prefs"
    private const val SELECTED_ADDRESS = "selected_printer_address"

    fun selectedPrinterAddress(context: Context): String? = context
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        .getString(SELECTED_ADDRESS, null)

    fun selectPrinter(context: Context, device: BluetoothDevice) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(SELECTED_ADDRESS, device.address)
            .apply()
    }

    @SuppressLint("MissingPermission")
    fun requestPairing(device: BluetoothDevice): Boolean = try {
        device.bondState == BluetoothDevice.BOND_BONDED || device.createBond()
    } catch (_: SecurityException) { false }

    @SuppressLint("MissingPermission")
    private suspend fun connectWithRetry(device: BluetoothDevice): BluetoothSocket {
        var lastError: Throwable? = null
        repeat(3) { attempt ->
            var socket: BluetoothSocket? = null
            try {
                if (device.bondState != BluetoothDevice.BOND_BONDED) {
                    if (!requestPairing(device)) error("Printer is not paired")
                    delay(700L)
                }
                BluetoothAdapter.getDefaultAdapter()?.cancelDiscovery()
                socket = when (attempt) {
                    0 -> device.createRfcommSocketToServiceRecord(SPP_UUID)
                    1 -> device.createInsecureRfcommSocketToServiceRecord(SPP_UUID)
                    else -> runCatching {
                        val method = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                        method.invoke(device, 1) as BluetoothSocket
                    }.getOrElse { device.createRfcommSocketToServiceRecord(SPP_UUID) }
                }
                socket.connect()
                if (socket.isConnected) return socket
                error("Printer socket is not connected")
            } catch (t: Throwable) {
                lastError = t
                try { socket?.close() } catch (_: Exception) {}
                if (attempt < 2) delay(400L * (1L shl attempt))
            }
        }
        throw lastError ?: IllegalStateException("Unable to connect to printer")
    }

    @SuppressLint("MissingPermission")
    suspend fun testPrint(device: BluetoothDevice): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        var socket: BluetoothSocket? = null
        try {
            socket = connectWithRetry(device)
            socket.outputStream.use { output ->
                output.write(EscPos.INIT)
                output.write("\n*** POS TEST PRINT ***\nBluetooth connection OK\n\n".toByteArray(Charsets.UTF_8))
                output.write(byteArrayOf(0x1B, 0x64, 0x03))
                output.write(EscPos.CUT)
                output.flush()
            }
            true to "Test Print အောင်မြင်ပါသည်"
        } catch (error: Exception) {
            false to "Test Print မအောင်မြင်ပါ: ${error.message ?: "Printer ကို စစ်ဆေးပါ"}"
        } finally {
            try { socket?.close() } catch (_: Exception) { }
        }
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(context: Context): List<BluetoothDevice> {
        return try {
            val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
            val adapter = manager?.adapter ?: BluetoothAdapter.getDefaultAdapter()
            adapter?.bondedDevices?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Renders a complete high-fidelity 80mm thermal receipt in Myanmar language
     * onto a 576-pixel-wide Bitmap using native Android text rendering.
     */
    fun renderReceiptBitmap(receipt: ReceiptData, profile: StoreProfile): Bitmap {
        val width = PRINTER_WIDTH_DOTS
        // First pass: measure total height required
        val paintTitle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 32f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val paintHeader = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 21f
            typeface = Typeface.DEFAULT
            textAlign = Paint.Align.CENTER
        }
        val paintBody = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 21f
            typeface = Typeface.DEFAULT
        }
        val paintBodyBold = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val paintTotal = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // Estimate height
        val baseHeaderHeight = 220
        val itemsHeight = (receipt.items.size * 55) + 60
        val summaryHeight = 260
        val footerLines = profile.footerMessage.split("\n").count { it.isNotBlank() }.coerceAtLeast(1)
        val footerHeight = (footerLines * 30) + 120
        val totalHeight = baseHeaderHeight + itemsHeight + summaryHeight + footerHeight

        val bitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        var y = 35f

        // 1. Store header (style and alignment are user-configurable)
        ReceiptStyle.apply(paintTitle, profile.receiptHeaderStyle, profile.receiptHeaderSize, profile.receiptHeaderAlign, 32f)
        val headerX = when (paintTitle.textAlign) { Paint.Align.LEFT -> 20f; Paint.Align.RIGHT -> width - 20f; else -> width / 2f }
        canvas.drawText(profile.storeName, headerX, y, paintTitle)
        y += paintTitle.textSize + 8f

        if (profile.headerNote.isNotBlank()) {
            ReceiptStyle.apply(paintHeader, profile.receiptHeaderStyle, profile.receiptHeaderSize, profile.receiptHeaderAlign, 21f)
            canvas.drawText(profile.headerNote, headerX, y, paintHeader)
            y += paintHeader.textSize + 8f
        }
        if (profile.address.isNotBlank()) {
            canvas.drawText(profile.address, headerX, y, paintHeader)
            y += paintHeader.textSize + 8f
        }
        if (profile.phone.isNotBlank()) {
            canvas.drawText("ဖုန်း: ${profile.phone}", headerX, y, paintHeader)
            y += paintHeader.textSize + 10f
        }

        // Divider
        drawDashedLine(canvas, y, width)
        y += 26f

        // Bill Info
        val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(receipt.timestampEpochMs))
        canvas.drawText("ဘေလ်အမှတ်: #${receipt.saleId.take(8).uppercase()}", 20f, y, paintBody)
        y += 26f
        canvas.drawText("ရက်စွဲ: $dateStr", 20f, y, paintBody)
        canvas.drawText("အရောင်းဝန်ထမ်း: ${receipt.cashierId}", 320f, y, paintBody)
        y += 30f

        // Table Header
        drawDashedLine(canvas, y, width)
        y += 26f
        canvas.drawText("ပစ္စည်းအမည်", 20f, y, paintBodyBold)
        canvas.drawText("အရေအတွက်", 285f, y, paintBodyBold)
        canvas.drawText("နှုန်း", 395f, y, paintBodyBold)
        canvas.drawText("သင့်ငွေ", 515f, y, paintBodyBold)
        y += 26f
        drawDashedLine(canvas, y, width)
        y += 30f

        // Line Items
        receipt.items.forEach { line ->
            // Item name (left side)
            val nameDisplay = if (line.productNameSnapshot.length > 24) {
                line.productNameSnapshot.take(22) + ".."
            } else {
                line.productNameSnapshot
            }
            canvas.drawText(nameDisplay, 20f, y, paintBodyBold)
            y += 24f

            canvas.drawText("${line.quantity}", 285f, y, paintBody)
            canvas.drawText("%,d".format(line.unitPriceMinor), 395f, y, paintBody)
            canvas.drawText("%,d".format(line.lineTotalMinor), 515f, y, paintBodyBold)
            y += 30f
        }

        // Divider
        drawDashedLine(canvas, y, width)
        y += 30f

        // Summary Rows
        drawSummaryRow(canvas, "ကျသင့်ငွေ (Subtotal):", "%,d Ks".format(receipt.totals.subtotalMinor), y, paintBody)
        y += 28f

        if (receipt.totals.discountMinor > 0) {
            drawSummaryRow(canvas, "လျှော့စျေး (Discount):", "-%,d Ks".format(receipt.totals.discountMinor), y, paintBody)
            y += 28f
        }

        if (receipt.deliveryFeeMinor > 0) {
            drawSummaryRow(canvas, "သယ်ယူ ပို့ဆောင်ခ:", "%,d Ks".format(receipt.deliveryFeeMinor), y, paintBody)
            y += 28f
        }

        drawSolidLine(canvas, y, width)
        y += 36f

        // Grand Total
        canvas.drawText("စုစုပေါင်း (TOTAL):", 20f, y, paintTotal)
        canvas.drawText("%,d Ks".format(receipt.totals.totalMinor), 320f, y, paintTotal)
        y += 38f

        drawSolidLine(canvas, y, width)
        y += 28f

        val paymentMethodBurmese = when (receipt.paymentMethod) {
            "CASH" -> "ငွေသား (Cash)"
            "QR_PAY" -> "KPay / Wave QR"
            "CARD" -> "ကတ် (Card)"
            else -> receipt.paymentMethod
        }
        drawSummaryRow(canvas, "ပေးချေမှုပုံစံ:", paymentMethodBurmese, y, paintBody)
        y += 28f
        drawSummaryRow(canvas, "ပေးငွေ (Tendered):", "%,d Ks".format(receipt.tenderedMinor), y, paintBody)
        y += 28f
        drawSummaryRow(canvas, "ပြန်အမ်းငွေ (Change):", "%,d Ks".format(receipt.changeMinor), y, paintBodyBold)
        y += 36f

        drawDashedLine(canvas, y, width)
        y += 30f

        // Footer
        ReceiptStyle.apply(paintHeader, profile.receiptFooterStyle, profile.receiptFooterSize, profile.receiptFooterAlign, 21f)
        val footerX = when (paintHeader.textAlign) { Paint.Align.LEFT -> 20f; Paint.Align.RIGHT -> width - 20f; else -> width / 2f }
        profile.footerMessage.split("\n").forEach { line ->
            if (line.isNotBlank()) {
                canvas.drawText(line.trim(), footerX, y, paintHeader)
                y += paintHeader.textSize + 7f
            }
        }
        y += 10f
        canvas.drawText("*** ကျေးဇူးတင်ပါသည် ***", width / 2f, y, paintHeader)
        y += 40f

        // Trim bitmap to actual rendered height
        val finalHeight = y.toInt().coerceAtMost(totalHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, finalHeight)
    }

    private fun drawDashedLine(canvas: Canvas, y: Float, width: Int) {
        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 2f
        }
        var x = 10f
        while (x < width - 10) {
            canvas.drawLine(x, y, x + 8f, y, paint)
            x += 14f
        }
    }

    private fun drawSolidLine(canvas: Canvas, y: Float, width: Int) {
        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 3f
        }
        canvas.drawLine(10f, y, width - 10f, y, paint)
    }

    private fun drawSummaryRow(canvas: Canvas, label: String, value: String, y: Float, paint: Paint) {
        canvas.drawText(label, 20f, y, paint)
        canvas.drawText(value, 380f, y, paint)
    }

    /**
     * Converts an Android Bitmap into standard ESC/POS raster bit-image command data.
     * 80mm format: 576 dots wide = 72 bytes per row.
     */
    fun convertBitmapToEscPos(bitmap: Bitmap): ByteArray {
        val width = bitmap.width
        val height = bitmap.height
        val widthBytes = (width + 7) / 8 // 72 bytes for 576 width

        val rawBits = ByteArray(widthBytes * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val luminance = (r * 299 + g * 587 + b * 114) / 1000

                // If pixel is dark (black ink)
                if (luminance < 165) {
                    val byteIndex = y * widthBytes + (x / 8)
                    val bitIndex = 7 - (x % 8)
                    rawBits[byteIndex] = (rawBits[byteIndex].toInt() or (1 shl bitIndex)).toByte()
                }
            }
        }

        return EscPos.raster(widthBytes, height, rawBits)
    }

    /**
     * Connects to a paired Bluetooth thermal printer and prints the formatted Burmese receipt.
     */
    @SuppressLint("MissingPermission")
    suspend fun printReceipt(
        device: BluetoothDevice,
        receipt: ReceiptData,
        profile: StoreProfile,
    ): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        var socket: BluetoothSocket? = null
        var outputStream: OutputStream? = null
        try {
            val bitmap = renderReceiptBitmap(receipt, profile)
            val rasterData = convertBitmapToEscPos(bitmap)

            socket = connectWithRetry(device)
            outputStream = socket.outputStream

            // Send Init, Print Bitmap, Feed & Cut Paper, Kick Cash Drawer
            outputStream.write(EscPos.INIT)
            outputStream.write(rasterData)
            outputStream.write(byteArrayOf(0x1B.toByte(), 0x64.toByte(), 0x03.toByte())) // Feed 3 lines
            outputStream.write(EscPos.CUT)
            outputStream.write(EscPos.CASH_DRAWER_KICK)
            outputStream.flush()

            Pair(true, "ဘေလ်ပြေစာကို ${device.name ?: "Printer"} သို့ အောင်မြင်စွာ ပရင့်ထုတ်ပြီးပါပြီ")
        } catch (e: Exception) {
            Pair(false, "Printer ချိတ်ဆက်မှု မအောင်မြင်ပါ: ${e.message}")
        } finally {
            try { outputStream?.close() } catch (_: Exception) {}
            try { socket?.close() } catch (_: Exception) {}
        }
    }
}
