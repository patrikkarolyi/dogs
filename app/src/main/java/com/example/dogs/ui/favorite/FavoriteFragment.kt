package com.example.dogs.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogs.databinding.FragmentFavoriteBinding
import com.example.dogs.ui.list.adapter.BreedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), BreedAdapter.Listener {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()
    private val adapter = BreedAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.adapter = adapter

        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshAllBreeds()
        }
        binding.starButton.setOnClickListener {
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToListFragment()
            )
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeContainer.isRefreshing = it
        }
        viewModel.breeds.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteBreeds()
    }

    override fun onItemSelected(id: String) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
                breedId = id
            )
        )
    }

    override fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateBreedFavoriteById(id, newIsFavorite)
    }
}