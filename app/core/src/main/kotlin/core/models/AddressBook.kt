package core.models

data class PersonEntry(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val gender: Gender?,
    val age: Int?,
    val phoneNumber: String,
    val email: String,
    val postalAddress: PostalAddress?
) {
    enum class Gender {
        MALE,
        FEMALE
    }

    data class PostalAddress(
        val address1: String,
        val address2: String?,
        val city: String,
        val state: String,
        val country: String
    ) {
        companion object
    }

    companion object
}

class PersonEntryNotFoundException(
    searchCriteria: String
) : RuntimeException("Person entry not found for search criteria: $searchCriteria")
