import os
from PIL import Image

res_path = "app/src/main/res"
densities = {
    "mipmap-mdpi": "48x48",
    "mipmap-hdpi": "72x72",
    "mipmap-xhdpi": "96x96",
    "mipmap-xxhdpi": "144x144",
    "mipmap-xxxhdpi": "192x192"
}

print("| Carpeta | Densidad | Multiplicador | Tamaño esperado (px) | Tamaño real (px) |")
print("|---|-|---|---|---|")

for folder, expected in densities.items():
    img_path = os.path.join(res_path, folder, "ic_launcher.png")
    if os.path.exists(img_path):
        with Image.open(img_path) as img:
            width, height = img.size
            real_size = f"{width}x{height}"
            multiplier = expected.split('x')[0] # This is just to help filling the table in a simplified way if needed
            # Actually multiplier is: mdpi=1x, hdpi=1.5x, xhdpi=2x, xxhdpi=3x, xxxhdpi=4x
            mult_map = {"mipmap-mdpi": "1x", "mipmap-hdpi": "1.5x", "mipmap-xhdpi": "2x", "mipmap-xxhdpi": "3x", "mipmap-xxxhdpi": "4x"}
            print(f"| {folder} | {folder.split('-')[1]} | {mult_map[folder]} | {expected} | {real_size} |")
    else:
        print(f"| {folder} | {folder.split('-')[1]} | - | {expected} | NOT FOUND |")
