package com.nhatnguyenba.chatapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    private lateinit var socket: Socket
    private lateinit var adapter: MessageAdapter
    private lateinit var rvMessages: RecyclerView
    private lateinit var btnSend: Button
    private lateinit var etMessage: EditText
    private val currentUserId by lazy {
        socket.id()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo view
        btnSend = findViewById(R.id.btnSend)
        etMessage = findViewById(R.id.etMessage)

        // Khởi tạo RecyclerView
        adapter = MessageAdapter()
        rvMessages = findViewById(R.id.rvMessages)
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(this)

        // Kết nối Socket.IO
        try {
            socket = IO.socket("http://10.0.2.2:3000") // 10.0.2.2 là localhost cho Android Emulator
            socket.connect()

            // Lắng nghe tin nhắn từ server
            socket.on("message") { args ->
                val data = args[0] as JSONObject
                val senderId = data.getString("id")
                val messageContent = data.getString("message")

                runOnUiThread {
                    Log.d("MainActivity", "current ID: $currentUserId, sender ID: $senderId")
                    if (senderId != currentUserId) {
                        adapter.addMessage(
                            Message(
                                senderId = senderId,
                                content = messageContent,
                                isSent = senderId == currentUserId
                            )
                        )
                    }
                    rvMessages.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        // Gửi tin nhắn
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // Gửi lên server
                socket.emit("message", message)

                // Hiển thị tin nhắn ngay lập tức
                adapter.addMessage(
                    Message(
                        senderId = currentUserId,
                        content = message,
                        isSent = true
                    )
                )
                etMessage.text.clear()
                rvMessages.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect() // Ngắt kết nối khi thoát app
    }
}