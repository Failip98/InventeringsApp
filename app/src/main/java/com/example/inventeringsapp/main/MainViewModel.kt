package com.example.inventeringsapp.main

import com.example.inventeringsapp.repository.Repository
import javax.inject.Inject

class MainViewModel  @Inject constructor(val repository: Repository) {
    var text = "MainViewModel"
}