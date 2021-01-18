package com.nasinha.digitalspace.favorite.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nasinha.digitalspace.R
import com.nasinha.digitalspace.favorite.db.AppDatabase
import com.nasinha.digitalspace.favorite.entity.FavoriteEntity
import com.nasinha.digitalspace.favorite.repository.FavoriteRepository
import com.nasinha.digitalspace.favorite.viewmodel.FavoriteViewModel
import com.nasinha.digitalspace.favorite.viewmodel.FavoriteViewModelFactory
import com.nasinha.digitalspace.utils.Constants.IMAGE
import com.nasinha.digitalspace.utils.Constants.TITLE
import com.nasinha.digitalspace.utils.Constants.VIDEO
import com.nasinha.digitalspace.utils.FavoriteUtils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class FavoriteScreenFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var _favoriteViewModel: FavoriteViewModel
    private lateinit var _favorite: FavoriteEntity
    private var _translatePrefs: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _view = view
        addViewModel()
        translatePrefHandler()
        backBtnHandler()
        argumentsHandler()
    }

    private fun addViewModel() {
        _favoriteViewModel = ViewModelProvider(
            this,
            FavoriteViewModelFactory(
                FavoriteRepository(
                    AppDatabase.getDatabase(_view.context).favoriteDao()
                )
            )
        ).get(FavoriteViewModel::class.java)
    }

    private fun translatePrefHandler() {
        val prefs =
            requireActivity().getSharedPreferences("switch_prefs", AppCompatActivity.MODE_PRIVATE)
        _translatePrefs = prefs?.getBoolean("SWITCH_PREFS", false)
    }

    private fun backBtnHandler() {
        val backBtn = _view.findViewById<ImageButton>(R.id.ibBackFavoriteScreen)
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun argumentsHandler() {
        val imageArgument = arguments?.getString(IMAGE)!!

        _favoriteViewModel.getFavorite(imageArgument).observe(viewLifecycleOwner, {
            addInfoToFavorite(it)
        })
    }

    private fun addInfoToFavorite(favorite: FavoriteEntity) {
        val imageView = _view.findViewById<ImageView>(R.id.ivImageFavoriteScreen)
        val dateView = _view.findViewById<TextView>(R.id.tvDateFavoriteScreen)
        val titleView = _view.findViewById<TextView>(R.id.tvTitleFavoriteScreen)
        val textView = _view.findViewById<TextView>(R.id.tvTextFavoriteScreen)

        titleView.text = if (_translatePrefs == true) favorite.titleBr else favorite.title
        textView.text = if (_translatePrefs == true) favorite.textBr else favorite.text

        dateView.text = FavoriteUtils.dateModifier(favorite.date)

        when (favorite.type) {
            IMAGE -> {
                Picasso.get().load(favorite.image).into(imageView)
                imageClickHandler(imageView, favorite.image, titleView)
                shareButton(favorite.image)
            }
            VIDEO -> {
                Toast.makeText(_view.context, "é video sô!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun shareButton(imageArgument: String) {
        val shareBtn = _view.findViewById<ImageButton>(R.id.ibShareFavoriteScreen)
        shareBtn.setOnClickListener {

            lifecycleScope.launch {
                val imageBitmap = FavoriteUtils.getBitmapFromView(_view, imageArgument)
                val text = _view.findViewById<TextView>(R.id.tvTextFavoriteScreen).text.toString()
                activity?.let { FavoriteUtils.shareImageText(it, _view, imageBitmap, text) }
            }

        }
    }

    private fun imageClickHandler(
        imageView: ImageView,
        imageArgument: String,
        titleText: TextView,
    ) {
        imageView.setOnClickListener {
            val navController = findNavController()
            val bundle = bundleOf(IMAGE to imageArgument, TITLE to titleText.text.toString())
            navController.navigate(
                R.id.action_favoriteScreenFragment_to_favoriteImageFragment,
                bundle
            )
        }
    }
}