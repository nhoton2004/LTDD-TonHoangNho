# UTH SmartTasks

📱 **Mobile App** quản lý công việc đơn giản và hiệu quả được xây dựng bằng **Flutter**.

> ✅ **Đã chuyển đổi hoàn toàn từ React Native sang Flutter!**

## Yêu cầu

- Flutter SDK (>=3.0.0)
- Dart SDK
- Android Studio hoặc VS Code với Flutter extension
- Android SDK (để chạy trên Android)

## Cài đặt

1. **Cài đặt Flutter:**
   - Tải Flutter từ: https://flutter.dev/docs/get-started/install
   - Thêm Flutter vào PATH

2. **Kiểm tra cài đặt:**
   ```bash
   flutter doctor
   ```

3. **Cài đặt dependencies:**
   ```bash
   flutter pub get
   ```

## Chạy ứng dụng

### Chạy trên Android Emulator

1. **Khởi động emulator:**
   - Mở Android Studio
   - Tools → Device Manager
   - Khởi động một emulator

2. **Chạy ứng dụng:**
   ```bash
   flutter run
   ```

### Chạy trên điện thoại thật

1. **Bật USB Debugging** trên điện thoại
2. **Kết nối điện thoại** qua USB
3. **Chạy:**
   ```bash
   flutter run
   ```

### Build APK

```bash
# Build APK debug
flutter build apk

# Build APK release
flutter build apk --release
```

File APK sẽ nằm tại: `build/app/outputs/flutter-apk/app-release.apk`

## Tính năng

### Màn hình Todo List
- Hiển thị danh sách tất cả các tasks
- Gọi API `GET /tasks` để lấy dữ liệu
- Nếu không có dữ liệu, hiển thị EmptyView
- Nếu có dữ liệu, hiển thị danh sách tasks dưới dạng cards
- Pull to refresh để làm mới danh sách
- Click vào task để xem chi tiết

### Màn hình Task Detail
- Hiển thị thông tin chi tiết của task
- Gọi API `GET /task/{id}` khi vào màn hình
- Hiển thị: title, description, category, status, priority, subtasks, attachments
- Nút xóa task (gọi API `DELETE /task/{id}`)
- Xác nhận trước khi xóa

## API Endpoints

- `GET https://amock.io/api/researchUTH/tasks` - Lấy tất cả tasks
- `GET https://amock.io/api/researchUTH/task/{id}` - Lấy chi tiết task
- `DELETE https://amock.io/api/researchUTH/task/{id}` - Xóa task

## Cấu trúc dự án

```
lib/
├── main.dart                 # Entry point
├── models/
│   └── task.dart            # Task model
├── screens/
│   ├── todo_list_screen.dart # Màn hình danh sách tasks
│   └── task_detail_screen.dart # Màn hình chi tiết task
├── widgets/
│   ├── task_card.dart       # Widget hiển thị task card
│   └── empty_view.dart      # Widget hiển thị khi không có tasks
└── services/
    └── api.dart             # Service gọi API
```

## Dependencies

- `http`: ^1.1.0 - Gọi API
- `intl`: ^0.18.1 - Format ngày tháng

## Troubleshooting

### Lỗi: "No devices found"
- Đảm bảo emulator đã khởi động hoàn toàn
- Hoặc kết nối điện thoại với USB debugging enabled

### Lỗi: "Flutter SDK not found"
- Kiểm tra Flutter đã được cài đặt: `flutter doctor`
- Đảm bảo Flutter đã được thêm vào PATH

### Lỗi: "Package not found"
```bash
flutter pub get
```

---

**Lưu ý:** Ứng dụng đã được chuyển đổi hoàn toàn sang Flutter. Code React Native cũ vẫn được giữ lại trong thư mục gốc để tham khảo.
