#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   bash list-callable-sub-starts-json.sh [glob] [output-file]
# Examples:
#   bash list-callable-sub-starts-json.sh
#   bash list-callable-sub-starts-json.sh './msgraph-connector/processes/*.p.json'
#   bash list-callable-sub-starts-json.sh './**/*.p.json' docs/callable-sub-starts.json

SKILL_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.."; pwd)"
LIB_DIR="$SKILL_DIR/lib"

if [ ! -d "$LIB_DIR" ] || [ -z "$(ls -A "$LIB_DIR" 2>/dev/null)" ]; then
  echo "First run: downloading Jackson jars..." >&2
  mvn dependency:copy-dependencies \
    -f "$SKILL_DIR/pom.xml" \
    -DoutputDirectory="$LIB_DIR" \
    -Dmdep.includeScope=runtime \
    -q
fi

GLOB_PATTERN="${1:-./**/*.p.json}"
OUTPUT_FILE="${2:-}"

if [[ -n "$OUTPUT_FILE" ]]; then
  java --class-path "$LIB_DIR/*" "$SKILL_DIR/ListCallableSubStarts.java" "$GLOB_PATTERN" "$OUTPUT_FILE" --json
else
  java --class-path "$LIB_DIR/*" "$SKILL_DIR/ListCallableSubStarts.java" "$GLOB_PATTERN" --json
fi
