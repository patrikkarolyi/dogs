package com.example.dogs.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.ui.list.ListViewState.Content
import com.example.dogs.ui.list.ListViewState.Initial
import com.example.dogs.ui.list.ListViewState.NetworkError
import com.example.dogs.ui.list.ListViewState.Refreshing
import com.example.dogs.ui.list.adapter.BreedAdapter
import com.example.dogs.ui.theme.Black
import com.example.dogs.ui.theme.DogsTheme
import com.example.dogs.ui.theme.Grey
import com.example.dogs.ui.theme.Purple500
import com.example.dogs.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(), BreedAdapter.Listener {

    //private lateinit var binding: FragmentListBinding
    //private val adapter = BreedAdapter(this)
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ListScreen(onItemClicked = { onItemSelected(it) })
            }
        }
    }

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

         binding.rv.adapter = adapter
         binding.swipeContainer.setOnRefreshListener {
             viewModel.refreshAllBreeds()
         }
         binding.toolbar.starBreedsButton.setOnClickListener {
             findNavController().navigate(
                 ListFragmentDirections.toFavoriteDogs()
             )
         }
         binding.toolbar.starImagesButton.setOnClickListener {
             findNavController().navigate(
                 ListFragmentDirections.toFavoriteImages()
             )
         }
         binding.filterEt.setOnEditorActionListener { _, actionId, _ ->
             if (actionId == EditorInfo.IME_ACTION_DONE) {
                 viewModel.updateFilters(binding.filterEt.text.toString())
                 (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                     view.windowToken, 0
                 )
                 true
             } else false
         }

         viewModel.state.observe(viewLifecycleOwner) {
             render(it)
         }
     }*/

    /*    private fun render(viewState: ListViewState) {
            when (viewState) {
                Initial -> {}
                Refreshing -> binding.swipeContainer.isRefreshing = true
                is Content -> {



                    binding.emptyList.isVisible = viewState.result.isEmpty()
                    adapter.submitList(viewState.result)
                    binding.swipeContainer.isRefreshing = false
                    if(viewState.clearEditText){
                        binding.filterEt.text?.clear()
                    }
                }
                is NetworkError -> {
                    showError(viewState.message)
                    binding.swipeContainer.isRefreshing = false
                }
            }
        }*/

    override fun onResume() {
        super.onResume()
        viewModel.getAllBreeds()
    }

    private fun showError(message: String) {
        /*Snackbar.make(binding.swipeContainer, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.refresh)) {
                viewModel.refreshAllBreeds()
            }
            .show()*/
    }

    override fun onItemSelected(id: String) {
        findNavController().navigate(
            ListFragmentDirections.toDetails(
                breedId = id
            )
        )
    }

    override fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateBreedFavoriteById(id, newIsFavorite)
    }
}

@Composable
fun ListScreen(viewModel: ListViewModel = viewModel(), onItemClicked: (String) -> Unit) {
    when (val state = viewModel.state) {
        Initial -> ListInitial()
        Refreshing -> ListLoading()
        is Content -> ListContent(state.result, onItemClicked)
        is NetworkError -> ListError()
    }
}

@Preview
@Composable
fun PreviewListScreen() {
    ListInitial()
}

@Composable
fun ListContent(newItems: List<RoomBreedData> = emptyList(), onItemClicked: (String) -> Unit) {
    DogsTheme {
        LazyColumn {
            items(newItems) { item ->
                ListItemContent(
                    item = item,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@Composable
fun ListItemContent(item: RoomBreedData, onItemClicked: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Black)
            .padding(vertical = 5.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Grey)
                .padding(5.dp)
                .clickable {
                    println("Clicked ${item.breedName}.")
                }
        ) {
            Column(
                Modifier
                    .background(Purple500)
                    .padding(5.dp)
            ) {
                Text(
                    text = item.breedName,
                    color = White
                )
            }
        }
    }
}

@Composable
fun ListError() {
    Row {
        Column {
            Text(text = "Error")
            Text(text = "description")
        }
    }
}

@Composable
fun ListInitial() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        text = "Initial"
    )
}

@Composable
fun ListLoading() {
    Row {
        Column {
            Text(text = "Loading")
            Text(text = "description")
        }
    }
}
