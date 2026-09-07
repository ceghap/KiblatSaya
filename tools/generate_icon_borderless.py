import subprocess
import os

svg_content = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" fill="none">
  <!-- KiblatSaya Official App Icon (Pure Borderless Obsidian + 2D Vector Sacred Compass) -->
  <rect width="512" height="512" rx="128" fill="#121316"/>
  
  <!-- Outer Compass Ring Subtle -->
  <circle cx="256" cy="256" r="160" stroke="#2D7A5D" stroke-width="3" stroke-dasharray="6 10" opacity="0.6"/>
  <circle cx="256" cy="256" r="142" stroke="#FFFFFF" stroke-width="2" opacity="0.2"/>

  <!-- 4 Cardinal Compass Diamonds -->
  <!-- Top Qibla Apex Indicator (Sacred Emerald) -->
  <polygon points="256,76 270,116 256,108 242,116" fill="#8AD6B4"/>
  <!-- Bottom Needle Tick -->
  <polygon points="256,436 266,404 256,410 246,404" fill="#FFFFFF" opacity="0.4"/>
  <!-- Left Needle Tick -->
  <polygon points="76,256 116,242 108,256 116,270" fill="#FFFFFF" opacity="0.4"/>
  <!-- Right Needle Tick -->
  <polygon points="436,256 396,242 404,256 396,270" fill="#FFFFFF" opacity="0.4"/>

  <!-- Master Center Symbol: 2D Sacred Kaaba Cube + Directional Compass Dial -->
  <!-- Top Facet (White/Light Silver) -->
  <polygon points="256,150 336,196 256,242 176,196" fill="#FFFFFF"/>
  <!-- Left Facet (Deep Contrast Dark) -->
  <polygon points="176,196 256,242 256,346 176,300" fill="#E2E8F0"/>
  <!-- Right Facet (Midtone Silver Shadow) -->
  <polygon points="256,242 336,196 336,300 256,346" fill="#CBD5E1"/>

  <!-- Golden Kiswah Belt (Sacred Gold Ribbon) -->
  <!-- Left Kiswah -->
  <polygon points="176,216 256,262 256,276 176,230" fill="#E5B869"/>
  <!-- Right Kiswah -->
  <polygon points="256,262 336,216 336,230 256,276" fill="#D4A352"/>

  <!-- Qibla Direction Beam Apex (Upward Pointer Arrow from Kaaba) -->
  <path d="M 256 118 L 268 142 L 256 136 L 244 142 Z" fill="#8AD6B4"/>

  <!-- Center Pivot Core -->
  <circle cx="256" cy="196" r="5" fill="#121316"/>
</svg>
"""

with open("kiblatsaya_icon.svg", "w") as f:
    f.write(svg_content)

# Convert to 512x512 PNG using rsvg-convert
subprocess.run(["rsvg-convert", "-w", "512", "-h", "512", "kiblatsaya_icon.svg", "-o", "play_store_assets/icon_512x512.png"], check=True)
subprocess.run(["cp", "play_store_assets/icon_512x512.png", "composeApp/src/commonMain/composeResources/drawable/app_logo.png"], check=True)

# Generate Android launcher mipmap icons
res_map = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

for folder, size in res_map.items():
    out_dir = f"composeApp/src/androidMain/res/{folder}"
    os.makedirs(out_dir, exist_ok=True)
    subprocess.run(["rsvg-convert", "-w", str(size), "-h", str(size), "kiblatsaya_icon.svg", "-o", f"{out_dir}/ic_launcher.png"], check=True)
    subprocess.run(["rsvg-convert", "-w", str(size), "-h", str(size), "kiblatsaya_icon.svg", "-o", f"{out_dir}/ic_launcher_round.png"], check=True)

print("Borderless 2D icon generated successfully across all resolutions!")
