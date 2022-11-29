package com.example.dogs.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(){

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val breedObserver = Observer<List<RoomBreedData>> { newBreeds ->
            Log.d("MyApp", newBreeds.joinToString { it.id })
        }
        viewModel.breeds.observe(viewLifecycleOwner, breedObserver)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBreeds()
    }
}