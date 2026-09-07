import os
from PIL import Image, ImageDraw, ImageFilter, ImageFont

ASSETS_DIR = "/home/ceghap/codes/KiblatSaya/play_store_assets"
SHOTS_DIR = os.path.join(ASSETS_DIR, "screenshots")
SHOWCASE_DIR = os.path.join(ASSETS_DIR, "showcase_screenshots")
os.makedirs(SHOWCASE_DIR, exist_ok=True)

serif_bold_path = "/usr/share/fonts/TTF/DejaVuSerif-Bold.ttf"
sans_bold_path = "/usr/share/fonts/TTF/DejaVuSans-Bold.ttf"
sans_path = "/usr/share/fonts/TTF/DejaVuSans.ttf"

font_badge = ImageFont.truetype(sans_bold_path, 30)
font_headline = ImageFont.truetype(serif_bold_path, 64)
font_subheadline = ImageFont.truetype(sans_path, 34)

configs = [
    {
        "filename": "showcase_1_kompas_kiblat.png",
        "badge": "ACCURATE QIBLA COMPASS",
        "headline": "Direction to the Holy Kaaba",
        "subheadline": "Instant launch with real-time GPS precision",
        "source": "screenshot_1_kompas_kiblat.png"
    },
    {
        "filename": "showcase_2_panduan_arah.png",
        "badge": "GLOBAL & OFFLINE CITIES",
        "headline": "Automatic GPS & Offline Cities",
        "subheadline": "Preloaded regional database for seamless offline use",
        "source": "screenshot_2_panduan_arah.png"
    },
    {
        "filename": "showcase_3_tema_spiritual.png",
        "badge": "SPIRITUAL THEMES & MODES",
        "headline": "4 Sacred Palettes & Display Modes",
        "subheadline": "Emerald, Lapis, Gold & Rose with Dark and Light modes",
        "source": "screenshot_3_tema_spiritual.png"
    }
]

for cfg in configs:
    width, height = 1080, 2340
    # Obsidian Base Canvas
    card = Image.new("RGBA", (width, height), (18, 19, 22, 255))
    
    # Radiant ambient emerald aura in background
    glow = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    gdraw = ImageDraw.Draw(glow)
    for r in range(650, 0, -8):
        alpha = int(45 * ((1.0 - r / 650) ** 2))
        gdraw.ellipse([540 - r, 1250 - r, 540 + r, 1250 + r], fill=(45, 122, 93, alpha))
    card = Image.alpha_composite(card, glow)
    
    # 1. Header: Badge Pill
    badge_txt = f"✦ {cfg['badge']}"
    bbox = font_badge.getbbox(badge_txt)
    bw, bh = bbox[2] - bbox[0], bbox[3] - bbox[1]
    bx, by = (width - bw) // 2, 90
    
    badge_layer = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    bdraw = ImageDraw.Draw(badge_layer)
    bdraw.rounded_rectangle(
        [bx - 20, by - 10, bx + bw + 20, by + bh + 10],
        radius=20,
        fill=(30, 95, 72, 100),
        outline=(138, 214, 180, 180),
        width=2
    )
    bdraw.text((bx, by - 2), badge_txt, font=font_badge, fill=(138, 214, 180, 255))
    card = Image.alpha_composite(card, badge_layer)
    
    # 2. Header: Headline & Subheadline
    tdraw = ImageDraw.Draw(card)
    head_bbox = font_headline.getbbox(cfg['headline'])
    hw = head_bbox[2] - head_bbox[0]
    tdraw.text(((width - hw) // 2, 175), cfg['headline'], font=font_headline, fill=(244, 245, 246, 255))
    
    sub_bbox = font_subheadline.getbbox(cfg['subheadline'])
    sw = sub_bbox[2] - sub_bbox[0]
    tdraw.text(((width - sw) // 2, 275), cfg['subheadline'], font=font_subheadline, fill=(157, 163, 174, 255))
    
    # 3. Device Mockup Frame
    src_path = os.path.join(SHOTS_DIR, cfg['source'])
    src_img = Image.open(src_path).convert("RGBA")
    
    dev_target_w = 840
    scale = dev_target_w / src_img.width
    dev_target_h = int(src_img.height * scale)
    src_scaled = src_img.resize((dev_target_w, dev_target_h), Image.Resampling.LANCZOS)
    
    border = 14
    full_w = dev_target_w + border * 2
    full_h = dev_target_h + border * 2
    radius = 56
    
    mask = Image.new("L", (dev_target_w, dev_target_h), 0)
    ImageDraw.Draw(mask).rounded_rectangle([0, 0, dev_target_w, dev_target_h], radius=radius - 8, fill=255)
    
    frame = Image.new("RGBA", (full_w, full_h), (0, 0, 0, 0))
    fdraw = ImageDraw.Draw(frame)
    fdraw.rounded_rectangle([0, 0, full_w, full_h], radius=radius, fill=(32, 35, 41, 255), outline=(45, 122, 93, 190), width=3)
    frame.paste(src_scaled, (border, border), mask=mask)
    
    # Deep ambient drop shadow behind phone
    shadow = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    pos_x = (width - full_w) // 2
    pos_y = 375
    shadow.paste((0, 0, 0, 220), (pos_x, pos_y + 28), mask=frame)
    shadow = shadow.filter(ImageFilter.GaussianBlur(36))
    card = Image.alpha_composite(card, shadow)
    card.paste(frame, (pos_x, pos_y), mask=frame)
    
    out_file = os.path.join(SHOWCASE_DIR, cfg['filename'])
    card.save(out_file, "PNG", optimize=True)
    print(f"Showcase banner created: {out_file} ({os.path.getsize(out_file)} bytes)")

print("English showcase assets generated successfully!")
