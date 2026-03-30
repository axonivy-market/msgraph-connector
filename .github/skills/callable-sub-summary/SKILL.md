---
name: callable-sub-summary
description: 'give me a marketing summary. what features can I expect from these callable subs?.'
argument-hint: '[optional: path-glob for process files]'
user-invocable: true
---

# Callable Sub Summary

Generate a concise, marketing-oriented summary of connector capabilities from process files where:
- process kind is CALLABLE_SUB
- start element type is CallSubStart
- tags contains connector

The summary must be grouped by what a workflow team can actually do with the callable subs.

## Inputs
- Optional file glob argument. Default: ./**/*.p.json
- Optional prose output file path. Default: stdout

## Procedure
1. Run the prose summary script (it reuses the JSON extractor internally):
   - bash ./.github/skills/callable-sub-summary/scripts/list-callable-sub-starts-json.sh
2. For file output, run:
   - bash ./.github/skills/callable-sub-summary/scripts/list-callable-sub-starts-json.sh './**/*.p.json' docs/callable-sub-capabilities-summary.txt
3. Inspect the generated callable-sub capability output and summarize it in marketing language.

## Output Rules

When asked for a summary such as "give me a marketing summary" or "what features can I expect", produce this format:

- Intro sentence: one concise value statement.
- Section heading: `What you get`
- 3-6 bullets, each describing concrete workflow capabilities (prefer grouping by domain when present: calendar, mail, files, chat, todo).
- Optional final sentence: clearly visible coverage limits only (for example input-only or result-only starts).

Additional requirements:
- Keep the whole response concise (target 4-8 lines total).
- Focus on signatures and typed input/result params as proof of capability.
- Use marketing language, but keep claims grounded in detected starts only.
- Do not paste raw JSON in the final summary.

See [summary reference](./references/output-format.md).
