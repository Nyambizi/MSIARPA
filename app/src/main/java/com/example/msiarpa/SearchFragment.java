package com.example.msiarpa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private ListView searchResultsListView;
    private FirebaseFirestore db;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = (MaterialSearchBar) view.findViewById(R.id.searchBar);
        searchBar.setHint("Custom hint");
        searchBar.setSpeechMode(true);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
        //restore last queries from disk
        List<String> lastSearches = loadSearchSuggestionFromDisk();
        if(lastSearches == null) {
            lastSearches = new ArrayList<>();
        }
        searchBar.setLastSuggestions(lastSearches);
        //Inflate menu
        MenuInflater inflater1 = getActivity().getMenuInflater();
        Menu menu = (Menu) searchBar.getMenu();
        inflater1.inflate(R.menu.main, menu);

        searchResultsListView = (ListView) view.findViewById(R.id.search_results_listview);

        db = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //save last queries to disk
        List<String> suggestions = searchBar.getLastSuggestions();
        if(suggestions!= null) {
            saveSearchSuggestionToDisk(suggestions);
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled? "enabled" : "disabled";
        Toast.makeText(getActivity(), "Search " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        if(text!= null) {
            searchDatabase(text.toString());
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                // drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                openVoiceRecognizer();
        }
    }

    private void searchDatabase(String query) {
        db.collection("students").whereEqualTo("Name", query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    List<String> searchResults = new ArrayList<>();
                    for (DocumentSnapshot document : documents) {
                        String name = document.getString("Name");
                        String email = document.getString("Email");
                        String gender = document.getString("Gender");
                        String phoneNumber = document.getString("phoneNumber");

                        searchResults.add("Name: " + name + ", Email: " + email + ", Gender: " + gender + ", Phone Number: " + phoneNumber);
                    }

                    SearchResultsAdapter adapter = new SearchResultsAdapter(getActivity(), searchResults);
                    searchResultsListView.setAdapter(adapter);

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                }
            }
        });
    }

    private void openVoiceRecognizer() {
        // implement your voice recognizer logic here
    }

    private List<String> loadSearchSuggestionFromDisk() {
        // implement your logic to load search suggestions from disk
        // for now, return an empty list
        return new ArrayList<>();
    }

    private void saveSearchSuggestionToDisk(List<String> suggestions) {
        // implement your logic to save search suggestions to disk
    }
}