import subprocess

fg_svg = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 500" width="1024" height="500" fill="none">
  <defs>
    <radialGradient id="bgGlow" cx="30%" cy="50%" r="50%">
      <stop offset="0%" stop-color="#2D7A5D" stop-opacity="0.18"/>
      <stop offset="100%" stop-color="#121316" stop-opacity="0"/>
    </radialGradient>
    <linearGradient id="goldGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#E5B869"/>
      <stop offset="100%" stop-color="#D4A352"/>
    </linearGradient>
  </defs>

  <!-- Solid Obsidian Dark Background -->
  <rect width="1024" height="500" fill="#121316"/>
  <!-- Subtle Radial Emerald Aura -->
  <rect width="1024" height="500" fill="url(#bgGlow)"/>

  <!-- Left Side: 2D Sacred Vector Compass Emblem -->
  <g transform="translate(140, 250)">
    <!-- Outer Rings -->
    <circle cx="0" cy="0" r="140" stroke="#2D7A5D" stroke-width="2" stroke-dasharray="6 8" opacity="0.4"/>
    <circle cx="0" cy="0" r="120" stroke="#FFFFFF" stroke-width="1.5" opacity="0.15"/>

    <!-- 4 Cardinal Compass Needles -->
    <!-- Top Apex (Emerald Qibla Pointer) -->
    <polygon points="0,-160 12,-120 0,-130 -12,-120" fill="#8AD6B4"/>
    <!-- Bottom Needle -->
    <polygon points="0,160 10,130 0,138 -10,130" fill="#FFFFFF" opacity="0.3"/>
    <!-- Left Needle -->
    <polygon points="-160,0 -130,-10 -138,0 -130,10" fill="#FFFFFF" opacity="0.3"/>
    <!-- Right Needle -->
    <polygon points="160,0 130,-10 138,0 130,10" fill="#FFFFFF" opacity="0.3"/>

    <!-- Sacred 2D Kaaba Isometric Cube -->
    <!-- Top Face -->
    <polygon points="0,-70 64,-34 0,2 -64,-34" fill="#FFFFFF"/>
    <!-- Left Face -->
    <polygon points="-64,-34 0,2 0,84 -64,48" fill="#E2E8F0"/>
    <!-- Right Face -->
    <polygon points="0,2 64,-34 64,48 0,84" fill="#CBD5E1"/>

    <!-- Golden Kiswah Ribbon -->
    <polygon points="-64,-18 0,18 0,28 -64,-8" fill="url(#goldGrad)"/>
    <polygon points="0,18 64,-18 64,-8 0,28" fill="#D4A352"/>

    <!-- Upward Beam Pointer -->
    <path d="M 0 -115 L 10 -92 L 0 -98 L -10 -92 Z" fill="#8AD6B4"/>
  </g>

  <!-- Right Side: Clean Typographic Branding -->
  <g transform="translate(430, 0)">
    <!-- Top Badge -->
    <rect x="0" y="115" width="220" height="28" rx="14" fill="#1C2120" stroke="#2D7A5D" stroke-width="1"/>
    <text x="110" y="133" fill="#8AD6B4" font-family="sans-serif" font-size="10" font-weight="600" letter-spacing="2" text-anchor="middle">ARAH KAABAH SUCI</text>

    <!-- App Title -->
    <text x="0" y="210" fill="#FFFFFF" font-family="serif" font-size="52" font-weight="bold" letter-spacing="-1">KiblatSaya</text>

    <!-- Subtitle / Tagline -->
    <text x="0" y="252" fill="#9DA3AE" font-family="sans-serif" font-size="18" font-weight="400">Pencari arah Kiblat yang pantas, tepat &amp; tenang.</text>

    <!-- Feature Tags -->
    <g transform="translate(0, 290)">
      <!-- Pill 1 -->
      <rect x="0" y="0" width="130" height="32" rx="8" fill="#181A1E" stroke="#262A30" stroke-width="1"/>
      <text x="65" y="20" fill="#E2E8F0" font-family="sans-serif" font-size="11" font-weight="500" text-anchor="middle">⚡ Segera Buka</text>

      <!-- Pill 2 -->
      <rect x="142" y="0" width="150" height="32" rx="8" fill="#181A1E" stroke="#262A30" stroke-width="1"/>
      <text x="217" y="20" fill="#E2E8F0" font-family="sans-serif" font-size="11" font-weight="500" text-anchor="middle">🔒 Tanpa Log Masuk</text>

      <!-- Pill 3 -->
      <rect x="304" y="0" width="140" height="32" rx="8" fill="#181A1E" stroke="#262A30" stroke-width="1"/>
      <text x="374" y="20" fill="#E2E8F0" font-family="sans-serif" font-size="11" font-weight="500" text-anchor="middle">📍 Mod Luar Talian</text>
    </g>

    <!-- Company Byline -->
    <g transform="translate(0, 375)">
      <text x="0" y="0" fill="#646A76" font-family="sans-serif" font-size="11" font-weight="600" letter-spacing="2.5">SEBUAH KARYA DARIPADA ASHRAF SYSTEMS</text>
    </g>
  </g>
</svg>
"""

with open("feature_graphic.svg", "w") as f:
    f.write(fg_svg)

subprocess.run(["rsvg-convert", "-w", "1024", "-h", "500", "feature_graphic.svg", "-o", "play_store_assets/feature_graphic_1024x500.png"], check=True)
subprocess.run(["convert", "play_store_assets/feature_graphic_1024x500.png", "play_store_assets/feature_graphic_1024x500.jpg"], check=True)
print("Minimalist 1024x500 Feature Graphic generated successfully!")
