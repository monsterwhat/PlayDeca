# Build Status — PlayDeca

**Status: BUILD SUCCESS** (verified 2026-10-01, **JDK 25**, Maven 3.9.12)

## Current Version Set

| Component | Version | Note |
| --- | --- | --- |
| Java (compile target) | **25** | `maven.compiler.release`; class files major version 69 |
| `io.quarkus.platform:quarkus-bom` | **3.40.1** | was 3.32.1 |
| `org.apache.myfaces.core.extensions.quarkus:myfaces-quarkus` | **4.1.4** | unchanged — current release |
| `io.quarkiverse.primefaces:quarkus-primefaces` | **4.16.0** | was 4.15.13 |
| `org.primefaces:primefaces` | **16.0.0** | was 15.0.13 (pulled in by the extension) |
| Lombok | 1.18.42 | unchanged |

## Java 25 + Quarkus 3.40.1 Upgrade (2026-10-01)

Upgraded the JDK target 21 → 25 and Quarkus 3.32.1 → 3.40.1. **No fallback to 3.39.5 was
needed** — 3.40.1 resolved and built cleanly on the first attempt.

```diff
-        <maven.compiler.release>21</maven.compiler.release>
+        <maven.compiler.release>25</maven.compiler.release>
-        <quarkus.version>3.32.1</quarkus.version>
+        <quarkus.version>3.40.1</quarkus.version>
-        <primefaces-quarkus.version>4.15.13</primefaces-quarkus.version>
+        <primefaces-quarkus.version>4.16.0</primefaces-quarkus.version>
```

### Also fixed: hardcoded dependency versions

Eight `io.quarkus` dependencies had the version **hardcoded to `3.32.1`** instead of referencing
`${quarkus.version}`. Bumping only the property would have left the graph on 3.32.1 while the BOM
and the `quarkus-maven-plugin` moved to 3.40.1 — a split-version build. All eight now use
`${quarkus.version}`:

`quarkus-rest`, `quarkus-rest-jackson`, `quarkus-hibernate-orm-panache`, `quarkus-jdbc-h2`,
`quarkus-elytron-security`, `quarkus-smallrye-jwt`, `quarkus-mailer`, `quarkus-scheduler`.

The `quarkus-maven-plugin` already used `${quarkus.version}`, so plugin and BOM stay in lockstep.

### Face-stack version selection

Neither MyFaces nor PrimeFaces is managed by `quarkus-bom` (verified: zero `myfaces`/`primefaces`
entries in `quarkus-bom-3.40.1.pom`), so both stay pinned explicitly in `<properties>`.

- **myfaces-quarkus 4.1.4 kept as-is.** 4.1.4 is the latest release and already carries the
  MYFACES-4735 null-guard described below. It was built against Quarkus 3.27.1, but Quarkus
  extensions are expected to run on newer platforms; augmentation completed without error.
- **quarkus-primefaces 4.15.13 → 4.16.0.** The previous pin predated the upgrade. 4.16.0 is the
  current release (built against Quarkus 3.33.1) and moves PrimeFaces 15.0.13 → 16.0.0.

### Verification

```
$ JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64 mvn -DskipTests clean package -B
[INFO] Compiling 35 source files with javac [debug release 25] to target/classes
[INFO] Building jar: /home/alvaro/Github/PlayDeca/PlayDeca/target/PlayDeca.jar
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 6668ms
[INFO] BUILD SUCCESS
[INFO] Total time:  01:42 min
```

Bytecode target confirmed as Java 25, not just the compiler flag:

```
$ javap -v target/classes/com/playdeca/utils/ServerInfo.class | grep major
  major version: 69        # 69 = Java 25 (65 would be Java 21)
```

Smoke run on JDK 25 — clean startup, **zero** exceptions/errors/warnings in the log:

```
[INFO] [org.apache.myfaces.webapp.FacesInitializerImpl] (main) MyFaces Core has started, it took [554] ms.
[INFO] [org.primefaces.webapp.PostConstructApplicationEventListener] (main) Running on PrimeFaces 16.0.0
[INFO] [io.quarkus] (main) Playdeca 1.0-SNAPSHOT on JVM (powered by Quarkus 3.40.1) started in 4.682s. Listening on: http://0.0.0.0:8080
[INFO] [io.quarkus] (main) Profile prod activated.
```

| Path | Status |
| --- | --- |
| `/` | 302 → `/index` |
| `/index` | 200 |
| `/forums` | 200 |
| `/login` | 200 |

Rendering confirmed beyond status codes: `/forums` returns the expected
`<title>PlayDeca - Community Forums | Minecraft Discussion</title>`, and `/index` emits PrimeFaces
JSF resources, so the view layer initialises correctly on PrimeFaces 16.

## Prior Fix — MyFaces NPE (MYFACES-4735)

Kept for context; still the reason `myfaces.version` is pinned rather than left to the BOM.

`mvn -DskipTests package` previously failed during Quarkus augmentation with a `NullPointerException`
in the MyFaces Quarkus extension:

```
[error]: Build step org.apache.myfaces.core.extensions.quarkus.deployment.MyFacesProcessor#buildServlet
         threw an exception: java.lang.NullPointerException: Cannot invoke "java.util.List.stream()"
         because the return value of "org.jboss.metadata.web.spec.WebMetaData.getContextParams()" is null
```

### Why `getContextParams()` was null

1. This project declares **no `META-INF/web.xml`**. Its descriptors live in the *wrong place*:
   `src/main/resources/META-INF/resources/WEB-INF/web.xml` and `.../WEB-INF/faces-config.xml`.
2. `META-INF/resources/` is Quarkus's **static HTTP resource root**, not the descriptor location.
   Files placed there are served as static web content, so Quarkus never parses them as descriptors.
3. Confirmed in Quarkus 3.32.1 `WebXmlParsingBuildStep` bytecode — the only two paths scanned are
   `META-INF/web.xml` and `META-INF/web-fragment.xml`.
4. With no descriptor parsed, `createWebMetadata` produces a `new WebMetaData()` whose
   `contextParams` field is `null` (in `WebCommonMetaData`, `contextParams` is a
   `List<ParamValueMetaData>` left uninitialised by the constructor).
5. MyFaces 4.1.2 `buildServlet` called `webMetaData.getContextParams().stream()` **unguarded** → NPE.

Verified by decompiling both versions. 4.1.2 (offset 6-11) has no null check; 4.1.4 (offset 14)
emits `ifnull 39` before the `.stream()` call. Matches upstream
[MYFACES-4735](https://issues.apache.org/jira/browse/MYFACES-4735), fixed in 4.1.3 / 4.1.4.

### Alternative considered and rejected

Relocating `web.xml` to `src/main/resources/META-INF/web.xml` also makes the build pass, but it was
**rejected** — it applies `<transport-guarantee>CONFIDENTIAL</transport-guarantee>` on `/*`, and
Undertow has no HTTPS listener configured, so every HTTP request fails at runtime with:

```
java.lang.NullPointerException: Cannot invoke
"io.undertow.servlet.api.ConfidentialPortManager.getConfidentialPort(...)" because "this.portManager" is null
```

Verified experimentally: with the relocated descriptor, `/` returns **500** instead of **302**.
Version-pinning keeps runtime behaviour unchanged.

## Jar Paths

- Application jar: `target/PlayDeca.jar` (~14 MB)
- **Runnable** entry point: `target/quarkus-app/quarkus-run.jar`

Note: the POM uses `<packaging>jar</packaging>`, not Quarkus's default `quarkus-app`, so
`target/PlayDeca.jar` has no `Main-Class` and running it directly fails with
`no main manifest attribute`. Use `quarkus-run.jar`.

## How to Run

```bash
cd /home/alvaro/Github/PlayDeca/PlayDeca

# Run (JDK 25)
java -jar target/quarkus-app/quarkus-run.jar

# Dev mode (live reload on http://localhost:8080)
JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64 ./mvnw quarkus:dev
```

Then open <http://localhost:8080> — it redirects to `/index`.

JDK 25 lives at `/usr/lib/jvm/java-25-openjdk-amd64`. Set `JAVA_HOME` explicitly so Maven and any
`./mvnw` invocation compile for 25.

## Note: pre-existing issues (NOT changed — outside scope of these upgrades)

Observed while diagnosing; **left as-is**. Flagged because they are likely unintended.

1. **Descriptors are inert.** `META-INF/resources/WEB-INF/web.xml`, `faces-config.xml`, and
   `beans.xml` are served as static resources rather than parsed. So the following declared in
   `web.xml` are **not actually in effect**:
   - `SecurityHeadersFilter` — responses carry no `X-Frame-Options`, `X-Content-Type-Options`,
     `Strict-Transport-Security`, or `Content-Security-Policy` (confirmed absent at runtime).
   - 404 error page, session config, and the `CONFIDENTIAL` transport guarantee.

   To activate them, move the descriptors to `src/main/resources/META-INF/web.xml` and
   `src/main/resources/META-INF/faces-config.xml` — but see the runtime breakage above: the
   transport guarantee must be removed or an HTTPS listener configured first. Alternatively,
   register the filter programmatically via `@WebFilter`, and port the error page to a
   `quarkus.http.*` exception mapper.

2. **`faces-config.xml` is empty** (3.0 schema) and the stray `faces-config.NavData` at the repo
   root is not a recognised file.

3. `application.properties` sets `org.apache.myfaces.*` keys, but MyFaces web params are not read
   from `application.properties` in a Quarkus build — they belong in a real `web.xml` /
   `web-fragment.xml` descriptor or as `quarkus.<property>` mappings in the extension.

4. **PrimeFaces 15 → 16 is a major version jump** and was not exercised beyond the smoke test
   above. PrimeFaces 16 tightened some component APIs; a UI regression sweep of the xhtml views is
   still advisable.

5. One pre-existing compiler warning, unrelated to the upgrade:
   `Users.java:24` generates `equals`/`hashCode` without calling `superclass`. Add
   `@EqualsAndHashCode(callSuper = false)` to make the intent explicit.