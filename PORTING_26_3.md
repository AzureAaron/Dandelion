# Building Dandelion for Minecraft 26.3

Apply the reviewed port to [AzureAaron/Dandelion master f3473b821a1fc59f4bfad41272bda25f7de5819f](https://github.com/AzureAaron/Dandelion/tree/f3473b821a1fc59f4bfad41272bda25f7de5819f), retaining the LGPL 3.0-or-later license. Use the committed wrapper with Java 25. Minecraft 26.3, Fabric Loader 0.19.5 and Fabric API 0.161.0+26.3 are genuine native dependencies.

Build and locally publish the separate reviewed MoulConfig v4 port first. Its common sources require a genuine Java 8 compiler and its modern platform requires Java 25. Choose `JDK8`, `JDK25` and `MOUL_MAVEN` outside the source checkouts, then run from the MoulConfig root:

```sh
export JAVA_HOME="$JDK25"
./gradlew --no-daemon --max-workers=2 --configure-on-demand \
  :modern:modern-26.3:remapJar :modern:modern-26.3:check \
  :modern:modern-26.3:publishMavenPublicationToPortDependenciesRepository \
  -Dmoulconfig.portVersion=4.7.2-codex26.3.1 \
  -Porg.gradle.java.installations.paths="$JDK8,$JDK25" \
  -PportDependencyRepository="$MOUL_MAVEN"
```

The coordinate is `org.notenoughupdates.moulconfig:modern-26.3:4.7.2-codex26.3.1`; the tested runtime JAR SHA256 is `eebfbf6ff1cfacc117f47b75877b1a5df4c239204633dbd23bc68b8c76722ce0`. Keep the MoulConfig build's real dependency verification and provenance. The existing Unimined SNAPSHOT must be pinned/retained for strict binary reproduction; future snapshots need not produce identical bytes.

Choose an external `PORT_DEPENDENCIES` directory, and from Dandelion's own root run:

```sh
export JAVA_HOME="$JDK25"
./gradlew --no-daemon --max-workers=2 build \
  publishMavenJavaPublicationToPortWorkspaceRepository \
  -PmoulconfigPortRepository="$MOUL_MAVEN" \
  -PportMavenRepository="$PORT_DEPENDENCIES"
```

`moulconfigPortRepository` optionally supplies the dependency repository. `portMavenRepository` enables only the caller-selected `portWorkspace` publication destination; the exact task above does not publish remotely. Use external absolute directories or file URIs to avoid source-layout assumptions; relative paths resolve against this root.

The real shaded production library is published as `net.azureaaron:dandelion:1.0.0-alpha.22+26.3`, alongside its generated POM and sources JAR. MoulConfig remains relocated in the intended namespace, with service files merged. The production build, checkstyle and this local publication passed. Unit tests have no sources, so no unit coverage is claimed. Consumer GUI/world and full-pack validation are separate checks.

MoulConfig's native 26.3 integer representation uses SDL keyboard values and encoded logical mouse values. A consumer persisting historical raw GLFW keybind IDs needs its own reviewed migration. Do not silently replace the library behind such a configuration schema. No owner configuration/account, DevAuth or client launch is required for the build.
