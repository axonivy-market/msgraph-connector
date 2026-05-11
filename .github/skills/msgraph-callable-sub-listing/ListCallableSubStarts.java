import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lists CALLABLE_SUB process elements that have a CallSubStart tagged "connector".
 *
 * Usage:
 *   java --class-path "lib/*" ListCallableSubStarts.java [<glob>] [<output-file>] [--json]
 *
 * Defaults:
 *   glob        ./** /*.p.json
 *   output-file stdout
 *   format      Markdown (pass --json for JSON)
 */
public class ListCallableSubStarts {

    static final ObjectMapper MAPPER = new ObjectMapper();

    // ── domain records ────────────────────────────────────────────────────────

    record Param(String name, String type, String desc) {}

    record ParamList(List<Param> params) {}

    record ConnectorStart(String id, String name, String signature,
                          List<String> tags, ParamList input, ParamList result) {}

    record ProcessEntry(String file, List<ConnectorStart> starts) {}

    // ── main ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        String glob = "./**/*.p.json";
        String outputFile = null;
        boolean jsonMode = false;
        int positional = 0;

        for (String arg : args) {
            if ("--json".equals(arg)) {
                jsonMode = true;
            } else if (positional == 0) {
                glob = arg;
                positional++;
            } else if (positional == 1) {
                outputFile = arg;
                positional++;
            }
        }

        List<Path> files = findFiles(glob);
        if (files.isEmpty()) {
            System.err.println("No process files matched: " + glob);
            System.exit(1);
        }

        String result = jsonMode ? renderJson(files, glob) : renderMarkdown(files);

        if (outputFile != null) {
            Path out = Path.of(outputFile);
            Path parent = out.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(out, result);
            System.err.println("Written to " + outputFile);
        } else {
            System.out.print(result);
        }
    }

    // ── file discovery ────────────────────────────────────────────────────────

    static List<Path> findFiles(String glob) throws IOException {
        // Strip leading "./" so Java's glob: syntax can match against relative paths.
        String normalised = glob.startsWith("./") ? glob.substring(2) : glob;
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + normalised);
        Path base = Path.of("").toAbsolutePath();
        return Files.walk(base)
                .filter(Files::isRegularFile)
                .filter(p -> matcher.matches(base.relativize(p)))
                .sorted()
                .collect(Collectors.toList());
    }

    // ── extraction ────────────────────────────────────────────────────────────

    static List<ProcessEntry> extractEntries(List<Path> files) throws IOException {
        Path base = Path.of("").toAbsolutePath();
        List<ProcessEntry> entries = new ArrayList<>();
        for (Path file : files) {
            JsonNode root = MAPPER.readTree(file.toFile());
            if (!"CALLABLE_SUB".equals(root.path("kind").asText(""))) continue;

            List<ConnectorStart> starts = new ArrayList<>();
            for (JsonNode el : iterableOf(root.path("elements"))) {
                if (!"CallSubStart".equals(el.path("type").asText())) continue;
                if (!hasConnectorTag(el)) continue;

                JsonNode cfg = el.path("config");
                JsonNode inputNode = cfg.path("input").isMissingNode()
                        ? cfg.path("parameter")
                        : cfg.path("input");

                starts.add(new ConnectorStart(
                        nullableText(el, "id"),
                        nullableText(el, "name"),
                        nullableText(cfg, "signature"),
                        tagList(el),
                        extractParamList(inputNode),
                        extractParamList(cfg.path("result"))
                ));
            }

            String displayPath = "./" + base.relativize(file).toString().replace('\\', '/');
            entries.add(new ProcessEntry(displayPath, starts));
        }
        return entries;
    }

    static boolean hasConnectorTag(JsonNode el) {
        for (JsonNode tag : iterableOf(el.path("tags"))) {
            if ("connector".equalsIgnoreCase(tag.asText())) return true;
        }
        return false;
    }

    static List<String> tagList(JsonNode el) {
        List<String> result = new ArrayList<>();
        for (JsonNode tag : iterableOf(el.path("tags"))) result.add(tag.asText());
        return result;
    }

    static ParamList extractParamList(JsonNode node) {
        if (node.isMissingNode() || node.isNull()) return null;
        List<Param> params = new ArrayList<>();
        for (JsonNode p : iterableOf(node.path("params"))) {
            params.add(new Param(
                    p.path("name").asText(null),
                    p.path("type").asText(null),
                    p.path("desc").asText(null)));
        }
        return new ParamList(params);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    static Iterable<JsonNode> iterableOf(JsonNode node) {
        if (node.isMissingNode() || node.isNull()) return Collections.emptyList();
        return node;
    }

    static String nullableText(JsonNode node, String field) {
        JsonNode v = node.path(field);
        return (v.isMissingNode() || v.isNull()) ? null : v.asText();
    }

    static String paramString(ParamList pl) {
        if (pl == null || pl.params().isEmpty()) return "none";
        return pl.params().stream()
                .map(p -> (p.name() != null ? p.name() : "") + ": " + (p.type() != null ? p.type() : ""))
                .collect(Collectors.joining(", "));
    }

    // ── Markdown output ───────────────────────────────────────────────────────

    static String renderMarkdown(List<Path> files) throws IOException {
        List<ProcessEntry> entries = extractEntries(files);
        StringBuilder sb = new StringBuilder();
        sb.append("# Callable Sub Connector Starts\n\n");

        if (entries.isEmpty()) {
            sb.append("No CALLABLE_SUB process files found.\n");
            return sb.toString();
        }

        for (ProcessEntry entry : entries) {
            sb.append("## ").append(entry.file()).append("\n\n");
            if (entry.starts().isEmpty()) {
                sb.append("- No CallSubStart with tag connector\n\n");
                continue;
            }
            for (ConnectorStart s : entry.starts()) {
                sb.append("- Signature: ").append(s.signature() != null ? s.signature() : "").append("\n");
                sb.append("  Input: ").append(paramString(s.input())).append("\n");
                sb.append("  Result: ").append(paramString(s.result())).append("\n\n");
            }
        }
        return sb.toString();
    }

    // ── JSON output ───────────────────────────────────────────────────────────

    static String renderJson(List<Path> files, String glob) throws IOException {
        List<ProcessEntry> entries = extractEntries(files);
        String generatedAt = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        ArrayNode processes = MAPPER.createArrayNode();
        for (ProcessEntry entry : entries) {
            ObjectNode pNode = MAPPER.createObjectNode();
            pNode.put("file", entry.file());
            ArrayNode startsArr = pNode.putArray("starts");
            for (ConnectorStart s : entry.starts()) {
                ObjectNode sNode = MAPPER.createObjectNode();
                sNode.put("id", s.id());
                sNode.put("name", s.name());
                sNode.put("signature", s.signature());
                ArrayNode tagsArr = sNode.putArray("tags");
                s.tags().forEach(tagsArr::add);
                sNode.set("input", paramListToNode(s.input()));
                sNode.set("result", paramListToNode(s.result()));
                startsArr.add(sNode);
            }
            processes.add(pNode);
        }

        ObjectNode root = MAPPER.createObjectNode();
        root.put("generatedAt", generatedAt);
        root.put("glob", glob);
        root.set("processes", processes);
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root) + "\n";
    }

    static JsonNode paramListToNode(ParamList pl) {
        if (pl == null) return MAPPER.nullNode();
        ObjectNode node = MAPPER.createObjectNode();
        ArrayNode paramsArr = node.putArray("params");
        for (Param p : pl.params()) {
            ObjectNode pn = MAPPER.createObjectNode();
            pn.put("name", p.name());
            pn.put("type", p.type());
            pn.put("desc", p.desc());
            paramsArr.add(pn);
        }
        return node;
    }
}
