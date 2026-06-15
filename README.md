
# Tugasin 📝

Aplikasi manajemen tugas (task manager) berbasis Android yang dibangun menggunakan Jetpack Compose. Aplikasi ini memungkinkan pengguna untuk mengelola tugas sehari-hari dengan fitur tambah, edit, hapus, filter, dan pengurutan berdasarkan prioritas.

---

## Fitur

- **Tambah Task** — Buat task baru dengan judul, deskripsi, dan prioritas
- **Edit Task** — Ubah detail task yang sudah ada
- **Hapus Task** — Hapus task yang tidak diperlukan
- **Mark as Completed** — Tandai task sebagai selesai dengan checkbox
- **Filter Task** — Filter berdasarkan prioritas (High/Medium/Low) dan status (Active/Completed)
- **Sort Task** — Urutkan task berdasarkan prioritas
- **Validasi Input** — Mencegah penyimpanan task tanpa judul

---

## Teknologi

- **Bahasa** : Kotlin
- **UI Framework** : Jetpack Compose
- **Navigasi** : Navigation Compose
- **State Management** : StateFlow + collectAsState
- **Arsitektur** : Repository Pattern
- **Min SDK** : 24 (Android 7.0)
- **Target SDK** : 36

---

## Struktur Project

```
TugasinApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/tugasin/app/
│   │       │   ├── MainActivity.kt
│   │       │   ├── model/
│   │       │   │   └── Task.kt
│   │       │   ├── data/
│   │       │   │   └── TaskRepository.kt
│   │       │   ├── ui/
│   │       │   │   ├── HomeScreen.kt
│   │       │   │   ├── AddTaskScreen.kt
│   │       │   │   ├── EditTaskScreen.kt
│   │       │   │   ├── components/
│   │       │   │   │   ├── TaskItem.kt
│   │       │   │   │   └── FilterSortSheet.kt
│   │       │   │   └── theme/
│   │       │   │       ├── Color.kt
│   │       │   │       ├── Theme.kt
│   │       │   │       └── Type.kt
│   │       │   └── util/
│   │       │       └── PriorityHelper.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── app-debug.apk
```

---

## Cara Menjalankan Aplikasi

### Prasyarat
- Android Studio Hedgehog atau lebih baru
- JDK 11
- Android SDK minimal API 24

### Langkah-langkah

1. Clone repository ini
   ```bash
   git clone https://github.com/Rezza-rmdh/Tugasin.git
   ```

2. Buka project di Android Studio
   ```
   File → Open → pilih folder TugasinApp
   ```

3. Sync Gradle
   ```
   File → Sync Project with Gradle Files
   ```

4. Jalankan aplikasi
   ```
   Run → Run 'app' atau tekan Shift + F10
   ```

## Cara Penggunaan

| Aksi | Cara |
|---|---|
| Tambah task baru | Tap tombol **+** di pojok kanan bawah |
| Edit task | Tap ikon ✏️ pada task yang ingin diubah |
| Hapus task | Tap ikon 🗑️ pada task yang ingin dihapus |
| Selesaikan task | Tap checkbox di sebelah kiri task |
| Filter & Sort | Tap ikon filter di pojok kanan atas |

---

## Automation Testing

Aplikasi ini memiliki project automation testing terpisah menggunakan Appium + Java.

🔗 Repo Automation: [TugasinApp-Automation](https://github.com/Rezza-rmdh/Tugasin-Appium-Testing.git)
