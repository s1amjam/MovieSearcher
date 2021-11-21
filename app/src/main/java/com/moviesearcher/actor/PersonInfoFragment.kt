package com.moviesearcher.actor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.actor.adapter.combinedcredits.CombinedCreditsAdapter
import com.moviesearcher.actor.adapter.combinedcredits.images.PersonImagesAdapter
import com.moviesearcher.actor.model.images.Profile
import com.moviesearcher.actor.viewmodel.person.PersonViewModel
import com.moviesearcher.actor.viewmodel.personCombinedCredits.PersonCombinedCreditsViewModel
import com.moviesearcher.actor.viewmodel.personimages.PersonImagesViewModel
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentPersonInfoBinding
import com.moviesearcher.utils.Constants

private const val TAG = "PersonInfoFragment"

class PersonInfoFragment : BaseFragment() {
    private var _binding: FragmentPersonInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<PersonInfoFragmentArgs>()

    private val personViewModel: PersonViewModel by viewModels()
    private val personCombinedCreditsViewModel: PersonCombinedCreditsViewModel by viewModels()
    private val imagesViewModel: PersonImagesViewModel by viewModels()

    private lateinit var filmographyRecyclerView: RecyclerView
    private lateinit var imagesRecyclerView: RecyclerView
    private lateinit var personPhotoImageView: ImageView
    private lateinit var personName: TextView
    private lateinit var personBorn: TextView
    private lateinit var personDied: TextView
    private lateinit var personBio: TextView
    private lateinit var personKnownFor: TextView
    private lateinit var personInfoConstraintLayout: ConstraintLayout
    private lateinit var buttonSeeAllImages: Button
    private lateinit var mainCardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val personId = args.personId

        filmographyRecyclerView = binding.filmographyRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView

        personInfoConstraintLayout = binding.personInfoConstraintLayout
        personPhotoImageView = binding.photoImageView
        personName = binding.personNameTextView
        personBorn = binding.bornTextView
        personDied = binding.diedTextView
        personBio = binding.bioTextView
        buttonSeeAllImages = binding.buttonSeeAllImages
        mainCardView = binding.mainPersonInfoCardView
        personKnownFor = binding.knownForTextView

        personViewModel.getPersonById(personId).observe(
            viewLifecycleOwner,
            { personInfo ->
                Glide.with(this)
                    .load(Constants.IMAGE_URL + personInfo.profile_path)
                    .centerCrop()
                    .override(300, 500)
                    .into(personPhotoImageView)
                personName.text = personInfo.name
                personBorn.text = getString(R.string.born).format(personInfo.birthday)
                if (personInfo.deathday == null) {
                    personDied.visibility = View.GONE
                }
                personDied.text = getString(R.string.died).format(personInfo.deathday)
                personBio.text = personInfo.biography
                personKnownFor.text = personInfo.known_for_department

                personBio.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext()).setMessage(personInfo.biography)
                        .show()
                }
            })

        personCombinedCreditsViewModel.getCombinedCreditsByPersonId(personId)
            .observe(viewLifecycleOwner, { combinedCreditsItems ->
                val filmographyAdapter = CombinedCreditsAdapter(
                    combinedCreditsItems,
                    findNavController(),
                    accountId,
                    sessionId,
                )

                filmographyRecyclerView.apply {
                    adapter = filmographyAdapter
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
                filmographyAdapter.differ.submitList(combinedCreditsItems.cast)
            })

        imagesViewModel.getImagesByPersonId(personId)
            .observe(viewLifecycleOwner, { imagesItems ->
                val imageAdapter = PersonImagesAdapter(
                    imagesItems,
                )
                var tenImages = imagesItems.profiles

                while (tenImages?.size!! > 10) {
                    tenImages = tenImages.dropLast(1) as MutableList<Profile>?
                }

                imagesItems.apply {
                    profiles = tenImages
                }

                imagesRecyclerView.apply {
                    adapter = imageAdapter
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
                imageAdapter.differ.submitList(imagesItems.profiles)
            })

        buttonSeeAllImages.setOnClickListener {
            val action = PersonInfoFragmentDirections.actionPersonInfoFragmentToImagesFragment()
            action.movieId = personId.toString()

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}