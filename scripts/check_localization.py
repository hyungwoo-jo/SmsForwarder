#!/usr/bin/env python3
"""Validate the Korean Android string resources without external services."""
import json
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BASE = ROOT / "app/src/main/res/values/strings.xml"
KOREAN = ROOT / "app/src/main/res/values-ko/strings.xml"
EXCEPTIONS = ROOT / "docs/localization-exceptions.json"
PRINTF = re.compile(r"%(?:\d+\$)?[-+#, 0]*(?:\d+)?(?:\.\d+)?[a-zA-Z]")
TAG = re.compile(r"\{\{[^{}]+\}\}")
CHINESE = re.compile(r"[\u3400-\u9fff]")


def load(path: Path):
    root = ET.parse(path).getroot()
    result = {}
    for element in root:
        name = element.attrib.get("name")
        if not name:
            continue
        if name in result:
            raise ValueError(f"duplicate resource in {path}: {name}")
        result[name] = (element, "".join(element.itertext()))
    return result


def signature(value: str):
    return sorted(PRINTF.findall(value)), sorted(TAG.findall(value))


def main() -> int:
    if not KOREAN.exists():
        print(f"missing Korean resource file: {KOREAN}", file=sys.stderr)
        return 1
    base = load(BASE)
    korean = load(KOREAN)
    exceptions = json.loads(EXCEPTIONS.read_text(encoding="utf-8"))["copied_values"]
    targets = {name: item for name, item in base.items() if item[0].attrib.get("translatable") != "false"}
    errors = []
    for name, (source, value) in targets.items():
        if name not in korean:
            errors.append(f"missing: {name}")
            continue
        translated_element, translated = korean[name]
        if not translated.strip():
            errors.append(f"empty translation: {name}")
        if translated_element.attrib.get("translatable") == "false":
            errors.append(f"nontranslatable override: {name}")
        if signature(value) != signature(translated):
            errors.append(f"format or template tags differ: {name}")
        if translated == value and name not in exceptions:
            errors.append(f"unexplained copied value: {name}")
        if CHINESE.search(translated):
            print(f"Chinese character candidate: {name}", file=sys.stderr)
    for name, (element, _) in korean.items():
        if name not in base:
            errors.append(f"unknown Korean key: {name}")
        elif base[name][0].attrib.get("translatable") == "false":
            errors.append(f"Korean override of nontranslatable key: {name}")
    if errors:
        print("Korean localization check failed:", file=sys.stderr)
        print("\n".join(errors), file=sys.stderr)
        return 1
    print(f"Korean localization check passed: {len(targets)} translated keys")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
