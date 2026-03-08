rootProject.name = "TypedConfig"

// Production projects
include("codegen", "runtime", "runtime-interfaces")
// Other sources
include("runtime-source-json")
// Plugins
include("typedconfig-gradle-plugin")
// Modules for third-party integration
include("runtime-module-koin")

// Test projects
include("codegen-test-config", "tests")

include("runtime-test-common")
