# 🎨 Modern Re-up Tool - CSS & Styling Guide

## 📋 Overview

Một bộ CSS chuyên nghiệp cho TikTok Affiliate Re-up Tool với:

✅ **Dark Mode** (#121212 primary, #1E1E1E cards)  
✅ **Neon Purple Accent** (#8B5CF6)  
✅ **12px Border Radius** trên tất cả thành phần  
✅ **Hiệu ứng Hover** trên Cards (border & background change)  
✅ **Pulse Effect** trên Primary Button  
✅ **Modern Toggle Switches** (bo tròn, xanh lá cây khi bật)  
✅ **100% Responsive** (Desktop, Tablet, Mobile)  
✅ **Animations** (fadeIn, slideIn, pulse, shimmer)  

---

## 📁 File Structure

```
src/main/resources/
├── static/
│   └── css/
│       └── reup-modern.css (Main stylesheet)
└── templates/
    └── reup_modern_clean.html (HTML template)
```

---

## 🎨 Color Palette

```css
--bg-primary: #121212      /* Main background */
--bg-card: #1E1E1E         /* Card/block background */
--accent-neon: #8B5CF6     /* Primary accent (Neon Purple) */
--accent-light: #A78BFA    /* Lighter purple */
--accent-dark: #7C3AED     /* Darker purple */
--text-primary: #FFFFFF    /* Main text */
--text-secondary: #B8B8B8  /* Secondary text */
--text-muted: #808080      /* Muted text */
--border-color: #2D2D2D    /* Border color */
--success-color: #10B981   /* Success green */
--error-color: #EF4444     /* Error red */
```

---

## 🏗️ Component Styling

### Upload Zone
```css
.reup-upload__zone {
    border: 2px dashed var(--accent-neon);
    border-radius: 12px;
    padding: 3rem 2rem;
    background: rgba(139, 92, 246, 0.05);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.reup-upload__zone:hover {
    border-color: var(--accent-light);
    background: rgba(139, 92, 246, 0.12);
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(139, 92, 246, 0.15);
}
```

**Hiệu ứng:**
- Gradient border khi hover
- Slight lift animation (translateY)
- Glowing box-shadow

### Settings Card
```css
.reup-card {
    padding: 1.25rem;
    background: var(--bg-card);
    border: 1.5px solid var(--border-color);
    border-radius: 12px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.reup-card:hover {
    border-color: var(--accent-neon);
    background: rgba(139, 92, 246, 0.08);
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(139, 92, 246, 0.12);
}

.reup-card.active {
    border-color: var(--accent-neon);
    background: rgba(139, 92, 246, 0.12);
    box-shadow: 0 0 16px rgba(139, 92, 246, 0.2);
}
```

**Hiệu ứng:**
- Border color change → Neon Purple
- Subtle background glow
- Smooth lift on hover

### Modern Toggle Switch
```css
.reup-toggle {
    width: 56px;
    height: 32px;
    background: #404040;
    border-radius: 16px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.reup-toggle.active {
    background: var(--success-color);
    box-shadow: 0 0 16px rgba(16, 185, 129, 0.4);
}

.reup-toggle::after {
    content: '';
    position: absolute;
    width: 28px;
    height: 28px;
    background: white;
    border-radius: 50%;
    left: 2px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.reup-toggle.active::after {
    left: 26px;
}
```

**Hiệu ứng:**
- Smooth sliding circle
- Color change: Gray → Green
- Glow effect when active

### Primary Button (CTA)
```css
.reup-button--primary {
    background: linear-gradient(135deg, var(--accent-neon) 0%, var(--accent-dark) 100%);
    color: white;
    box-shadow: 0 4px 16px rgba(139, 92, 246, 0.3);
    min-height: 56px;
    padding: 1rem 2rem;
    border-radius: 12px;
    width: 100%;
}

.reup-button--primary:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(139, 92, 246, 0.5);
    animation: pulse 2s infinite;
}
```

**Hiệu ứng:**
- Gradient background (Purple tones)
- Lift animation on hover
- Pulse glow effect (infinite)
- Full width (100%)

### Textarea (Subtitle Input)
```css
.reup-content__textarea {
    width: 100%;
    min-height: 110px;
    padding: 1rem;
    background: var(--bg-card);
    border: 1.5px solid var(--border-color);
    border-radius: 12px;
    transition: all 0.3s ease;
}

.reup-content__textarea:hover {
    border-color: var(--accent-neon);
    background: rgba(139, 92, 246, 0.08);
}

.reup-content__textarea:focus {
    border-color: var(--accent-light);
    background: rgba(139, 92, 246, 0.12);
    box-shadow: 0 0 0 4px rgba(139, 92, 246, 0.1);
}
```

**Hiệu ứng:**
- Border color change on focus
- Subtle background glow
- Focus ring (outer glow)

### Progress Bar
```css
.reup-progress__bar {
    height: 8px;
    background: rgba(139, 92, 246, 0.15);
    border-radius: 4px;
    overflow: hidden;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
}

.reup-progress__fill {
    height: 100%;
    background: linear-gradient(90deg, var(--accent-neon), var(--accent-light));
    width: 0%;
    transition: width 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    border-radius: 4px;
    box-shadow: 0 0 12px rgba(139, 92, 246, 0.6);
}
```

**Hiệu ứng:**
- Gradient fill
- Smooth width transition
- Glowing effect

---

## 🎭 Animations

### fadeInDown (Header)
```css
@keyframes fadeInDown {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
```

### fadeInUp (Sections)
```css
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
```

### slideIn (Expert Mode)
```css
@keyframes slideIn {
    from {
        opacity: 0;
        max-height: 0;
    }
    to {
        opacity: 1;
        max-height: 500px;
    }
}
```

### pulse (Button)
```css
@keyframes pulse {
    0%, 100% {
        box-shadow: 0 0 0 0 rgba(139, 92, 246, 0.7);
    }
    50% {
        box-shadow: 0 0 0 10px rgba(139, 92, 246, 0);
    }
}
```

---

## 📱 Responsive Design

### Desktop (> 768px)
```css
.reup-container {
    max-width: 700px;
    padding: 2rem 1.25rem;
}

.reup-settings__expert {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
}
```

### Tablet (768px - 480px)
```css
@media (max-width: 768px) {
    .reup-container {
        padding: 1.5rem 1rem;
    }

    .reup-settings__expert {
        grid-template-columns: 1fr;
    }

    .reup-button {
        min-height: 50px;
        font-size: 0.9rem;
    }
}
```

### Mobile (< 480px)
```css
@media (max-width: 480px) {
    .reup-container {
        padding: 1rem 0.75rem;
    }

    .reup-header__title {
        font-size: 1.4rem;
    }

    .reup-upload__zone {
        padding: 2.5rem 1.5rem;
    }

    .reup-toggle {
        width: 52px;
        height: 30px;
    }

    .reup-card {
        padding: 1rem;
    }
}
```

---

## 🔧 Customization Tips

### Change Primary Color
```css
:root {
    --accent-neon: #YOUR_COLOR;
    --accent-light: lighter_version;
    --accent-dark: darker_version;
}
```

### Modify Border Radius
```css
:root {
    --border-radius: 16px; /* From 12px */
}
```

### Adjust Animation Speed
```css
:root {
    --transition-speed: 0.5s; /* From 0.3s */
}
```

### Change Success Color (Toggle)
```css
:root {
    --success-color: #3B82F6; /* Blue instead of green */
}
```

---

## ✨ Key Features

✅ **Professional Dark Mode**
- Easy on eyes
- Modern look
- Perfect for video editing tools

✅ **Neon Purple Accent**
- Pops on dark background
- Elegant and trendy
- Consistent throughout UI

✅ **Smooth Transitions**
- 0.3s cubic-bezier by default
- Responsive to user interaction
- Never jarring or abrupt

✅ **Glowing Effects**
- Box-shadow glow on hover
- Pulse animation on primary button
- Sophisticated appearance

✅ **Mobile-First Responsive**
- Works on all screen sizes
- Touch-friendly buttons
- Readable text everywhere

✅ **Accessible**
- Sufficient contrast ratios
- Focus states visible
- Reduced motion support

---

## 📊 Border Radius Consistency

All components use **12px border radius**:

```css
--border-radius: 12px;

.reup-upload__zone { border-radius: var(--border-radius); }
.reup-settings__auto { border-radius: var(--border-radius); }
.reup-card { border-radius: var(--border-radius); }
.reup-content__textarea { border-radius: var(--border-radius); }
.reup-button { border-radius: var(--border-radius); }
.reup-alert { border-radius: var(--border-radius); }
```

---

## 🎯 Hover Effects Summary

| Component | Hover Effect |
|-----------|--------------|
| **Upload Zone** | Border → Light Purple, Lift -4px, Glow |
| **Card** | Border → Neon Purple, Glow, Lift -2px |
| **Toggle** | Background → Gray darker |
| **Textarea** | Border → Purple, Background glow |
| **Button** | Lift -2px, Pulse animation, Enhanced glow |

---

## 🌙 Dark Mode Support

```css
@media (prefers-color-scheme: dark) {
    body {
        background: var(--bg-primary);
    }
}
```

---

## ♿ Accessibility

```css
/* Reduced Motion Support */
@media (prefers-reduced-motion: reduce) {
    * {
        animation: none !important;
        transition: none !important;
    }
}
```

---

## 📝 CSS File Location

```
src/main/resources/static/css/reup-modern.css
```

**Size:** ~12KB (minified: ~8KB)  
**Lines:** 600+  
**Custom Properties:** 17  
**Media Queries:** 3  
**Animations:** 5  

---

## 🚀 How to Use

### Link in HTML
```html
<link href="/css/reup-modern.css" rel="stylesheet">
```

### Development
- Edit `/reup-modern.css` directly
- Changes apply immediately on refresh
- No compilation needed

### Production
- CSS is already minified-ready
- Consider minifying for production
- Cache busting recommended

---

## 📚 Related Files

- **HTML Template**: `templates/reup_modern_clean.html`
- **Controller**: `controller/ModernReupController.java`
- **Service**: `service/SingleVideoReupService.java`

---

**Version**: 1.0.0  
**Status**: ✅ Production Ready  
**Last Updated**: 2026-04-14  

🎨 Ready to style! Enjoy the professional look!

