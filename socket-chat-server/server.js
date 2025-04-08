const express = require('express');
const http = require('http');
const { Server } = require('socket.io');

// Khởi tạo Express và HTTP server
const app = express();
const server = http.createServer(app);
const io = new Server(server);

// Phục vụ file tĩnh (HTML, CSS, JS)
app.use(express.static('public'));

// Xử lý kết nối Socket.IO
io.on('connection', (socket) => {
  console.log('User connected:', socket.id);

  // Gửi tin nhắn đến tất cả client
  socket.on('message', (msg) => {
    io.emit('message', { id: socket.id, message: msg });
  });

  // Ngắt kết nối
  socket.on('disconnect', () => {
    console.log('User disconnected:', socket.id);
  });
});

// Chạy server trên port 3000
server.listen(3000, () => {
  console.log('Server đang chạy: http://localhost:3000');
});