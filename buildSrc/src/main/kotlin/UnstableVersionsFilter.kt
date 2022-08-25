// TODO: Replace with the built-in filter when the issue is solved
// ref: https://github.com/ben-manes/gradle-versions-plugin/issues/440

/**
 * Filters versions by rejecting all unstable ones
 */
object UnstableVersionsFilter {

    // order is important!
    private val unstablePostfixes = listOf("preview", "alpha", "beta", "m", "cr", "rc")

    fun rejectVersion(current: String, new: String): Boolean =
        stabilityLevel(current) > stabilityLevel(new)

    private fun stabilityLevel(version: String): Int {
        with(unstablePostfixes) {
            forEachIndexed { index, postfix ->
                val regex = ".*[.\\-]$postfix[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE)
                if (version.matches(regex)) return index
            }
            return size
        }
    }
}