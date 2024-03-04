package core.usecase

import core.models.PersonEntry

interface RandomPersonUsecase {
    suspend fun populateRandomPerson(): PersonEntry
}
