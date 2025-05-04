package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.repository.UserRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {
    UserRepository userRepository;
    MutableLiveData<List<String>> search = new MutableLiveData<>();

    public SearchViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<List<String>> getSearch() {
        return search;
    }

    public void addSearch(String search) {
        userRepository.addSearch(search);
    }

    public void getHistory() {
        userRepository.getSearch(result -> {
            search.setValue(result);
        });
    }

    public void deleteHistory(String search) {
        userRepository.deleteSearch(search);
    }
}
