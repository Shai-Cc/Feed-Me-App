package com.feed_me_app.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feed_me_app.data.local.entities.PetEntity
import com.feed_me_app.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    val allPets: StateFlow<List<PetEntity>> = repository.allPets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedPet = MutableStateFlow<PetEntity?>(null)
    val selectedPet: StateFlow<PetEntity?> = _selectedPet.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPet(petId: Int) {
        viewModelScope.launch {
            repository.getPetById(petId).collect { pet ->
                _selectedPet.value = pet
            }
        }
    }

    fun insertPet(pet: PetEntity) {
        viewModelScope.launch {
            repository.insertPet(pet)
        }
    }

    fun updatePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.updatePet(pet)
        }
    }

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.deletePet(pet)
        }
    }

    fun toggleFavorite(petId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(petId, isFavorite)
        }
    }

    fun updateLastFeedingTime(petId: Int) {
        viewModelScope.launch {
            repository.updateLastFeedingTime(petId, System.currentTimeMillis())
        }
    }

    fun getFilteredPets(): StateFlow<List<PetEntity>> {
        val filtered = MutableStateFlow<List<PetEntity>>(emptyList())
        viewModelScope.launch {
            allPets.collect { pets ->
                val query = _searchQuery.value.lowercase()
                filtered.value = if (query.isEmpty()) {
                    pets
                } else {
                    pets.filter { it.name.lowercase().contains(query) }
                }
            }
        }
        return filtered
    }
}