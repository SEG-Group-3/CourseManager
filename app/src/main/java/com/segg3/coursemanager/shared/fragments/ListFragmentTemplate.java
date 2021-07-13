package com.segg3.coursemanager.shared.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.databinding.FragmentListViewBinding;
import com.segg3.coursemanager.shared.adapters.GenericCardAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ListFragmentTemplate<T, A extends GenericCardAdapter<T>> extends Fragment {
    public FragmentListViewBinding binding;
    public String query = "";
    public List<T> currentList;
    public List<T> filteredList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater);
        LinearLayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        binding.courseRecyclerView.setLayoutManager(layoutManager);
        binding.courseRecyclerView.scrollToPosition(0);
        binding.floatingActionButton.setOnClickListener(this::onAddClicked);

        // Update UI on change
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                updateItems(newText);
                return false;
            }
        });

        // Swipe stuff to the left to delete it
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getLayoutPosition();
                if (onItemSwiped(currentList.get(pos))) {
                    currentList.remove(pos);
                }
                updateItems(query);
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.courseRecyclerView);

        return binding.getRoot();
    }


    /**
     * @param query The search query taken from the user
     * @param items The items to filter
     * @return An adapter representing the filtered result
     */
    public abstract A filterQuery(String query, List<T> items);

    public abstract boolean onItemSwiped(T item);

    public void setItems(@NonNull A adapter) {
        this.currentList = adapter.itemList;
        updateItems(query);
    }

    public void forceUpdate(){
        updateItems(query);
    }

    private void updateItems(String q) {
        if (currentList == null)
            return;
        A adapter = filterQuery(q, new ArrayList<>(currentList));
        filteredList = adapter.itemList;
        adapter.setOnClickListener(this::onItemClicked);
        binding.courseRecyclerView.setAdapter(adapter);
    }


    public abstract void onItemClicked(T item);

    public abstract void onAddClicked(View view);

    private void onItemClicked(@NonNull View v) {
        int position = binding.courseRecyclerView.getChildLayoutPosition(v);
        onItemClicked(filteredList.get(position));
    }
}
