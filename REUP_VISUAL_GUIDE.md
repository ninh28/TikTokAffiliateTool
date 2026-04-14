# 📸 VISUAL GUIDE - Hướng Dẫn Trực Quan

## 🎨 Giao Diện Chi Tiết

### **1. HEADER**
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║          🎬 TikTok Re-up Tool                                   ║
║    Tạo video re-up độc lập, tránh phát hiện sao chép           ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Kiểu**: 
- Font size: 48px (Title), 16px (Subtitle)
- Color: Gradient tím-xanh
- Alignment: Center

---

### **2. KHỐI 1: UPLOAD**
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║                          ☁️                                      ║
║                                                                  ║
║                    Kéo thả video vào đây                        ║
║                                                                  ║
║             Hỗ trợ: MP4, MOV, AVI, MKV (Max 500MB)             ║
║                                                                  ║
║         📹 video_sample.mp4 (125.5 MB)     [✓ Selected]         ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Khi hover/dragover**:
- Border: Thay đổi thành tím neon
- Background: Nhẹ hơn
- Box-shadow: Glow effect

**Khi có file**:
- Hiển thị tên file + dung lượng
- Màu xanh cyan
- Icon check mark

---

### **3. KHỐI 2: SETTINGS**

#### **3a. Auto Mode Switch**
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║  ⚙️ Cấu Hình Re-up                                               ║
║                                                                  ║
║  ┌──────────────────────────────────────────────────────────┐  ║
║  │                                                          │  ║
║  │  🤖 Chế độ Auto Re-up (Khuyên dùng)     ◉───●────     │  ║
║  │                                        [Bật]             │  ║
║  │                                                          │  ║
║  └──────────────────────────────────────────────────────────┘  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Toggle Switch**:
- Hiển thị: ● = Bật (xanh), ○ = Tắt (xám)
- Size: 60px width, 32px height
- Animation: Smooth slide

---

#### **3b. Expert Mode Cards** (Show khi Auto Mode = OFF)
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║  ┌─────────────────────┐  ┌─────────────────────┐             ║
║  │ 🎬 Cắt xén & Zoom   │  │ ⏩ Tăng tốc 1.1x     │             ║
║  │ Trim 1s + Zoom 1.1x │  │ Động lực hơn        │             ║
║  │          [✓] Bật    │  │          [✓] Bật    │             ║
║  └─────────────────────┘  └─────────────────────┘             ║
║                                                                  ║
║  ┌─────────────────────┐  ┌─────────────────────┐             ║
║  │ 🔄 Lật Mirror       │  │ 🔇 Tắt âm thanh    │             ║
║  │ Flip ngang          │  │ Gốc                │             ║
║  │          [ ] Tắt    │  │          [ ] Tắt    │             ║
║  └─────────────────────┘  └─────────────────────┘             ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Card Hover**:
- Border: Thay đổi thành tím
- Background: Sáng hơn
- Box-shadow: Glow effect

**Card Toggle**:
- Kích thước: 48px x 24px
- Color: Xám khi tắt, gradient tím-xanh khi bật
- Animation: Smooth 0.3s

---

### **4. KHỐI 3: CONTENT**
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║  📝 Phụ đề (Tùy chọn)                                            ║
║                                                                  ║
║  ┌────────────────────────────────────────────────────────┐    ║
║  │ Nhập phụ đề hoặc tiêu đề video. Để trống nếu không... │    ║
║  │                                                        │    ║
║  │                                                        │    ║
║  └────────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  Progress: [████████████░░░░░░░░░░░░░░░░░░░░] 40%           ║
║  (Hiệu ứng glow, gradient tím-xanh)                            ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Textarea**:
- Min-height: 100px
- Padding: 16px
- Border radius: 12px
- Focus: Glow effect tím

**Progress Bar**:
- Height: 6px
- Background: #252525
- Fill: Gradient tím-xanh, glow effect
- Show khi xử lý

---

### **5. BUTTONS**
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║              ┌──────────┐          ┌──────────────────────┐    ║
║              │ 🔄 Tải   │          │ ▶️  BẮT ĐẦU RE-UP   │    ║
║              │  lại     │          │                     │    ║
║              └──────────┘          │ (Gradient tím→xanh) │    ║
║                                    │                     │    ║
║                                    │ Full-width button   │    ║
║                                    │ Pulse animation     │    ║
║                                    └──────────────────────┘    ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**"Tải lại" Button**:
- Border: 2px tím
- Text color: Tím
- Hover: Background tím, text trắng
- Size: Nhỏ

**"BẮT ĐẦU RE-UP" Button**:
- Background: Gradient 135° tím→xanh
- Text: White, bold
- Full-width
- Hover: translateY(-2px), box-shadow glow
- Disabled: Opacity 0.5

---

### **6. ALERTS**
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║  SUCCESS:  [✓] ✅ Video được chọn. Nhấn BẮT ĐẦU để xử lý!    ║
║            (Green background, thin border)                      ║
║            (Auto hide sau 5 giây)                               ║
║                                                                  ║
║  ERROR:    [!] ❌ File quá lớn. Max 500MB                      ║
║            (Red background, thin border)                        ║
║                                                                  ║
║  INFO:     [i] ⏳ Đang upload video...                          ║
║            (Blue background, thin border)                       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Animations**:
- Slide down khi appear (0.3s)
- Slide up khi disappear (0.3s)
- Auto remove sau 5s

---

### **7. RESULT SECTION** (Show khi completed)
```
╔══════════════════════════════════════════════════════════════════╗
║                                                                  ║
║                      🎉 Thành công!                             ║
║                                                                  ║
║         Video re-up của bạn đã sẵn sàng để tải xuống           ║
║                                                                  ║
║     ┌────────────────────────────────────────────────┐         ║
║     │  📹 VIDEO PREVIEW                             │         ║
║     │  [▶️] ─────────────●──────────── [⊡]        │         ║
║     │       00:00          01:30        03:00       │         ║
║     └────────────────────────────────────────────────┘         ║
║                                                                  ║
║         ┌──────────────────────────────────┐                  ║
║         │ 📥 Tải video                    │                  ║
║         │ (Gradient button, hover glow)   │                  ║
║         └──────────────────────────────────┘                  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

**Video Player**:
- Max-width: 400px
- Border-radius: 12px
- Border: 1px #2a2a2a
- Controls: Standard HTML5

**Download Button**:
- Gradient tím-xanh
- Hover: translateY(-2px)

---

## 🎬 FLOW DIAGRAM

### **User Journey**
```
START
  │
  ▼
[🎬 UPLOAD] ──► Select/Drag video
  │
  ├─► ❌ Invalid format? → Show error
  │
  ├─► ❌ File too large? → Show error
  │
  └─► ✅ Valid video
       │
       ▼
   [⚙️ SETTINGS]
       │
       ├─► Auto Mode ON (default)
       │   ├─ Crop: ON
       │   ├─ Speed: ON
       │   ├─ Mirror: OFF
       │   └─ Audio: OFF
       │
       ├─► Auto Mode OFF (Expert)
       │   ├─ Toggle Crop
       │   ├─ Toggle Speed
       │   ├─ Toggle Mirror
       │   └─ Toggle Audio
       │
       ▼
   [📝 CONTENT]
       ├─ Add subtitle (optional)
       │
       ▼
   [▶️ START] Button Click
       │
       ▼
   [📤 UPLOAD]
       ├─ Validate file
       ├─ Save to temp
       ├─ Create job
       │
       ▼
   [⏳ PROCESSING]
       ├─ Show progress: 0-100%
       ├─ Poll every 2 seconds
       ├─ Update progress bar
       │
       ├─ Status: PROCESSING → 100%
       │
       ▼
   [✅ COMPLETED]
       ├─ Show result section
       ├─ Display video preview
       ├─ Show download button
       │
       ▼
   [📥 DOWNLOAD]
       ├─ Click "Tải video"
       ├─ Browser downloads file
       │
       ▼
    END ✅
```

---

## 🎨 COLOR SCHEME

### **Primary Colors**
```
Background:    #121212 (Deep Black)
Card BG:       #1E1E1E, #252525
Text Primary:  #FFFFFF (White)
Text Secondary: #808080 (Gray)
```

### **Accent Colors**
```
Accent 1:      #8B5CF6 (Neon Purple)
Accent 2:      #06B6D4 (Cyan)
Success:       #22c55e (Green)
Error:         #ef4444 (Red)
Warning:       #f59e0b (Orange)
Info:          #3b82f6 (Blue)
```

### **Gradients**
```
Primary:       linear-gradient(135deg, #8B5CF6, #06B6D4)
Hover:         rgba(139, 92, 246, 0.1)
Glow:          0 0 30px rgba(139, 92, 246, 0.1)
```

---

## 📱 RESPONSIVE BREAKPOINTS

### **Desktop (> 768px)**
```
Layout: 3-column grid for settings
Cards: Spacious (200px+ width)
Text: Full size
Buttons: Normal size
```

### **Tablet (500px - 768px)**
```
Layout: 2-column grid for settings
Cards: Medium
Text: Slightly smaller
Buttons: Normal
```

### **Mobile (< 500px)**
```
Layout: 1-column stack
Cards: Full-width
Text: Optimized
Buttons: Touch-friendly
Upload zone: Taller
```

---

## ⏱️ ANIMATION SPECS

### **Hover Effects**
```
Duration: 0.3s ease
Effects:
  - Border color change
  - Background change
  - Box-shadow glow
  - Transform: translateY(-2px)
```

### **Transitions**
```
Progress bar fill: Smooth 0.3s
Toggle switch: 0.3s ease
Card toggle: 0.3s ease
Alert: slideDown 0.3s ease
```

### **Keyframe Animations**
```
Float (icon): 3s ease-in-out infinite
  - 0%, 100%: translateY(0px)
  - 50%: translateY(-10px)

Spin (loader): 1s linear infinite
  - rotate(0deg) → rotate(360deg)

Pulse (button): Custom glow
  - Box-shadow pulsing effect
```

---

## 🎯 User Actions & Feedback

### **Upload**
```
Click/Drag → Highlight zone → Validate → Success alert → Enable button
```

### **Toggle Auto Mode**
```
Click → Toggle state → Smooth animation → Show/hide expert cards
```

### **Card Selection**
```
Click → Toggle state → Update button state → Color change
```

### **Processing**
```
Click Start → Disable button → Show progress → Poll server → Complete
```

### **Download**
```
Click Download → Browser download → File saved → Notify user
```

---

## 📊 Progress Bar Animation

```
Initial: ════════════════════════════ 0%
  ▼ (2s)
Processing: ████░░░░░░░░░░░░░░░░░░░░ 15%
  ▼ (2s)
          ████████░░░░░░░░░░░░░░░░░░░ 35%
  ▼ (2s)
          ████████████░░░░░░░░░░░░░░ 60%
  ▼ (2s)
          ██████████████████░░░░░░░░░ 85%
  ▼ (2s)
Completed: ████████████████████████████ 100% ✅
```

**Gradient Fill**: Tím (#8B5CF6) → Xanh (#06B6D4)  
**Glow Effect**: Box-shadow pulse

---

## 🌙 Dark Mode Implementation

```css
/* All components use dark palette */
body {
  background: #121212;
  color: #FFFFFF;
}

.card {
  background: #1E1E1E;
  border: 1px solid #2a2a2a;
}

.button:hover {
  box-shadow: 0 0 30px rgba(139, 92, 246, 0.3);
}

/* No light mode - full dark experience */
```

---

**Visual Guide Version**: 2.0  
**Last Updated**: 2026-04-14  
**Theme**: Dark Mode Modern

