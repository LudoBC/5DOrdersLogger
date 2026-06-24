# 5D Diplomacy Order Log Generator

A simple Java command-line tool that creates a **nicely formatted orders log** from the JSON output provided by the [Oliver Lugg 5D Diplomacy adjudicator](https://github.com/Oliveriver/5d-diplomacy-with-multiverse-time-travel).

The tool reads the adjudicator's JSON results, groups and formats orders by **owner** and **timeline**, and writes them to a Markdown (`.md`) file for easy reading or sharing.

---

## Features

- Reads adjudicator JSON directly from your local filesystem
- Groups orders by:
    1. **Owner**
    2. **Timeline**
- Prints only the **latest phase** per timeline (older phases are omitted)
- Supports all order types:
    - Move
    - Support
    - Hold
    - Convoy
    - Build
    - Disband
- Marks failed orders by italicising
- Outputs clean, human-readable Markdown
