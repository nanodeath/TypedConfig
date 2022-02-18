rootProject.name = "TypedConfig"

// Production projects
include("generator", "runtime", "runtime-interfaces")
// Other sources
include("runtime-source-json")

// Test projects
include("generator-test-config", "tests")
